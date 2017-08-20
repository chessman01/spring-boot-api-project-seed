package com.tianbao.buy.service;

import com.tianbao.buy.domain.YenCard;
import com.tianbao.buy.vo.YenCardVO;

import java.util.List;

public interface YenCardService {
    List<YenCard> getCardByUser(long userId);

    YenCard getDefault(long userId);

    YenCard getSpecify(long userId, final long cardId);

    void initNormalCard(long userId);

    void updatePrice(int newCash, int oldCash, int newGift, int oldGift, long id);

    List<YenCardVO> getCardByUser();

    YenCardVO build(long cardId);

    String create(long cardId, long rechargeId, long couponUserId);

    YenCardVO adjust(long cardId, long rechargeId, long couponId);
}
