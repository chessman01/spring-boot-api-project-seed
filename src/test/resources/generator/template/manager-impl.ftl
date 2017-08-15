package ${basePackage}.manager.impl;

import ${basePackage}.dao.${modelNameUpperCamel}Mapper;
import ${basePackage}.domain.${modelNameUpperCamel};
import ${basePackage}.manager.${modelNameUpperCamel}Manager;
import ${basePackage}.core.AbstractManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by ${author} on ${date}.
 */
@Service
@Transactional
public class ${modelNameUpperCamel}ManagerImpl extends AbstractManager<${modelNameUpperCamel}> implements ${modelNameUpperCamel}Manager {
    @Resource
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

}
