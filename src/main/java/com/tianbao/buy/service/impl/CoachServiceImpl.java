package com.tianbao.buy.service.impl;

import com.google.common.collect.Maps;
import com.tianbao.buy.domain.Coach;
import com.tianbao.buy.manager.CoachManager;
import com.tianbao.buy.service.CoachService;
import com.tianbao.buy.vo.CoachVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class CoachServiceImpl implements CoachService {
    private static Logger logger = LoggerFactory.getLogger(CoachServiceImpl.class);

    @Resource
    private CoachManager coachManager;

    @Override
    public CoachVO getCoach(long id) {
        checkArgument(id > NumberUtils.LONG_ZERO, "id must great than 0.");
        Map<Long, CoachVO> coachVOMap = getAllCoach();

        CoachVO coachVO = coachVOMap.get(id);

        if (coachVO == null) logger.error(String.format("get coach null.coachId[%d]", id));

        return coachVO;
    }

    /** 获取到所有教练 **/
    private Map<Long, CoachVO> getAllCoach() {
        Condition condition = new Condition(Coach.class);

        condition.createCriteria().andCondition("status=", CoachVO.Status.NORMAL.getCode());
        List<Coach> coaches = coachManager.findByCondition(condition);

        Map<Long, CoachVO> coachMap = Maps.newConcurrentMap();
        if (CollectionUtils.isEmpty(coaches)) return coachMap;

        coaches.stream().forEach(coach -> {
            CoachVO coachVO = new CoachVO();
            BeanUtils.copyProperties(coach, coachVO);
            coachVO.setDesc(coach.getDescription());
            coachMap.put(coachVO.getId(), coachVO);
        });

        return coachMap;
    }
}
