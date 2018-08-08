package com.springframework.http.service;

import com.springframework.http.configure.*;
import com.springframework.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.springframework.http.utils.HttpUtils.getStats;

/**
 * @author summer
 * 2018/8/8
 * 描述：HttpClient客户端封装
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ThreadPoolTaskExecutor.class, MyconnectionKeepAliveStrategy.class, MyDefaultProxyRoutePlanner.class,
        MyDefaultRedirectStrategy.class, MyPoolingHttpClientConnectionManager.class,MyHttpRequestRetryHandler.class,MyRequestConfig.class})
public class HttpClientManager implements FactoryBean<CloseableHttpClient>, InitializingBean, DisposableBean {

    /**
     * FactoryBean生成的目标对象
     */
    private CloseableHttpClient client;
    private final ThreadPoolTaskExecutor httpClientManagerCleanTaskExecutor;
    private final ConnectionKeepAliveStrategy connectionKeepAliveStrategy;
    private final DefaultRedirectStrategy defaultRedirectStrategy;
    private final HttpRequestRetryHandler httpRequestRetryHandler;
    private final DefaultProxyRoutePlanner proxyRoutePlanner;
    private final PoolingHttpClientConnectionManager poolHttpcConnManager;
    private final RequestConfig config;


    @Autowired
    public HttpClientManager(DefaultRedirectStrategy defaultRedirectStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRequestRetryHandler httpRequestRetryHandler, DefaultProxyRoutePlanner proxyRoutePlanner, PoolingHttpClientConnectionManager poolHttpcConnManager, RequestConfig config, ThreadPoolTaskExecutor httpClientManagerCleanTaskExecutor) {
        this.connectionKeepAliveStrategy = connectionKeepAliveStrategy;
        this.httpRequestRetryHandler = httpRequestRetryHandler;
        this.defaultRedirectStrategy = defaultRedirectStrategy;
        this.proxyRoutePlanner = proxyRoutePlanner;
        this.poolHttpcConnManager = poolHttpcConnManager;
        this.config = config;
        this.httpClientManagerCleanTaskExecutor = httpClientManagerCleanTaskExecutor;
    }

    /**
     * 销毁上下文时，销毁HttpClient实例
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        /*
         * 调用httpClient.close()会先shut down connection manager，然后再释放该HttpClient所占用的所有资源，
         * 关闭所有在使用或者空闲的connection包括底层socket。由于这里把它所使用的connection manager关闭了，
         * 所以在下次还要进行http请求的时候，要重新new一个connection manager来build一个HttpClient,
         * 也就是在需要关闭和新建Client的情况下，connection manager不能是单例的.
         */
        if (null != this.client) {
            this.client.close();
        }
    }

    @Override// 初始化实例
    public void afterPropertiesSet() throws Exception {
        /*
         * 建议此处使用HttpClients.custom的方式来创建HttpClientBuilder，而不要使用HttpClientBuilder.create()方法来创建HttpClientBuilder
         * 从官方文档可以得出，HttpClientBuilder是非线程安全的，但是HttpClients确实Immutable的，immutable 对象不仅能够保证对象的状态不被改变，
         * 而且还可以不使用锁机制就能被其他线程共享
         */
        this.client = HttpClients.custom().setConnectionManager(poolHttpcConnManager)
                .setRetryHandler(httpRequestRetryHandler)
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                .setRedirectStrategy(defaultRedirectStrategy)
                .setRoutePlanner(proxyRoutePlanner)
                .setDefaultRequestConfig(config)
                .build();

        this.logHostCountCommand();
        this.idleConnectionEvictorCommand();
    }


    /**
     * 20分钟清理一次
     */
    @Async("httpClientManagerCleanTaskExecutor")
    @Scheduled(fixedRate = 1000 * 60 * 20)
    public void logHostCountCommand() {
        Map<String, Long> stats = getStats(true);
        if (!stats.isEmpty()) {
            log.warn("HostCount:" + JsonUtils.writeObjectToJson(stats));
        }
    }

    /**
     * 5分钟清理一次
     */
    @Async("httpClientManagerCleanTaskExecutor")
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void idleConnectionEvictorCommand() {
        poolHttpcConnManager.closeExpiredConnections();
        // cm.closeIdleConnections(CONNECTION_IDLE_TIMEOUT,
        // TimeUnit.MILLISECONDS);//
        PoolStats poolStats = poolHttpcConnManager.getTotalStats();
        log.warn("PoolStats----" + poolStats.toString());
    }


    /**
     * 返回实例的类型
     *
     * @return
     * @throws Exception
     */
    @Override
    public CloseableHttpClient getObject() throws Exception {
        return this.client;
    }

    @Override
    public Class<?> getObjectType() {
        return (this.client == null ? CloseableHttpClient.class : this.client.getClass());
    }

    /**
     * 构建的实例为单例
     *
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }


}

