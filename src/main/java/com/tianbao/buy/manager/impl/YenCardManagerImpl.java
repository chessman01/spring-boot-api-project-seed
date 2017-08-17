package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.YenCardDAO;
import com.tianbao.buy.domain.YenCard;
import com.tianbao.buy.manager.YenCardManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/08/17.
 */
@Service
public class YenCardManagerImpl extends AbstractManager<YenCard> implements YenCardManager {
    @Resource
    private YenCardDAO yenCardDAO;

}
