package com.tianbao.buy.core;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Manager 层 基础接口，其他Manager 接口 请继承该接口
 */
public interface Manager<T> {
    int save(T model);//持久化
    int save(List<T> models);//批量持久化
    int deleteById(Long id);//通过主鍵刪除
    int deleteByIds(String ids);//批量刪除 eg：ids -> “1,2,3,4”
    int update(T model);//更新
    int update(T model, Condition condition);
    T findById(Long id);//通过ID查找
    T findBy(String fieldName, Object value) throws TooManyResultsException; //通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
    List<T> findByIds(String ids);//通过多个ID查找//eg：ids -> “1,2,3,4”
    List<T> findByCondition(Condition condition);//根据条件查找
    List<T> findAll();//获取所有
}
