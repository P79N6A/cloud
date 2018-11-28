package com.springframework.gateway.event.listener;

import com.springframework.gateway.event.CacheExpireFailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * cache事件监听
 *
 * @author summer
 */
@Component
@Slf4j
public class CacheListener {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param event CacheExpireFailEvent
     */
    @EventListener
    public void onAppDeletionEvent(CacheExpireFailEvent event) {
        redisTemplate.opsForHash().delete(event.getPrefix(), event.getItem());
    }
}
