package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.TagDAO;
import com.tianbao.buy.domain.Tag;
import com.tianbao.buy.manager.TagManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/08/31.
 */
@Service
public class TagManagerImpl extends AbstractManager<Tag> implements TagManager {
    @Resource
    private TagDAO tagDAO;

}
