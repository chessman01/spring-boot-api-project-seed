package com.tianbao.buy.service;

import com.tianbao.buy.domain.FundDetail;
import com.tianbao.buy.domain.User;
import com.tianbao.buy.domain.YenCard;
import com.tianbao.buy.vo.OrderVO;
import com.tianbao.buy.vo.YenCardVO;

import java.util.List;

public interface YenCardService {
    List<YenCardVO> getCardByUser(User user);

    YenCard getDefault(long userId);

    YenCard getSpecify(long userId, final long cardId);

    void initNormalCard(long userId);

    void updatePrice(int newCash, int oldCash, int newGift, int oldGift, long id);

    List<YenCard> getCardByUser(long userId);

    YenCardVO build(Long cardId, User user);

    String create(long cardId, long templateId, Long couponUserId, User user);

    YenCardVO adjust(long cardId, long templateId, Long couponId, User user);

    YenCardVO convert2CardVO(YenCard YenCard);

    List<OrderVO.PayDetail> getPayDetail(List<FundDetail> fundDetails);
}
