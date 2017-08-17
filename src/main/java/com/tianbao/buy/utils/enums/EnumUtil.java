package com.tianbao.buy.utils.enums;

import org.springframework.util.CollectionUtils;

public class EnumUtil {
    /**
     * 获取value返回枚举对象
     * @param value
     * @param clazz
     * */
    public static <T extends EnumMessage>  T getEnumObject(Object value, Class<T> clazz){
        if (CollectionUtils.isEmpty(Constant.ENUM_MAP)) return null;
        if (Constant.ENUM_MAP.get(clazz) == null) return null;


        return (T) Constant.ENUM_MAP.get(clazz).get(value);
    }
}