package com.springframework.redis;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author summer 防重复锁
 * 2018/12/4
 */
public class DuplicateProtectCacheManager extends GenericCacheRedisManager {

    private static final String CACHE_PREFIX = "duplicate:protect:cache:manager:";
    // 防重锁过期时间(ms), 通常设置得稍微长一点
    private static final long LOCK_EXPIRE = 8 * 60 * 60 * 1000;

    public DuplicateProtectCacheManager(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public static String getKey(String uuid) {
        return CACHE_PREFIX + uuid;
    }

    public boolean acquireLock(String lockKey) {
        String lock = DuplicateProtectCacheManager.class.getSimpleName() + getKey(lockKey);
        return (Boolean) super.redisTemplate.execute(
                (RedisCallback) connection -> {
                    try {
                        // 设置锁过期时间
                        long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
                        // 尝试加锁, 如果加锁成功, 则返回
                        if (connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes())) {
                            return true;
                        }
                        // 走到这一步表示没拿到锁，需要判断前一个锁的持有者是否超时
                        byte[] previousExipreAt = connection.get(lock.getBytes());

                        // 有可能前一个持有者刚好自己处理完之后delete了
                        if (previousExipreAt == null) {
                            if (connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes())) {
                                return true;
                            }
                            return false;
                        }
                        // 锁中设置的过期时间戳标示该锁已经过期了
                        if (Long.valueOf(new String(previousExipreAt)) < System.currentTimeMillis()) {
                            String previousExipreAtAfterGetSet = new String(connection.getSet(lock.getBytes(), String.valueOf(expireAt).getBytes()));
                            if (previousExipreAtAfterGetSet == null || new String(previousExipreAt).equals(previousExipreAtAfterGetSet)) {
                                return true;
                            }
                        }
                        return false;
                    } finally {
                        connection.close();
                    }
                }
        );
    }
}
