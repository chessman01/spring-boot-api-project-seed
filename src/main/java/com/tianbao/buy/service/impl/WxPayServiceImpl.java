package com.tianbao.buy.service.impl;

import com.tianbao.buy.core.BizException;
import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.domain.OrderMain;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.domain.YenCard;
import com.tianbao.buy.manager.FundDetailManager;
import com.tianbao.buy.manager.OrderMainManager;
import com.tianbao.buy.service.UserService;
import com.tianbao.buy.service.WxPayService;
import com.tianbao.buy.service.YenCardService;
import com.tianbao.buy.vo.FundDetailVO;
import com.tianbao.buy.vo.OrderVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class WxPayServiceImpl implements WxPayService {
    @Resource
    YenCardService yenCardService;

    @Resource
    OrderMainManager orderMainManager;

    @Resource
    FundDetailManager fundDetailManager;

    private void getCash(List<FundDetail> details, Integer cash, Integer gift, Integer oldCash, Integer oldGift) {
        for (FundDetail detail : details) {
            if (detail.getOrigin().equals(FundDetailVO.Channel.WEIXIN.getCode())) {
                cash = detail.getPrice();
            } else {
                gift = gift + detail.getPrice();
            }
        }

        cash = oldCash + cash;
        gift = oldGift + gift;
    }

    public void paySuccess(String orderId, FundDetailVO.Direction direction) {
        List<FundDetail> fundDetails;
        Integer newCash = 0, newGift = 0, oldGift, oldCash;
        OrderMain orderMain = this.updateOrder(orderId, OrderVO.Status.PENDING_PAY);

        if (direction.equals(FundDetailVO.Direction.INCOME_CARD)) {
            fundDetails = this.updateFund(orderId);

            YenCard card = yenCardService.getSpecify(orderMain.getUserId(), orderMain.getYenCarId());
            oldCash = card.getCashAccount();
            oldGift = card.getGiftAccount();

            getCash(fundDetails, newCash, newGift, oldCash, oldGift);
            yenCardService.updatePrice(newCash, oldCash, newGift, oldGift, card.getId());
        }
    }

    private OrderMain updateOrder(String orderId, OrderVO.Status originStatus) {
        // 先找原始订单
        Condition condition = new Condition(OrderMain.class);
        condition.createCriteria().andEqualTo("status", originStatus.getCode()).andCondition("order_id=", orderId);

        List<OrderMain> orderMains = orderMainManager.findByCondition(condition);

        if (orderMains == null || orderMains.size() != NumberUtils.INTEGER_ONE) throw new BizException(String.format("没找到orderId[%s]的订单", orderId));
        OrderMain origin = orderMains.get(NumberUtils.INTEGER_ONE);

        OrderMain order = new OrderMain();

        if (origin.getType().equals(OrderVO.Type.CARD.getCode())) {
            order.setStatus(OrderVO.Status.END.getCode());
        } else {
            if (originStatus.equals(OrderVO.Status.PENDING_PAY)) {
                order.setStatus(OrderVO.Status.ORDER.getCode());
            }

            if (originStatus.equals(OrderVO.Status.PENDING_CANCLE)) {
                order.setStatus(OrderVO.Status.CANCLED.getCode());
            }
        }

        orderMainManager.update(order, condition);
        return origin;
    }

    private List<FundDetail> updateFund(String orderId) {
        // 先找到订单
        Condition condition = new Condition(FundDetail.class);
        condition.createCriteria().andEqualTo("status", FundDetailVO.Status.PENDING.getCode()).andCondition("order_id=", orderId);

        List<FundDetail> fundDetails = fundDetailManager.findByCondition(condition);

        if (CollectionUtils.isEmpty(fundDetails)) throw new BizException(String.format("没找到orderId[%s]的资金流水", orderId));

        FundDetail fundDetail = new FundDetail();
        fundDetail.setStatus(FundDetailVO.Status.FINISH.getCode());
        fundDetailManager.update(fundDetail, condition);

        return fundDetails;
    }

}
