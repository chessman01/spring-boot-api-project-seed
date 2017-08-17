package com.tianbao.buy.service.impl;

import com.google.common.collect.Maps;
import com.tianbao.buy.domain.Coach;
import com.tianbao.buy.manager.CoachManager;
import com.tianbao.buy.service.CoachService;
import com.tianbao.buy.vo.CoachVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CoachServiceImpl implements CoachService {
    @Resource
    private CoachManager coachManager;

    /** 获取到所有教练 **/
    private Map<Long, CoachVO> getAllCoach() {
        Condition condition = new Condition(Coach.class);

        condition.createCriteria().andCondition("status=", CoachVO.Status.NORMAL.getCode());

        List<Coach> coaches = coachManager.findByCondition(condition);
        Map<Long, CoachVO> coachVOs = Maps.newConcurrentMap();

        coaches.stream().forEach(coach -> {
            CoachVO coachVO = new CoachVO();
            BeanUtils.copyProperties(coach, coachVO);
            coachVOs.put(coachVO.getId(), coachVO);
        });

        return coachVOs;
    }
}
