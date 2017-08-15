package com.tianbao.buy.manager.impl;

import com.tianbao.buy.dao.YenCareDAO;
import com.tianbao.buy.domain.YenCare;
import com.tianbao.buy.manager.YenCareManager;
import com.tianbao.buy.core.AbstractManager;
import org.springframework.stereotype.Service;
dd
import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
public class YenCareManagerImpl extends AbstractManager<YenCare> implements YenCareManager {
    @Resource
    private YenCareDAO yenCareDAO;

}
