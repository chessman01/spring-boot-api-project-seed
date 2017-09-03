package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.manager.FundDetailManager;
import com.tianbao.buy.service.FundDetailService;
import com.tianbao.buy.service.OrderService;
import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FundDetailServiceImpl implements FundDetailService {
    private static Logger logger = LoggerFactory.getLogger(FundDetailServiceImpl.class);

    @Resource
    private FundDetailManager fundDetailManager;

    @Override
    public List<FundDetail> get(String orderId, FundDetailVO.Status originStatus){
        Condition condition = new Condition(FundDetail.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("orderId", orderId);

        if (originStatus != null) criteria.andEqualTo("status", originStatus.getCode());

        return fundDetailManager.findByCondition(condition);
    }

    @Override
    public void updateStatus(String orderId, FundDetailVO.Status originStatus, FundDetailVO.Status status) {
        FundDetail fundDetail = new FundDetail();
        fundDetail.setStatus(status.getCode());

        Condition condition = new Condition(FundDetail.class);
        condition.createCriteria().andEqualTo("status", originStatus.getCode()).andEqualTo("orderId", orderId);

        fundDetailManager.update(fundDetail, condition);
    }

    @Override
    public List<FundDetail> refundByPer(String orderId, List<FundDetail> fundDetails) {
        Date current = new Date();
        for (FundDetail fundDetail : fundDetails) {
            fundDetail.setTarget(fundDetail.getOrigin());
            fundDetail.setOrigin(FundDetailVO.Channel.END.getCode());
            fundDetail.setCreateTime(current);
            fundDetail.setModifyTime(current);
            fundDetail.setOrderId(orderId);
            fundDetail.setStatus(FundDetailVO.Status.PENDING.getCode());
            fundDetail.setDirection(FundDetailVO.Direction.REFUND_PER.getCode());
        }

        fundDetailManager.save(fundDetails);
        return fundDetails;
    }

    @Override
    public List<FundDetail> incomeByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx) {
        return init(orderId, fee4wx, payDetailMap, FundDetailVO.Direction.INCOME_PER);
    }

    @Override
    public List<FundDetail> incomeByRecharge(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx) {
        return init(orderId, fee4wx, payDetailMap, FundDetailVO.Direction.INCOME_CARD);
    }

    @Override
    public int getRealPayFee(List<FundDetail> details) {
        for (FundDetail detail : details) {
            if (detail.getOrigin().equals(FundDetailVO.Channel.WEIXIN.getCode())) {
                return detail.getPrice();
            }
        }

        return NumberUtils.INTEGER_ZERO;
    }

    @Override
    public int getCardFee(List<FundDetail> details, boolean isCash, boolean isRecharge) {
        int fee = 0;

        for (FundDetail detail : details) {
            if (isCash && isRecharge && detail.getOrigin().equals(FundDetailVO.Channel.WEIXIN.getCode())) {
                return detail.getPrice();
            }

            if (!isCash && isRecharge && !detail.getOrigin().equals(FundDetailVO.Channel.WEIXIN.getCode())) {
                fee = fee + detail.getPrice();
            }

            if (isCash && !isRecharge && detail.getOrigin().equals(FundDetailVO.Channel.CARD_CASH.getCode())) {
                return detail.getPrice();
            }

            if (!isCash && !isRecharge && detail.getOrigin().equals(FundDetailVO.Channel.CARD_GIFT.getCode())) {
                fee = fee + detail.getPrice();
            }
        }

        return fee;
    }

    @Override
    public int getFee(OrderVO.PayDetail payDetail) {
        return payDetail != null ? payDetail.getOriginFee() : NumberUtils.INTEGER_ZERO;
    }

    private boolean getFee(Integer fee) {
        return fee != null && fee > NumberUtils.INTEGER_ZERO;
    }

    private FundDetail setChannel(FundDetailVO.Direction direction, FundDetailVO.Channel channel) {
        FundDetail fundDetail = new FundDetail();

        if (FundDetailVO.Direction.INCOME_CARD.equals(direction)) {
            if (FundDetailVO.Channel.WEIXIN.equals(channel)) {
                fundDetail.setTarget(FundDetailVO.Channel.CARD_CASH.getCode());
            } else {
                fundDetail.setTarget(FundDetailVO.Channel.CARD_GIFT.getCode());
            }

            fundDetail.setOrigin(channel.getCode());
        }

        if (FundDetailVO.Direction.REFUND_CARD.equals(direction)) {
            if (FundDetailVO.Channel.WEIXIN.equals(channel)) {
                fundDetail.setOrigin(FundDetailVO.Channel.CARD_CASH.getCode());
            } else {
                fundDetail.setOrigin(FundDetailVO.Channel.CARD_GIFT.getCode());
            }

            fundDetail.setTarget(channel.getCode());
        }

        if (FundDetailVO.Direction.INCOME_PER.equals(direction)) {
            fundDetail.setTarget(FundDetailVO.Channel.END.getCode());
            fundDetail.setOrigin(channel.getCode());
        }

        if (FundDetailVO.Direction.REFUND_PER.equals(direction)) {
            fundDetail.setOrigin(FundDetailVO.Channel.END.getCode());
            fundDetail.setTarget(channel.getCode());
        }

        return fundDetail;
    }

    private List<FundDetail> init(String orderId, Integer fee4wx, Map<String, OrderVO.PayDetail> payDetailMap, FundDetailVO.Direction direction) {
        Integer fee4CardCash = getFee(payDetailMap.get(OrderService.CARD_CASH_PAY_FEE));
        Integer fee4CardGift = getFee(payDetailMap.get(OrderService.CARD_GIFT_PAY_FEE));
        Integer fee4Gift = getFee(payDetailMap.get(OrderService.GIFT_FEE));
        Integer fee4Coupon = getFee(payDetailMap.get(OrderService.COUPON_FEE));
        Integer onlineReduceFee = getFee(payDetailMap.get(OrderService.ONLINE_REDUCE));
        Integer cardDiscount = getFee(payDetailMap.get(OrderService.CARD_DISCOUNT));

        List<FundDetail> fundDetails = Lists.newArrayList();
        Date date = new Date();

        // 微信
        if (getFee(fee4wx)) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.WEIXIN, fee4wx, direction, date));
        }

        // 赠送
        if (getFee(fee4Gift)) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.GIFT, fee4Gift, direction, date));
        }

        // 立减
        if (getFee(onlineReduceFee)) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.REDUCE, onlineReduceFee, direction, date));
        }

        // 礼券
        if (getFee(fee4Coupon)) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.COUPON, fee4Coupon, direction, date));
        }

        // 瘾卡
        if (getFee(fee4CardCash)) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.CARD_CASH, fee4CardCash, direction, date));
        }

        // 瘾卡
        if (getFee(fee4CardGift)) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.CARD_GIFT, fee4CardGift, direction, date));
        }

        // 瘾卡折扣
        if (getFee(cardDiscount)) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.CARD_DISCOUNT, cardDiscount, direction, date));
        }

        fundDetailManager.save(fundDetails);
        return fundDetails;
    }

    private FundDetail make(String orderId, FundDetailVO.Channel channel, Integer price,
                            FundDetailVO.Direction direction, Date date) {
        FundDetail fundDetail = setChannel(direction, channel);

        fundDetail.setCreateTime(date);
        fundDetail.setModifyTime(date);
        fundDetail.setOrderId(orderId);
        fundDetail.setPrice(price);
        fundDetail.setDirection(direction.getCode());
        fundDetail.setStatus(FundDetailVO.Status.PENDING.getCode());

        return fundDetail;
    }
}
