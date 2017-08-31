package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FundDetailServiceImpl implements FundDetailService {
    private static Logger logger = LoggerFactory.getLogger(FundDetailServiceImpl.class);

    @Resource
    private FundDetailManager fundDetailManager;

//    @Override
//    public Map<String, OrderVO.PayDetail> toMap(List<OrderVO.PayDetail> payDetails) {
//        Map<String, OrderVO.PayDetail> payDetailMap = Maps.newHashMap();
//
//        for (OrderVO.PayDetail payDetail : payDetails) {
//            payDetailMap.put(payDetail.getTitle(), payDetail);
//        }
//        return payDetailMap;
//    }

    @Override
    public void updateStatus(String orderId, FundDetailVO.Status status) {
        FundDetail fundDetail = new FundDetail();
        fundDetail.setStatus(status.getCode());

        Condition condition = new Condition(FundDetail.class);
        condition.createCriteria().andCondition("order_id=", orderId);

        fundDetailManager.update(fundDetail, condition);
    }

    @Override
    public void refundByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx) {
        init(orderId, fee4wx, payDetailMap, FundDetailVO.Direction.REFUND_PER);
    }

    @Override
    public void incomeByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx) {
        init(orderId, fee4wx, payDetailMap, FundDetailVO.Direction.INCOME_PER);
    }

    @Override
    public void refundByRecharg(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx) {
        init(orderId, fee4wx, payDetailMap, FundDetailVO.Direction.REFUND_CARD);
    }

    @Override
    public void incomeByRecharg(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx) {
        init(orderId, fee4wx, payDetailMap, FundDetailVO.Direction.INCOME_CARD);
    }

    @Override
    public int getFee(OrderVO.PayDetail payDetail) {
        return payDetail != null ? payDetail.getOriginFee() : NumberUtils.INTEGER_ZERO;
    }
    private void init(String orderId, Integer fee4wx, Map<String, OrderVO.PayDetail> payDetailMap, FundDetailVO.Direction direction) {
        Integer fee4Card = getFee(payDetailMap.get(OrderService.CARD_PAY_FEE));
        Integer fee4Gift = getFee(payDetailMap.get(OrderService.GIFT_FEE));
        Integer fee4Coupon = getFee(payDetailMap.get(OrderService.COUPON_FEE));
        Integer onlineReduceFee = getFee(payDetailMap.get(OrderService.ONLINE_REDUCE));

        List<FundDetail> fundDetails = Lists.newArrayList();
        Date date = new Date();

        // 微信
        if (fee4wx != null && fee4wx > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.WEIXIN, fee4wx, direction, date));
        }

        // 赠送
        if (fee4Gift != null && fee4Gift > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.GIFT, fee4Gift, direction, date));
        }

        // 立减
        if (onlineReduceFee != null && onlineReduceFee > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.REDUCE, onlineReduceFee, direction, date));
        }

        // 礼券
        if (fee4Coupon != null && fee4Coupon > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.COUPON, fee4Coupon, direction, date));
        }

        // 瘾卡
        if (fee4Card != null && fee4Card > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.YENCARD, fee4Card, direction, date));
        }

        FundDetailVO.Channel origin = null, target = null;

        switch(direction) {
            case INCOME_CARD:
                target = FundDetailVO.Channel.YENCARD;
                break;
            case REFUND_CARD:
                origin = FundDetailVO.Channel.YENCARD;
                break;
            case INCOME_PER:
                target = FundDetailVO.Channel.END;
                break;
            case REFUND_PER:
                origin = FundDetailVO.Channel.END;
                break;
        }

        for (FundDetail fundDetail : fundDetails) {
            if (origin != null) {
                fundDetail.setOrigin(origin.getCode());
            }

            if (target != null) {
                fundDetail.setTarget(target.getCode());
            }
        }

        fundDetailManager.save(fundDetails);
    }


    private FundDetail make(String orderId, FundDetailVO.Channel channel, Integer price, FundDetailVO.Direction direction, Date date) {
        FundDetail fundDetail = new FundDetail();

        fundDetail.setCreateTime(date);
        fundDetail.setModifyTime(date);
        fundDetail.setOrderId(orderId);
        fundDetail.setPrice(price);
        fundDetail.setOrigin(channel.getCode());
        fundDetail.setTarget(channel.getCode());
        fundDetail.setDirection(direction.getCode());
        fundDetail.setStatus(FundDetailVO.Status.PENDING.getCode());

        return fundDetail;
    }
}
