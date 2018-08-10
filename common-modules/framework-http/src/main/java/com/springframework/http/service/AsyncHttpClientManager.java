package com.springframework.http.service;

import com.springframework.http.configure.async.MyAsyncRequestConfig;
import com.springframework.http.configure.async.MyCredentialsProvider;
import com.springframework.http.configure.async.MyIOReactorConfig;
import com.springframework.http.configure.async.MySchemeIOSessionStrategy;
import com.springframework.log.log.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.pool.PoolStats;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.charset.CodingErrorAction;
import java.util.Map;

import static com.springframework.http.utils.AsyncHttpClientUtils.getStats;

/** @author summer 2018/8/8 描述：HttpClient客户端封装 */
@Configuration
@Slf4j
//@Service
@EnableConfigurationProperties({
  MyAsyncRequestConfig.class, MyCredentialsProvider.class,
  MyIOReactorConfig.class, MySchemeIOSessionStrategy.class
})
public class AsyncHttpClientManager
    implements FactoryBean<CloseableHttpClient>, InitializingBean, DisposableBean {

  /** http代理相关参数 */
  private String host = "localhost";

  /** */
  private int port = 8080;

  /** 连接池最大连接数 */
  private static int poolSize = 3000;

  /** 每个主机的并发最多只有1500 */
  private static int maxPerRoute = 1500;

  /** 异步httpclient */
  private CloseableHttpAsyncClient asyncHttpClient;

  /** 异步加代理的httpclient */
  private CloseableHttpAsyncClient proxyAsyncHttpClient;

  private PoolingNHttpClientConnectionManager conMgr = null;
  @Autowired
  private RequestConfig asyncConfig;
  @Autowired
  private IOReactorConfig ioReactorConfig;
  @Autowired
  private CredentialsProvider credentialsProvider;
  @Autowired
  private Registry<SchemeIOSessionStrategy> sessionStrategyRegistry;

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
    if (null != this.asyncHttpClient) {
      this.asyncHttpClient.close();
    }
    if (null != this.proxyAsyncHttpClient) {
      this.proxyAsyncHttpClient.close();
    }
  }

  @Override // 初始化实例
  public void afterPropertiesSet() throws Exception {
    /*
     * 建议此处使用HttpClients.custom的方式来创建HttpClientBuilder，而不要使用HttpClientBuilder.create()方法来创建HttpClientBuilder
     * 从官方文档可以得出，HttpClientBuilder是非线程安全的，但是HttpClients确实Immutable的，immutable 对象不仅能够保证对象的状态不被改变，
     * 而且还可以不使用锁机制就能被其他线程共享
     */
    this.asyncHttpClient = createAsyncClient(false);
    this.proxyAsyncHttpClient = createAsyncClient(false);
    this.logHostCountCommand();
    this.idleConnectionEvictorCommand();
  }

  public CloseableHttpAsyncClient createAsyncClient(boolean proxy) throws IOReactorException {

    // 设置连接池大小
    ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
    conMgr =
        new PoolingNHttpClientConnectionManager(ioReactor, null, sessionStrategyRegistry, null);

    if (poolSize > 0) {
      conMgr.setMaxTotal(poolSize);
    }

    if (maxPerRoute > 0) {
      conMgr.setDefaultMaxPerRoute(maxPerRoute);
    } else {
      conMgr.setDefaultMaxPerRoute(10);
    }

    ConnectionConfig connectionConfig =
        ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .build();

    Lookup<AuthSchemeProvider> authSchemeRegistry =
        RegistryBuilder.<AuthSchemeProvider>create()
            .register(AuthSchemes.BASIC, new BasicSchemeFactory())
            .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
            .register(AuthSchemes.NTLM, new NTLMSchemeFactory())
            .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
            .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
            .build();
    conMgr.setDefaultConnectionConfig(connectionConfig);

    if (proxy) {
      return HttpAsyncClients.custom()
          .setConnectionManager(conMgr)
          .setDefaultCredentialsProvider(credentialsProvider)
          .setDefaultAuthSchemeRegistry(authSchemeRegistry)
          .setProxy(new HttpHost(host, port))
          .setDefaultCookieStore(new BasicCookieStore())
          .setDefaultRequestConfig(asyncConfig)
          .build();
    } else {
      return HttpAsyncClients.custom()
          .setConnectionManager(conMgr)
          .setDefaultCredentialsProvider(credentialsProvider)
          .setDefaultAuthSchemeRegistry(authSchemeRegistry)
          .setDefaultCookieStore(new BasicCookieStore())
          .build();
    }
  }

  /** 20分钟清理一次 */
  @Async("httpClientManagerCleanTaskExecutor")
  @Scheduled(fixedRate = 1000 * 60 * 20)
  public void logHostCountCommand() {
    Map<String, Long> stats = getStats(true);
    if (!stats.isEmpty()) {
      log.warn("HostCount:" + JsonUtils.writeObjectToJson(stats));
    }
  }

  /** 5分钟清理一次 */
  @Async("httpClientManagerCleanTaskExecutor")
  @Scheduled(fixedRate = 1000 * 60 * 5)
  public void idleConnectionEvictorCommand() {
    conMgr.closeExpiredConnections();
    // cm.closeIdleConnections(CONNECTION_IDLE_TIMEOUT,
    // TimeUnit.MILLISECONDS);//
    PoolStats poolStats = conMgr.getTotalStats();
    log.warn("PoolStats----" + poolStats.toString());
  }

  /**
   * 返回实例的类型
   *
   * @return
   */
  @Override
  public CloseableHttpClient getObject() {
    return null;
  }

  public CloseableHttpAsyncClient getCloseableHttpAsyncClient(boolean proxy) {
    return proxy ? proxyAsyncHttpClient : asyncHttpClient;
  }

  @Override
  public Class<?> getObjectType() {
    return (this.asyncHttpClient == null
        ? CloseableHttpClient.class
        : this.asyncHttpClient.getClass());
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
