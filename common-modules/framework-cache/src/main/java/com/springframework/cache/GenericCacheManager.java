package com.springframework.cache;


import com.springframework.enums.rediskey.CacheNamePrefixEnum;

/**
 * @author summer
 */
public interface GenericCacheManager extends CacheTools {

    public static long DEFAULT_CACHE_TIME = 1000 * 60 * 5L;
    public static long EXPIRE_TIME = 0L;
    public static long DEFAULT_CACHE_TIME_ONE_DATE = 1000 * 60 * 60 * 24L;

    /**
     * 子类实现该方法注册缓存 prefix
     */
    public CacheNamePrefixEnum registerCacheName();
}
