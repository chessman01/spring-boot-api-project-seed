package ${basePackage}.manager.impl;

import ${basePackage}.dao.${modelNameUpperCamel}DAO;
import ${basePackage}.domain.${modelNameUpperCamel};
import ${basePackage}.manager.${modelNameUpperCamel}Manager;
import ${basePackage}.core.AbstractManager;
import org.springframework.stereotype.Service;
<#--import org.springframework.transaction.annotation.Transactional;-->

import javax.annotation.Resource;

/**
 * Created by ${author} on ${date}.
 */
@Service
<#--@Transactional 在service类中做即可-->
public class ${modelNameUpperCamel}ManagerImpl extends AbstractManager<${modelNameUpperCamel}> implements ${modelNameUpperCamel}Manager {
    @Resource
    private ${modelNameUpperCamel}DAO ${modelNameLowerCamel}DAO;

}
