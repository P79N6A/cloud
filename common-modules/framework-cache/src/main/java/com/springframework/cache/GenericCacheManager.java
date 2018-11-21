package com.springframework.cache;


import com.springframework.enums.rediskey.CacheNamePrefixEnum;

/** @author summer */
public interface GenericCacheManager extends CacheTools {

    /** 子类实现该方法注册缓存 prefix
     */
   public CacheNamePrefixEnum registerCacheName();
}
