package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.TagMapper;
import com.tianbao.buy.model.Tag;
import com.tianbao.buy.service.TagService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class TagServiceImpl extends AbstractService<Tag> implements TagService {
    @Resource
    private TagMapper tagMapper;

}
