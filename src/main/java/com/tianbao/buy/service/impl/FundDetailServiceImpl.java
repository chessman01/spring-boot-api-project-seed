package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.manager.FundDetailManager;
import com.tianbao.buy.vo.FundDetailVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FundDetailServiceImpl {
    @Resource
    private FundDetailManager fundDetailManager;

    // 微信：按次购买，按次退款，瘾卡充值，瘾卡退款

    public List<FundDetail> fund4RechargIn(Long orderId, Integer price4wx, Integer price4Gift, Integer price4Coupon) {
        // 瘾卡进一笔赠送；礼券进一笔；礼券出一笔
        List<FundDetail> fundDetails = Lists.newArrayList();

        // 微信进一笔；微信出一笔；瘾卡进一笔现金；
        fundDetails.add(convert(null, orderId, FundDetailVO.Channel.WEIXIN, price4wx,
                FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));

        fundDetails.add(convert(null, orderId, FundDetailVO.Channel.WEIXIN, price4wx,
                FundDetailVO.Direction.OUT, FundDetailVO.Status.PENDING));

        fundDetails.add(convert(null, orderId, FundDetailVO.Channel.YENCARD, price4wx,
                FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));

        if (price4Gift != null && price4Gift > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.GIFT, price4Gift,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));

            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.GIFT, price4Gift,
                    FundDetailVO.Direction.OUT, FundDetailVO.Status.PENDING));

            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.YENCARD, price4Gift,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));
        }

        if (price4Coupon != null && price4Coupon > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.COUPON, price4Coupon,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));

            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.COUPON, price4Coupon,
                    FundDetailVO.Direction.OUT, FundDetailVO.Status.PENDING));

            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.YENCARD, price4Coupon,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING));
        }

        return fundDetails;
    }

    private FundDetail convert(Long id, Long orderId, FundDetailVO.Channel channel, Integer price,
                               FundDetailVO.Direction direction, FundDetailVO.Status status) {
        FundDetail fundDetail = new FundDetail();

        fundDetail.setId(id);
        fundDetail.setOrderId(orderId);
        fundDetail.setPrice(price);
        fundDetail.setChannel(channel.getCode());
        fundDetail.setDirection(direction.getCode());
        fundDetail.setStatus(status.getCode());

        return fundDetail;
    }
}
