package com.tianbao.buy.service.impl;

import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.manager.FundDetailManager;
import com.tianbao.buy.vo.FundDetailVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FundDetailServiceImpl {
    @Resource
    private FundDetailManager fundDetailManager;

    // 微信：按次购买，按次退款，瘾卡充值，瘾卡退款

    public List<FundDetail> fund4recharg(Long userId, FundDetailVO.Target target, Long orginId,
                                  Integer price, String payOrderId) {
        // 微信进一笔；微信出一笔；瘾卡进一笔现金；瘾卡进一笔赠送；礼券进一笔；礼券出一笔
        FundDetail fundIn4Wx, fundOut4Wx, fundIn4Card;

        fundIn4Wx = convert(null, userId, FundDetailVO.Channel.WEIXIN, FundDetailVO.Target.CARD, orginId, price, payOrderId,  FundDetailVO.Direction.IN,
                null, FundDetailVO.AccountType.CASH, FundDetailVO.Status.PENDING);

        fundOut4Wx = convert(null, userId, FundDetailVO.Channel.WEIXIN, target, orginId, price, payOrderId,  FundDetailVO.Direction.IN,
                null, FundDetailVO.AccountType.CASH, FundDetailVO.Status.PENDING);

        fundIn4Card = convert(null, userId, FundDetailVO.Channel.WEIXIN, target, orginId, price, payOrderId,  FundDetailVO.Direction.IN,
                null, FundDetailVO.AccountType.CASH, FundDetailVO.Status.PENDING);

        return null;
    }

    public FundDetail generatePay(int cashAccount, int giftAccount, Long userId, FundDetailVO.Target target, Long orginId,
                            Integer price, String payOrderId) {





        return convert(null, userId, FundDetailVO.Channel.WEIXIN, target, orginId, price, payOrderId,  FundDetailVO.Direction.IN,
                null, FundDetailVO.AccountType.CASH, FundDetailVO.Status.PENDING);
    }

    private FundDetail convert(Long id, Long userId, FundDetailVO.Channel channel, FundDetailVO.Target target, Long orginId,
                               Integer price, String payOrderId, FundDetailVO.Direction direction, Integer rechargeCouponeId,
                               FundDetailVO.AccountType accountType, FundDetailVO.Status status) {
        FundDetail fundDetail = new FundDetail();

        fundDetail.setId(id);
        fundDetail.setUserId(userId);
        fundDetail.setChannel(channel.getCode());
        fundDetail.setTarget(target.getCode());
        fundDetail.setOrginId(orginId);
        fundDetail.setPrice(price);
        fundDetail.setPayOrderId(payOrderId);
        fundDetail.setDirection(direction.getCode());
        fundDetail.setRechargeCouponeId(rechargeCouponeId);
        fundDetail.setAccountType(accountType.getCode());
        fundDetail.setStatus(status.getCode());

        return fundDetail;
    }
}
