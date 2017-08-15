package com.tianbao.buy.service.impl;

import com.tianbao.buy.dao.YenCareMapper;
import com.tianbao.buy.model.YenCare;
import com.tianbao.buy.service.YenCareService;
import com.tianbao.buy.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2017/08/15.
 */
@Service
@Transactional
public class YenCareServiceImpl extends AbstractService<YenCare> implements YenCareService {
    @Resource
    private YenCareMapper yenCareMapper;

}
