package com.tianbao.buy.utils.redis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 注解式环境管理
 *
 * @create 2016年9月12日
 */
@Configuration
@EnableCaching
public class CachingConfig extends CachingConfigurerSupport {

    /**
     * 在使用@Cacheable时，如果不指定key，则使用找个默认的key生成器生成的key
     *
     * @return
     *
     * @author 单红宇(CSDN CATOOP)
     * @create 2017年3月11日
     */
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator() {

            /**
             * 对参数进行拼接后MD5
             */
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(".").append(method.getName());

                StringBuilder paramsSb = new StringBuilder();
                for (Object param : params) {
                    // 如果不指定，默认生成包含到键值中
                    if (param != null) {
                        paramsSb.append(param.toString());
                    }
                }

                if (paramsSb.length() > 0) {
                    sb.append("_").append(paramsSb);
                }
                return sb.toString();
            }

        };

    }

    /**
     * 管理缓存
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate, RedisKeys redisKeys) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        // 设置缓存默认过期时间（全局的）
        rcm.setDefaultExpiration(1800);// 30分钟

        // 根据key设定具体的缓存时间，key统一放在常量类RedisKeys中
        rcm.setExpires(redisKeys.getExpiresMap());

        List<String> cacheNames = new ArrayList<String>(redisKeys.getExpiresMap().keySet());
        rcm.setCacheNames(cacheNames);

        return rcm;
    }

}
