package com.tianbao.buy.service.impl;

import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.CouponTemplate;
import com.tianbao.buy.domain.CouponUser;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.manager.CouponTemplateManager;
import com.tianbao.buy.manager.CouponUserManager;
import com.tianbao.buy.manager.UserManager;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.YenCardService;
import com.tianbao.buy.utils.MoneyUtils;
import com.tianbao.buy.vo.CouponVO;
import com.tianbao.buy.vo.InvitationVO;
import com.tianbao.buy.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserManager userManager;

    @Resource
    private YenCardService yenCardService;

    @Resource
    private CouponTemplateManager couponTemplateManager;

    @Resource
    private CouponUserManager couponUserManager;

    @Override
    public InvitationVO invitation() {
        InvitationVO invitationVO = new InvitationVO();
        User user = getUserByWxUnionId();

        Condition condition = new Condition(CouponTemplate.class);

        condition.createCriteria().andCondition("source=", CouponVO.Source.FRIEND.getCode())
                .andCondition("status=", CouponVO.Status.NORMAL.getCode());
        List<CouponTemplate> couponTemplates = couponTemplateManager.findByCondition(condition);

        if (CollectionUtils.isEmpty(couponTemplates)) throw new BizException("没找到邀请好友礼券");

        Condition couponUserManagerCondition = new Condition(CouponUser.class);

        couponUserManagerCondition.createCriteria().andCondition("coupon_template_id=", couponTemplates.get(NumberUtils.INTEGER_ZERO).getId())
                .andCondition("user_id=", user.getId());
        List<CouponUser> couponUsers = couponUserManager.findByCondition(couponUserManagerCondition);

        invitationVO.setTotalPrize(MoneyUtils.format(2, couponUsers.size()
                * couponTemplates.get(NumberUtils.INTEGER_ZERO).getPrice() / 100));
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
        if (!user.getStatus().equals(UserVO.Status.NORMAL.getCode())) throw new BizException("用户状态异常，请联系系统方");
        return user;
    }

    @Override

    public User getUserByWxUnionId() {
        // todo 这里是要依据微信接口拿到用户uid，到userManager查用户，然后得到用户ID
        String wxUnionId = "12345";

        if (StringUtils.isBlank(wxUnionId)) {
            logger.error("uid为空");
            throw new BizException("uid为空");
        }

        User user = userManager.findBy("wxUnionId", wxUnionId);

        /* 当用户是首次使用时，初始化用户和瘾卡 */
        if (user == null) {
            user = init(wxUnionId, wxUnionId, "nick", true, "http://gw.alicdn.com/tps/TB1FDOHLVXXXXcZXFXXXXXXXXXX-183-129.png",
                    0, "中国", "浙江", "杭州");
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

        yenCardService.initNormalCard(user.getId());

        return user;
    }


}
