package com.tianbao.buy.utils.redis;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * 方法缓存key常量
 *
 */
@Component
public class RedisKeys {

    // 测试 begin
    public static final String _CACHE_TEST = "_cache_test";// 缓存key
    public static final Long _CACHE_TEST_SECOND = 20L;// 缓存时间
    // 测试 end

    // 根据key设定具体的缓存时间
    private Map<String, Long> expiresMap = null;

    @PostConstruct
    public void init(){
        expiresMap = new HashMap<>();
        expiresMap.put(_CACHE_TEST, _CACHE_TEST_SECOND);
    }

    public Map<String, Long> getExpiresMap(){
        return this.expiresMap;
    }
}
