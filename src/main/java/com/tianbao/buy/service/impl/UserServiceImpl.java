package com.tianbao.buy.service.impl;

import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.domain.YenCard;
import com.tianbao.buy.manager.UserManager;
import com.tianbao.buy.manager.YenCardManager;
import com.tianbao.buy.service.BaseService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class UserServiceImpl extends BaseService implements UserService {
    @Value("${biz.card.discount.rate}")
    private int cardDiscountRate;

    @Resource
    protected UserManager userManager;

    @Resource
    private YenCardManager yenCardManager;

    @Override
    public User getUserByuserId(long userId) {
        User user = userManager.findById(userId);
        if (!user.getStatus().equals(UserVO.Status.NORMAL.getCode())) throw new BizException("用户状态异常，请联系系统方");
        return user;
    }

    @Override
    @Transactional
    public User getUserByWxUnionId() {
        // todo 这里是要依据微信接口拿到用户uid，到userManager查用户，然后得到用户ID
        String wxUnionId = "12345";

        if (StringUtils.isBlank(wxUnionId)) {
            // todo log
            throw new BizException("系统错误");
        }

        User user = userManager.findBy("wxUnionId", wxUnionId);

        if (user == null) {
            initUser(wxUnionId, wxUnionId, "nick", true, "http://gw.alicdn.com/tps/TB1FDOHLVXXXXcZXFXXXXXXXXXX-183-129.png",
                    0, "中国", "浙江", "杭州");

            user = userManager.findBy("wxUnionId", wxUnionId);

            initNormalCard(user.getId());
        }

        if (user == null) throw new BizException("用户没发现");
        if (!user.getStatus().equals(UserVO.Status.NORMAL.getCode())) throw new BizException("用户状态异常，请联系系统方");

        return user;
    }

    /* 当用户是首次使用时，初始化用户和瘾卡 */
    private void initUser(String wxOpenId, String wxUnionId, String nick, boolean isMale, String avatar
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
    }

    /* 初始化普通瘾卡 */
    private void initNormalCard(long userId) {
        checkArgument(userId > NumberUtils.LONG_ZERO);

        YenCard card = new YenCard();

        card.setUserId(userId);
        card.setDiscountRate(cardDiscountRate);

        yenCardManager.save(card);
    }
}
