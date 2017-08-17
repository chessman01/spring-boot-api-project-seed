package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.domain.Tag;
import com.tianbao.buy.manager.CourseManager;
import com.tianbao.buy.manager.TagManager;
import com.tianbao.buy.service.CourseService;
import com.tianbao.buy.vo.TagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    private TagManager tagManager;

    @Resource
    private CourseManager courseManager;

//    public

    /** 获取到课程相关的所有tag **/
    private List<TagVO> get4Course() {
        Condition condition = new Condition(Tag.class);
        condition.orderBy("type"); // 统一按卡类型排序

        condition.createCriteria().andCondition("status=", TagVO.Status.NORMAL.getCode())
                .andIn("type", Lists.newArrayList(TagVO.Type.NORMAL.getCode(), TagVO.Type.COURSE.getCode()));

        List<Tag> tags = tagManager.findByCondition(condition);
        List<TagVO> tagVOs = Lists.newArrayList();

        tags.stream().forEach(tag -> {
            TagVO tagVO = new TagVO();
            BeanUtils.copyProperties(tag, tagVO);
            tagVOs.add(tagVO);
        });

        return tagVOs;
    }
}
