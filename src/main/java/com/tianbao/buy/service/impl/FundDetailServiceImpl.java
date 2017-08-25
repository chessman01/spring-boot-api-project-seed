package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.manager.FundDetailManager;
import com.tianbao.buy.service.FundDetailService;
import com.tianbao.buy.vo.FundDetailVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class FundDetailServiceImpl implements FundDetailService {
    private static Logger logger = LoggerFactory.getLogger(FundDetailServiceImpl.class);

    @Resource
    private FundDetailManager fundDetailManager;

    @Override
    public void updateStatus(String orderId, FundDetailVO.Status status) {
        FundDetail fundDetail = new FundDetail();
        fundDetail.setStatus(status.getCode());

        Condition condition = new Condition(FundDetail.class);
        condition.createCriteria().andCondition("order_id=", orderId);

        fundDetailManager.update(fundDetail, condition);
    }

    @Override
    public void initFund4PerIn(String orderId, Integer price4wx, Integer price4Card, Integer price4Coupon, Date date) {
        List<FundDetail> fundDetails = Lists.newArrayList();

        // 从瘾卡消费现金
        if (price4Card != null && price4Card > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.YENCARD, FundDetailVO.Channel.END, price4Card,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING, date));
        }

        // 从微信消费现金
        fundDetails.add(convert(null, orderId, FundDetailVO.Channel.WEIXIN, FundDetailVO.Channel.END, price4wx,
                FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING, date));

        // 从礼券消费现金
        if (price4Coupon != null && price4Coupon > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.COUPON, FundDetailVO.Channel.END, price4Coupon,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING, date));
        }

        fundDetailManager.save(fundDetails);
    }

    @Override
    public void initFund4PerOut(String orderId, Integer price4wx, Integer price4Card, Integer price4Coupon, Date date) {
        List<FundDetail> fundDetails = Lists.newArrayList();

        // 从瘾卡退款现金
        if (price4Card != null && price4Card > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.YENCARD, FundDetailVO.Channel.END, price4Card,
                    FundDetailVO.Direction.OUT, FundDetailVO.Status.PENDING, date));
        }

        // 从微信退款现金
        fundDetails.add(convert(null, orderId, FundDetailVO.Channel.WEIXIN, FundDetailVO.Channel.END, price4wx,
                FundDetailVO.Direction.OUT, FundDetailVO.Status.PENDING, date));

        // 从礼券退款现金
        if (price4Coupon != null && price4Coupon > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.COUPON, FundDetailVO.Channel.END, price4Coupon,
                    FundDetailVO.Direction.OUT, FundDetailVO.Status.PENDING, date));
        }

        fundDetailManager.save(fundDetails);
    }

    @Override
    public void initFund4RechargIn(String orderId, Integer price4wx, Integer price4Gift, Integer price4Coupon, Date date) {
        List<FundDetail> fundDetails = Lists.newArrayList();

        // 瘾卡从微信进一笔现金；
        fundDetails.add(convert(null, orderId, FundDetailVO.Channel.WEIXIN, FundDetailVO.Channel.YENCARD, price4wx,
                FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING, date));

        // 瘾卡从赠送进一笔现金；
        if (price4Gift != null && price4Gift > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.GIFT, FundDetailVO.Channel.YENCARD, price4Gift,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING, date));
        }

        // 瘾卡从礼券进一笔现金；
        if (price4Coupon != null && price4Coupon > NumberUtils.INTEGER_ZERO) {
            fundDetails.add(convert(null, orderId, FundDetailVO.Channel.COUPON, FundDetailVO.Channel.YENCARD, price4Coupon,
                    FundDetailVO.Direction.IN, FundDetailVO.Status.PENDING, date));
        }

        fundDetailManager.save(fundDetails);
    }

    private FundDetail convert(Long id, String orderId, FundDetailVO.Channel fromChannel, FundDetailVO.Channel toChannel,
                               Integer price, FundDetailVO.Direction direction, FundDetailVO.Status status, Date date) {
        FundDetail fundDetail = new FundDetail();

        fundDetail.setId(id);
        fundDetail.setOrderId(orderId);
        fundDetail.setPrice(price);
        fundDetail.setFromChannel(fromChannel.getCode());
        fundDetail.setToChannel(toChannel.getCode());
        fundDetail.setDirection(direction.getCode());
        fundDetail.setStatus(status.getCode());
        fundDetail.setCreateTime(date);
        fundDetail.setModifyTime(date);

        return fundDetail;
    }
}
