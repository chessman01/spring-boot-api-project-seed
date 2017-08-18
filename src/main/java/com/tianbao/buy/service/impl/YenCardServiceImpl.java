package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.Context;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.domain.YenCard;
import com.tianbao.buy.manager.YenCardManager;
import com.tianbao.buy.service.BaseService;
import com.tianbao.buy.service.CouponService;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.YenCardService;
import com.tianbao.buy.utils.MoneyUtils;
import com.tianbao.buy.vo.Button;
import com.tianbao.buy.vo.CouponVO;
import com.tianbao.buy.vo.YenCardVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class YenCardServiceImpl extends BaseService implements YenCardService{
    @Value("${biz.card.discount.rate}")
    private int cardDiscountRate;

    @Resource
    private YenCardManager yenCardManager;

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

        YenCard card;
        if (cardId > NumberUtils.LONG_ZERO) {
            card = getSpecify(user.getId(), cardId);
        } else {
            card = getDefault(user.getId());
        }

        YenCardVO cardVO = convert2CardVO(card);

        Context context = new Context();
        context.setUser(user);
        context.setCard(card);

        // 2. 找到充值模版
        List<CouponVO> templates = couponService.getCardRechargeTemplate(context);
        if (CollectionUtils.isEmpty(templates)) throw new BizException("没有瘾卡充值模版");
        cardVO.setTemplates(templates);

        // 3. 找到充值礼券
        List<CouponVO> couponVOs = couponService.getCoupon4Recharge(user.getId(),
                context.getTemplate().getRulePrice(), null, context);
        cardVO.setCouponVOs(couponVOs);

        // 4. 充值按钮
        Button button = new Button();

        button.setEvent(new Button.Event("http://h5.m.taobao.com", "click"));
        String price = MoneyUtils.format(2, (context.getTemplate().getRulePrice() - context.getCoupon().getPrice()) / 100);
        button.setTitle("支付（￥" + price + "）");

        return cardVO;
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

    @Override
    public YenCard getDefault(long userId){
        List<YenCard> cards = getCardByUser(userId);

        if (!CollectionUtils.isEmpty(cards)
                && cards.get(NumberUtils.INTEGER_ZERO).getType().equals(YenCardVO.Type.NORMAL)) {
            return cards.get(NumberUtils.INTEGER_ZERO);
        }

        throw new BizException("名下未找到指定的瘾卡");
    }

    @Override
    public YenCard getSpecify(long userId, final long cardId){
        List<YenCard> YenCards = getCardByUser(userId);

        for (YenCard card : YenCards) {
            if (card.getId().equals(cardId)) return card;
        }

        throw new BizException("名下未找到指定的瘾卡");
    }

    @Override
    public void initNormalCard(long userId) {
        checkArgument(userId > NumberUtils.LONG_ZERO);

        YenCard card = new YenCard();

        card.setUserId(userId);
        card.setDiscountRate(cardDiscountRate);

        yenCardManager.save(card);
    }

    public List<YenCard> getCardByUser(long userId) {
        Condition condition = new Condition(YenCard.class);
        condition.orderBy("type"); // 统一按卡类型排序

        condition.createCriteria().andCondition("user_id=", userId)
                .andCondition("status=", YenCardVO.Status.NORMAL.getCode());

        return yenCardManager.findByCondition(condition);
    }

    private YenCardVO convert2CardVO(YenCard YenCard) {
        String gift = MoneyUtils.format(2, YenCard.getGiftAccount() / 100f);
        String cash = MoneyUtils.format(2, YenCard.getCashAccount() / 100f);
        String total = MoneyUtils.format(2, (YenCard.getGiftAccount() + YenCard.getCashAccount()) / 100f);

        String discount = String.format("消费立打%s折", MoneyUtils.format(1, YenCard.getDiscountRate() / 10f));

        return new YenCardVO(YenCard.getId(), "http://gw.alicdn.com/tps/TB1LNMxPXXXXXbhaXXXXXXXXXXX-183-129.png",
                gift, cash, total,
                discount, "http://xxx.xxx.com/recharge.xx", null, null, null);
    }

    private List<YenCardVO> convert2CardVO(List<YenCard> YenCards) {
        List<YenCardVO> voList = Lists.newArrayList();

        for (YenCard YenCard : YenCards) {
            voList.add(convert2CardVO(YenCard));
        }

        return voList;
    }
}
