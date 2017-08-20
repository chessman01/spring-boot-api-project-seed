package com.tianbao.buy.service;

import com.tianbao.buy.vo.CoachVO;

public interface CoachService {
    CoachVO getCoach(long id, boolean needDesc);
}
