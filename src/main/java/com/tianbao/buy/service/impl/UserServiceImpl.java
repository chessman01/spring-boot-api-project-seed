package com.tianbao.buy.service.impl;

import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.manager.UserManager;
import com.tianbao.buy.service.CouponService;
import com.tianbao.buy.service.OrderService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.YenCardService;
import com.tianbao.buy.utils.MoneyUtils;
import com.tianbao.buy.vo.*;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserManager userManager;

    @Resource
    private YenCardService cardService;

    @Resource
    private CouponService couponService;

    @Resource
    private OrderService orderService;

    @Autowired
    protected WxMpService wxMpService;

    @Override
    public UserVO self(User user) {
        UserVO userVO = new UserVO();

        List<YenCardVO> cards = cardService.getCardByUser(user);
        List<OrderVO> orders = orderService.get(OrderVO.Status.ORDER.getCode(), user);

        userVO.setCards(cards);
        userVO.setOrders(orders);

        userVO.setBeyond(String.format("您已经超过 %s %%同时健身的同学", MoneyUtils.format(0, Math.random() * 100)));
        userVO.setCalorieTotal(MoneyUtils.format(1, user.getCalorieTotal()));
        userVO.setDurationTotal(new UserVO.Digital("10", "1/4", "10.25"));
        userVO.setDurationWeek(new UserVO.Digital("11", "2/4", "11.5"));
        userVO.setPoint(user.getPoint());
        userVO.setUserId(user.getId());
        return userVO;
    }

    private boolean isOldUser(User user) {
        int boughtNum = orderService.getBoughtNum(user.getId());

        if (boughtNum > NumberUtils.INTEGER_ZERO) return true;

        if (user.getReferrerId() != null && user.getReferrerId() > 0) {
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public void recommend(long inviterId, String code, String phone, User self) {
        // todo 验证手机

        User model = new User();
        User inviter = getUserByuserId(inviterId);

        if (isOldUser(self)) throw new BizException("您已是老用户");

        Condition condition = new Condition(User.class);
        condition.createCriteria().andCondition("id=", self.getId())
                .andCondition("status=", CouponVO.Status.NORMAL.getCode());

        model.setPhone(phone);
        model.setReferrerId(inviterId);

        userManager.update(model, condition);

        if (inviter == null) return;

        CouponTemplate couponTemplate = couponService.getRecommendTemplate();
        couponService.obtainRecommend(couponTemplate.getId(), inviter.getId(), (byte) 3);
        couponService.obtainRecommend(couponTemplate.getId(), self.getId(), (byte) 4);
    }

    @Override
    public boolean validatePhone(String code, String phone, User user) {
        // 验证code

        User model = new User();

        model.setId(user.getId());
        model.setPhone(phone);

        userManager.update(model);

        return true;
    }

    @Override
    public boolean getPin(String phone, boolean isObtainRecommend, User user) {
        if (isObtainRecommend) {

            if (isOldUser(user)) throw new BizException("您已是老用户");
        }

        // todo 发放一个短信验证码
        return true;
    }

    @Override
    public InvitationVO invitation(User user) {
        InvitationVO invitationVO = new InvitationVO();

        CouponTemplate couponTemplate = couponService.getRecommendTemplate();

        int num = couponService.getCouponNum(couponTemplate.getId(), user.getId());

        invitationVO.setTotalPrize(MoneyUtils.format(2, num * couponTemplate.getPrice() / 100));
        invitationVO.setInviterId(user.getId());

        return invitationVO;
    }

    @Override
    public void updatePhone(long userId, String phone) {
        User user = new User();
        user.setId(userId);
        user.setPhone(phone);
        userManager.update(user);
    }

    @Override
    public User getUserByuserId(long userId) {
        User user = userManager.findById(userId);
        if (user == null || !user.getStatus().equals(UserVO.Status.NORMAL.getCode())) throw new BizException("用户状态异常，请联系系统方");
        return user;
    }

    @Override
    public User getUserByWxOpenId(String openId, String lang) throws WxErrorException {
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId, lang);
        String wxUnionId = wxMpUser.getUnionId();

        if (StringUtils.isBlank(wxUnionId)) {
            logger.error("uid为空");
            throw new BizException("uid为空");
        }

        User user = userManager.findBy("wxUnionId", wxUnionId);

        /* 当用户是首次使用时，初始化用户和瘾卡 */
        if (user == null) {
            boolean isMale = "男".equals(wxMpUser.getSex());
            user = init(wxMpUser.getOpenId(), wxMpUser.getUnionId(), wxMpUser.getNickname(), isMale, wxMpUser.getHeadImgUrl(),
                    0, wxMpUser.getCountry(), wxMpUser.getProvince(), wxMpUser.getCity());
        }

        if (user == null) {
            logger.error(String.format("用户没发现.uic[%s]", wxUnionId));
            throw new BizException("用户没发现");
        }

        if (!user.getStatus().equals(UserVO.Status.NORMAL.getCode())) {
            logger.error(String.format("用户状态异常。用户id[%d]，状态[%d]", user.getId(), user.getStatus()));
            throw new BizException("用户状态异常，请联系管理员");
        }

        return user;
    }

    /* 初始化用户 */
    @Transactional
    private User init(String wxOpenId, String wxUnionId, String nick, boolean isMale, String avatar
            , long refId, String country, String province, String city) {
        User user = new User();

        user.setWxOpenId(wxOpenId);
        user.setWxUnionId(wxUnionId);
        user.setNick(nick);
        user.setSex(isMale);
        user.setAvatar(avatar);
        user.setReferrerId(refId);
        user.setCountry(country);
        user.setProvince(province);
        user.setCity(city);

        userManager.save(user);

        user = userManager.findBy("wxUnionId", wxUnionId);

        cardService.initNormalCard(user.getId());

        return user;
    }


}
