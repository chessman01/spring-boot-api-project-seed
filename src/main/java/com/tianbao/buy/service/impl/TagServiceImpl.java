package com.tianbao.buy.service.impl;

import com.google.common.collect.Lists;
import com.tianbao.buy.domain.Tag;
import com.tianbao.buy.manager.TagManager;
import com.tianbao.buy.vo.TagVO;
import org.springframework.beans.BeanUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * 暂不用
 */
public class TagServiceImpl {
    @Resource
    private TagManager tagManager;

    /** 获取到课程相关的所有tag 改了方案，暂不用了，搞太复杂了**/
    private List<TagVO> getAllTag4Course() {
        Condition condition = new Condition(Tag.class);

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
