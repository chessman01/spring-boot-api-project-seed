package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.manager.FundDetailManager;
import com.tianbao.buy.service.FundDetailService;
import com.tianbao.buy.vo.FundDetailVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FundDetailServiceImpl implements FundDetailService {
    @Resource
    private FundDetailManager fundDetailManager;

    @Override
    public void updateStatus(Long orderId, FundDetailVO.Status status) {
        FundDetail fundDetail = new FundDetail();
        fundDetail.setStatus(status.getCode());

        Condition condition = new Condition(FundDetail.class);
        condition.createCriteria().andCondition("order_id=", orderId);

        fundDetailManager.update(fundDetail, condition);
    }

    @Override
    public void initFund4PerIn(Long orderId, Integer price4wx, Integer price4Card, Integer price4Coupon) {
        List<FundDetail> fundDetails = Lists.newArrayList();

        // 从瘾卡消费现金
        if (price4Card != null && price4Card > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.YENCARD, FundDetailVO.Channel.END, price4Card,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));
        }

        // 从微信消费现金
        fundDetails.add(convert(null, orderId, FundDetailVO.Channel.WEIXIN, FundDetailVO.Channel.END, price4wx,
                FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));

        // 从礼券消费现金
        if (price4Coupon != null && price4Coupon > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.COUPON, FundDetailVO.Channel.END, price4Coupon,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));
        }

        fundDetailManager.save(fundDetails);
    }

    @Override
    public void initFund4PerOut(Long orderId, Integer price4wx, Integer price4Card, Integer price4Coupon) {
        List<FundDetail> fundDetails = Lists.newArrayList();

        // 从瘾卡退款现金
        if (price4Card != null && price4Card > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.YENCARD, FundDetailVO.Channel.END, price4Card,
                    FundDetailVO.Direction.OUT, FundDetailVO.Status.PENDING));
        }

        // 从微信退款现金
        fundDetails.add(convert(null, orderId, FundDetailVO.Channel.WEIXIN, FundDetailVO.Channel.END, price4wx,
                FundDetailVO.Direction.OUT, FundDetailVO.Status.PENDING));

        // 从礼券退款现金
        if (price4Coupon != null && price4Coupon > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.COUPON, FundDetailVO.Channel.END, price4Coupon,
                    FundDetailVO.Direction.OUT, FundDetailVO.Status.PENDING));
        }

        fundDetailManager.save(fundDetails);
    }

    @Override
    public void initFund4RechargIn(Long orderId, Integer price4wx, Integer price4Gift, Integer price4Coupon) {
        List<FundDetail> fundDetails = Lists.newArrayList();

        // 瘾卡从微信进一笔现金；
        fundDetails.add(convert(null, orderId, FundDetailVO.Channel.WEIXIN, FundDetailVO.Channel.YENCARD, price4wx,
                FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));

        // 瘾卡从赠送进一笔现金；
        if (price4Gift != null && price4Gift > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.GIFT, FundDetailVO.Channel.YENCARD, price4Gift,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));
        }

        // 瘾卡从礼券进一笔现金；
        if (price4Coupon != null && price4Coupon > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.COUPON, FundDetailVO.Channel.YENCARD, price4Coupon,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));
        }

        fundDetailManager.save(fundDetails);
    }

    private FundDetail convert(Long id, Long orderId, FundDetailVO.Channel fromChannel, FundDetailVO.Channel toChannel,
                               Integer price, FundDetailVO.Direction direction, FundDetailVO.Status status) {
        FundDetail fundDetail = new FundDetail();

        fundDetail.setId(id);
        fundDetail.setOrderId(orderId);
        fundDetail.setPrice(price);
        fundDetail.setFromChannel(fromChannel.getCode());
        fundDetail.setToChannel(toChannel.getCode());
        fundDetail.setDirection(direction.getCode());
        fundDetail.setStatus(status.getCode());

        return fundDetail;
    }
}
