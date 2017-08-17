package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.domain.YenCard;
import com.tianbao.buy.manager.YenCardManager;
import com.tianbao.buy.service.BaseService;
import com.tianbao.buy.service.CouponService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.YenCardService;
import com.tianbao.buy.vo.YenCardVO;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.List;

@Service
public class YenCardServiceImpl extends BaseService implements YenCardService{
    @Resource
    private YenCardManager YenCardManager;

    @Resource
    private UserService userService;

    @Resource
    private CouponService couponService;

    @Override
    public List<YenCardVO> getAllByUser() {
        User user = userService.getUserByWxUnionId();
        List<YenCard> YenCards = getCardByUser(user.getId());

        return convert2CardVO(YenCards);
    }

    @Override
    public YenCardVO build(long cardId) {
        // 1. 找到用户的瘾卡
        User user = userService.getUserByWxUnionId();

        YenCard card = getSpecifyCard(user.getId(), cardId);
        YenCardVO cardVO = convert2CardVO(card);

//        List<CouponVO> couponVOs = couponService.getCoupon4Recharge(user.getId(), )
//
//        cardVO.setCouponVOs(couponVOs);
//
//        return cardVO;
        return null;
    }

    @Override
    public String create(long cardId, long rechargeId, long couponId) {
        // 1. 找到用户的瘾卡
//        Long userId = getUserByWxUnionId().getId();
//
//        YenCardVO YenCardVO = getYenCard(userId, cardId);

        // 2. 找礼券

        return null;
    }

    @Override
    public YenCardVO adjust(long cardId, long rechargeId, long couponId) {
        return null;
    }

    private YenCard getSpecifyCard(long userId, final long cardId){
        List<YenCard> YenCards = getCardByUser(userId);

        for (YenCard card : YenCards) {
            if (card.getId().equals(cardId)) return card;
        }

        throw new BizException("名下未找到指定的瘾卡");
    }

    public List<YenCard> getCardByUser(long userId) {
        Condition condition = new Condition(YenCard.class);
        condition.orderBy("type"); // 统一按卡类型排序

        condition.createCriteria().andCondition("user_id=", userId)
                .andCondition("status=", YenCardVO.Status.NORMAL.getCode());

        return YenCardManager.findByCondition(condition);
    }

    private YenCardVO convert2CardVO(YenCard YenCard) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(2);

        String gift = numberFormat.format(YenCard.getGiftAccount() / 100f);
        String cash = numberFormat.format(YenCard.getCashAccount() / 100f);
        String total = numberFormat.format((YenCard.getGiftAccount() + YenCard.getCashAccount()) / 100f);

        numberFormat.setMaximumFractionDigits(1);
        String discount = String.format("消费立打%s折", numberFormat.format(YenCard.getDiscountRate() / 10f));

        return new YenCardVO(YenCard.getId(), "http://gw.alicdn.com/tps/TB1LNMxPXXXXXbhaXXXXXXXXXXX-183-129.png",
                gift, cash, total,
                discount, "http://xxx.xxx.com/recharge.xx", null);
    }

    private List<YenCardVO> convert2CardVO(List<YenCard> YenCards) {
        List<YenCardVO> voList = Lists.newArrayList();

        for (YenCard YenCard : YenCards) {
            voList.add(convert2CardVO(YenCard));
        }

        return voList;
    }
}
