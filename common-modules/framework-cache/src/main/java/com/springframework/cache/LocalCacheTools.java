package com.springframework.cache;




import com.springframework.utils.StringUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author summer
 * 使用本地缓存做一层代理，使用集中缓存做数据预热
 */
public class LocalCacheTools implements CacheTools{
    private Gcache<String, Object> cachedUkeyMap;
    private GenericCacheManager genericCacheManager;
    private AtomicInteger remoteHit = new AtomicInteger(0);
    public LocalCacheTools(int maxnum, GenericCacheManager genericCacheManager){
        cachedUkeyMap = new Gcache<String, Object>(maxnum);
        this.genericCacheManager = genericCacheManager;
    }

    private String getRealKey(String regionName, String key){
        String result = regionName + key;
        if(result.length()>128){
            result = "md5" + StringUtil.md5(key);
        }
        return result;
    }


    @Override
    public Object get(String regionName, String key) {
        String realKey = getRealKey(regionName, key);
        Object result = cachedUkeyMap.getIfPresent(realKey);
        if(result == null){
            result = genericCacheManager.get(regionName, key);
            if(result!=null){
                remoteHit.incrementAndGet();
                cachedUkeyMap.put(realKey, result);
            }
        }
        return result;
    }

    @Override
    public synchronized Boolean set(String regionName, String key, Object value) {
        cachedUkeyMap.put(getRealKey(regionName, key), value);
       return genericCacheManager.set(regionName, key, value);
    }

    @Override
    public synchronized Boolean remove(String regionName, String key) {
        cachedUkeyMap.invalidate(getRealKey(regionName, key));
       return genericCacheManager.remove(regionName, key);
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    public int getRemoteHit() {
        return remoteHit.get();
    }

}

