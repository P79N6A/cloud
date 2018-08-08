package com.springframework.task.executor.configuration;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author summer
 */
@Slf4j
public abstract class ExecutorManager {
    /**
     * 注册的线程池，当系统重新启动时，先等待线程池中的任务完成，最大30s
     *
     * @param executor
     * @param maxWaitSecondsBeforeDomainRestart
     */
    private static Map<String, ThreadPoolExecutor> executorMap = new ConcurrentHashMap<>();

    public static void registerExecutor(String name, ThreadPoolExecutor executor) {
        executorMap.put(name, executor);
    }

    /**
     * 在系统重启前，等待一段时间，便于线程池执行完任务
     *
     * @param totalWait
     */
    public static void waitComplete(int totalWait) {
        for (Map.Entry<String, ThreadPoolExecutor> entry : executorMap.entrySet()) {
            while (totalWait > 0 && entry.getValue().getActiveCount() > 0) {
                try {
                    Thread.sleep(1000L);
                    log.warn("wait:" + entry.getKey() + " to complete!" + totalWait);
                } catch (InterruptedException e) {
                }
                totalWait--;
            }
        }
    }
}
