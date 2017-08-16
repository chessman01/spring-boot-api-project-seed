package com.tianbao.buy.service;


import com.tianbao.buy.vo.YenCareVO;

import java.util.List;

public interface YenCareService {
    List<YenCareVO> getAllCareByUser();

    YenCareVO build(long cardId);

    String create(long cardId, long rechargeId, long couponId);

    YenCareVO adjust(long cardId, long rechargeId, long couponId);
}
