package com.springframework.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author summer  统一线程池管理
 * 2018/7/13
 */
@Configuration
public class CouponSentMessagePoolConfiguration {
    @Bean
    public ThreadPoolTaskExecutor couponSentMessageTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//线程池的基本大小，即在没有任务需要执行的时候线程池的大小，并且只有在工作队列满了的情况下才会创建超出这个数量的线程。这里需要注意的是：在刚刚创建ThreadPoolExecutor的时候，线程并不会立即启动，而是要等到有任务提交时才会启动，
        threadPoolTaskExecutor.setCorePoolSize(20);
//如果一个线程处在空闲状态的时间超过了该属性值，就会因为超时而退出。举个例子，如果线程池的核心大小corePoolSize=5，而当前大小poolSize =8，那么超出核心大小的线程，会按照keepAliveTime的值判断是否会超时退出
        threadPoolTaskExecutor.setKeepAliveSeconds(200);
//线程池中允许的最大线程数，线程池中的当前线程数目不会超过该值。如果队列中任务已满，并且当前线程个数小于maxPoolSize，那么会创建新的线程来执行任务。
        threadPoolTaskExecutor.setMaxPoolSize(100);
//等待运行的线程阻塞队列的长度，务必设置。否则默认值是Integer.MAX
        threadPoolTaskExecutor.setQueueCapacity(30);
//超出阻塞队列时，新的线程处理方式。下面的处理方式是：在主线程运行
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//  重要，优雅关闭
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskExecutor;
    }
}