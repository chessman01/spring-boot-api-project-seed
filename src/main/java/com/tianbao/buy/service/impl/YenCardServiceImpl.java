package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.*;
import com.tianbao.buy.manager.YenCardManager;
import com.tianbao.buy.service.*;
import com.tianbao.buy.utils.MakeOrderNum;
import com.tianbao.buy.utils.MoneyUtils;
import com.tianbao.buy.vo.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class YenCardServiceImpl implements YenCardService{
    private static Logger logger = LoggerFactory.getLogger(YenCardServiceImpl.class);

    @Value("${biz.card.discount.rate}")
    private int cardDiscountRate;

    @Resource
    private YenCardManager yenCardManager;

    @Resource
    private UserService userService;

    @Resource
    private CouponService couponService;

    @Resource
    private FundDetailService fundDetailService;

    @Resource
    private OrderService orderService;

    @Override
    public List<YenCardVO> getCardByUser() {
        User user = userService.getUserByWxUnionId();
        List<YenCard> YenCards = getCardByUser(user.getId());

        return convert2CardVO(YenCards);
    }

    @Override
    public YenCardVO build(Long cardId) {
        return render(cardId, null, null);
    }

    @Override
    public YenCardVO adjust(long cardId, long templateId, Long couponId) {
        checkArgument(cardId > NumberUtils.LONG_ZERO);
        checkArgument(templateId > NumberUtils.LONG_ZERO);
        return render(cardId, templateId, couponId);
    }

    @Override
    @Transactional
    public String create(long cardId, long templateId, Long couponUserId) {
        // 1. 找到用户的瘾卡
        User user = userService.getUserByWxUnionId();
        getSpecify(user.getId(), cardId); // 主要是判断下卡是否存在

        // 2. 找充值模版
        CouponTemplate rechargeTemplate = couponService.getTemplate(templateId);

        if (!rechargeTemplate.getStatus().equals(CouponVO.Status.RECHARGE.getCode()) ||
                !rechargeTemplate.getPayType().equals(CouponVO.PayType.RECHARGE.getCode())) {
            logger.error(String.format("充值模版无效。id=%d", templateId));
            throw new BizException("充值模版无效");
        }

        CouponTemplate couponTemplate = null;

        // 3. 找礼券
        if (couponUserId != null && couponUserId > NumberUtils.LONG_ZERO) {
            CouponUser couponUser = couponService.getCouponUser(couponUserId);

            if (!couponUser.getStatus().equals(CouponVO.Status.NORMAL.getCode())) {
                logger.error(String.format("礼券状态无效。id=%d", couponUserId));
                throw new BizException(String.format("礼券无效", couponUserId));
            }

            // 4. 礼券用的模版
            couponTemplate = couponService.getTemplate(couponUser.getCouponTemplateId());

            if (couponTemplate == null || couponTemplate.getRulePrice() > rechargeTemplate.getRulePrice()) {
                logger.error(String.format("礼券额度不匹配无效。id=%d", couponUserId));
                throw new BizException(String.format("礼券无效", couponUserId));
            }
        }

        String orderId = MakeOrderNum.makeOrderNum();

        List<OrderVO.PayDetail> payDetails = Lists.newArrayList();
        Map<String, OrderVO.PayDetail> payDetailMap = orderService.calFeeDetail(rechargeTemplate.getRulePrice(), NumberUtils.INTEGER_ONE, null,
                couponTemplate, rechargeTemplate, payDetails, false);


        int price4wx = orderService.calRealPay(payDetailMap).getOriginFee();

        List<FundDetail> fundDetails = fundDetailService.incomeByRecharg(orderId, payDetailMap, price4wx);

        // 生成订单
        OrderMain order = orderService.make(orderId, null, user.getId(), null, payDetailMap, price4wx,
        cardId, couponUserId, OrderVO.Status.PENDING_PAY.getCode(), OrderVO.Type.CARD.getCode());

        orderService.sava(order);

        // 锁定礼券
        if (couponUserId != null && couponUserId > NumberUtils.LONG_ZERO) {
            couponService.updateCouponUserStatus(couponUserId, CouponVO.Status.PENDING.getCode(),
                    CouponVO.Status.NORMAL.getCode());
        }

        return orderId;
    }

    @Override
    public int getCash(List<FundDetail> details) {
        int cash = 0;
        for (FundDetail detail : details) {
            if (detail.getOrigin().equals(FundDetailVO.Channel.WEIXIN.getCode())) {
                cash = detail.getPrice();
            }
        }

        return cash;
    }

    @Override
    public int getGift(List<FundDetail> details) {
        int gift = 0;
        for (FundDetail detail : details) {
            if (!detail.getOrigin().equals(FundDetailVO.Channel.WEIXIN.getCode())) {
                gift = gift + detail.getPrice();
            }
        }

        return gift;
    }

    @Override
    public void updatePrice(int newCash, int oldCash, int newGift, int oldGift, long id) {
        Condition condition = new Condition(YenCard.class);

        condition.createCriteria().andCondition("id=", id)
                .andCondition("status=", YenCardVO.Status.NORMAL.getCode())
                .andCondition("cash_account=", oldCash).andCondition("gift_account=", oldGift);

        YenCard card = new YenCard();
        card.setGiftAccount(newGift);
        card.setCashAccount(newCash);

        yenCardManager.update(card, condition);
    }

    @Override
    public YenCard getDefault(long userId){
        checkArgument(userId > NumberUtils.LONG_ZERO);
        List<YenCard> cards = getCardByUser(userId);

        if (!CollectionUtils.isEmpty(cards)
                && cards.get(NumberUtils.INTEGER_ZERO).getType().equals(YenCardVO.Type.NORMAL.getCode())) {
            return cards.get(NumberUtils.INTEGER_ZERO);
        }

        logger.error(String.format("名下未找到瘾卡.userId[%d]", userId));
        throw new BizException("名下未找到瘾卡");
    }

    @Override
    public YenCard getSpecify(long userId, final long cardId){
        checkArgument(userId > NumberUtils.LONG_ZERO);
        checkArgument(cardId > NumberUtils.LONG_ZERO);
        List<YenCard> YenCards = getCardByUser(userId);

        for (YenCard card : YenCards) {
            if (card.getId().equals(cardId)) return card;
        }

        logger.error(String.format("名下未找到指定的瘾卡.userId[%d];cardId[%d]", userId, cardId));
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

    @Override
    public List<YenCard> getCardByUser(long userId) {
        checkArgument(userId > NumberUtils.LONG_ZERO);
        Condition condition = new Condition(YenCard.class);
        condition.orderBy("type"); // 统一按卡类型排序

        condition.createCriteria().andCondition("user_id=", userId)
                .andCondition("status=", YenCardVO.Status.NORMAL.getCode());

        return yenCardManager.findByCondition(condition);
    }

    private YenCardVO render(Long cardId, Long rechargeId, Long couponId) {
        // 1. 找到用户的瘾卡
        User user = userService.getUserByWxUnionId();

        YenCard card;
        if (cardId != null && cardId > NumberUtils.LONG_ZERO) {
            card = getSpecify(user.getId(), cardId);
        } else {
            card = getDefault(user.getId());
        }

        YenCardVO cardVO = convert2CardVO(card);

        Context context = new Context();
        context.setUser(user);
        context.setCard(card);

        // 2. 找到充值模版
        List<CouponVO> templates = couponService.getCardRechargeTemplate(context, rechargeId);
        if (CollectionUtils.isEmpty(templates)) {
            logger.error("没有瘾卡充值模版");
            throw new BizException("没有瘾卡充值模版");
        }
        cardVO.setTemplates(templates);

        // 3. 找到充值礼券
        List<CouponVO> couponVOs = couponService.getCoupon4Recharge(user.getId(),
                context.getTemplate().getRulePrice(), couponId, context);
        cardVO.setCoupon(couponVOs);

        // 4. 充值按钮
        Button button = new Button();

        long realPay = context.getTemplate().getRulePrice();

        if (context.getCoupon() != null && context.getCoupon().getPrice() != null) {
            realPay = realPay - context.getCoupon().getPrice();
        }

        String price = MoneyUtils.format(2, realPay / 100);
        button.setTitle("支付（￥" + price + "）");
        cardVO.setButton(button);

        return cardVO;
    }

    public YenCardVO convert2CardVO(YenCard card) {
        if (card == null) return new YenCardVO();

        String gift = MoneyUtils.format(2, card.getGiftAccount() / 100f);
        String cash = MoneyUtils.format(2, card.getCashAccount() / 100f);
        String total = MoneyUtils.format(2, (card.getGiftAccount() + card.getCashAccount()) / 100f);

        String discount = String.format("消费立打%s折", MoneyUtils.format(1, card.getDiscountRate() / 10f));

        // 4. 充值按钮
        Button button = new Button();
        button.setTitle("充值");

        return new YenCardVO(card.getId(), null,
                gift, cash, total,
                discount, null, null, card.getType(), button);
    }

    private List<YenCardVO> convert2CardVO(List<YenCard> cards) {
        List<YenCardVO> cardVOs = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cards)) return cardVOs;

        for (YenCard card : cards) {
            cardVOs.add(convert2CardVO(card));
        }

        return cardVOs;
    }
}
