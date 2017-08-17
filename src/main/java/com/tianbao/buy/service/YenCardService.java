package com.tianbao.buy.service;

import com.tianbao.buy.domain.YenCard;
import com.tianbao.buy.vo.YenCardVO;

import java.util.List;

public interface YenCardService {
    List<YenCardVO> getAllByUser();

    List<YenCard> getCardByUser(long userId);

    YenCardVO build(long cardId);

    String create(long cardId, long rechargeId, long couponId);

    YenCardVO adjust(long cardId, long rechargeId, long couponId);
}
