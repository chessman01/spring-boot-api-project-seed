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

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FundDetailServiceImpl implements FundDetailService {
    private static Logger logger = LoggerFactory.getLogger(FundDetailServiceImpl.class);

    @Resource
    private FundDetailManager fundDetailManager;

    public List<FundDetail> get(String orderId, FundDetailVO.Status originStatus){
        Condition condition = new Condition(FundDetail.class);
        condition.createCriteria().andEqualTo("status", originStatus.getCode()).andEqualTo("orderId", orderId);

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
    public void refundByPer(String orderId) {
        updateStatus(orderId, FundDetailVO.Status.FINISH, FundDetailVO.Status.BLOCKED);
    }

    @Override
    public List<FundDetail> incomeByPer(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx) {
        return init(orderId, fee4wx, payDetailMap, FundDetailVO.Direction.INCOME_PER);
    }

    @Override
    public List<FundDetail> incomeByRecharg(String orderId, Map<String, OrderVO.PayDetail> payDetailMap, Integer fee4wx) {
        return init(orderId, fee4wx, payDetailMap, FundDetailVO.Direction.INCOME_CARD);
    }

    @Override
    public int getFee(OrderVO.PayDetail payDetail) {
        return payDetail != null ? payDetail.getOriginFee() : NumberUtils.INTEGER_ZERO;
    }

    private List<FundDetail> init(String orderId, Integer fee4wx, Map<String, OrderVO.PayDetail> payDetailMap, FundDetailVO.Direction direction) {
        Integer fee4Card = getFee(payDetailMap.get(OrderService.CARD_PAY_FEE));
        Integer fee4Gift = getFee(payDetailMap.get(OrderService.GIFT_FEE));
        Integer fee4Coupon = getFee(payDetailMap.get(OrderService.COUPON_FEE));
        Integer onlineReduceFee = getFee(payDetailMap.get(OrderService.ONLINE_REDUCE));
        Integer cardDiscount = getFee(payDetailMap.get(OrderService.CARD_DISCOUNT));

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

        // 瘾卡折扣
        if (cardDiscount != null && cardDiscount > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(make(orderId, FundDetailVO.Channel.CARD_DISCOUNT, cardDiscount, direction, date));
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

        return fundDetails;
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
