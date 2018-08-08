package com.springframework.http.utils;

import com.google.common.collect.Maps;
import com.springframework.http.service.AsyncHttpClientManager;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author summer
 * 2018/8/8
 */
public class AsyncHttpClientUtils {

    private AsyncHttpClientManager asyncHttpClientManager;

    public AsyncHttpClientUtils(AsyncHttpClientManager asyncHttpClientManager) {
        this.asyncHttpClientManager = asyncHttpClientManager;
    }

    private static Logger LOG = LoggerFactory
            .getLogger(AsyncHttpClientUtils.class);

    private static String utf8Charset = "utf-8";
    private static Map<String, AtomicLong> hostCountMap = new ConcurrentHashMap<String, AtomicLong>();

    public static Map<String, Long> getStats(boolean clean) {
        Map<String, Long> result = Maps.newHashMap();
        for (Map.Entry<String, AtomicLong> entry : hostCountMap.entrySet()) {
            long value = entry.getValue().get();
            if (value > 0) {
                result.put(entry.getKey(), value);
                if (clean) {
                    entry.getValue().addAndGet(-value);
                }
            }
        }
        return result;
    }

    private static void hostCount(HttpUriRequest request) {
        try {
            if (request == null) {
                return;
            }
            if (request.getURI() == null || StringUtils.isBlank(request.getURI().getHost())) {
                return;
            }
            URI uri = request.getURI();
            int port = uri.getPort();

            String host = uri.getScheme() + "://" + uri.getHost() + (port == -1 ? "" : ":" + port) + uri.getPath();
            AtomicLong al = hostCountMap.get(host);
            if (al == null) {
                al = new AtomicLong(0);
                hostCountMap.put(host, al);
            }
            al.incrementAndGet();
        } catch (Exception e) {
            LOG.error(e + "");
        }
    }

    /**
     * 向指定的url发送一次异步post请求,参数是字符串
     *
     * @param baseUrl    请求地址
     * @param postString 请求参数,格式是json.toString()
     * @param urlParams  请求参数,格式是String
     * @param callback   回调方法,格式是FutureCallback
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public void httpAsyncPost(String baseUrl, String postString,
                              String urlParams, FutureCallback callback) throws Exception {
        if (baseUrl == null || "".equals(baseUrl)) {
            LOG.warn("we don't have base url, check config");
            throw new Exception("missing base url");
        }
        CloseableHttpAsyncClient hc = asyncHttpClientManager.getCloseableHttpAsyncClient(false);
        try {
            hc.start();
            HttpPost httpPost = new HttpPost(baseUrl);

            httpPost.setHeader("Connection", "close");

            if (null != postString) {
                LOG.debug("exeAsyncReq post postBody={}", postString);
                StringEntity entity = new StringEntity(postString.toString(), utf8Charset);
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }

            if (null != urlParams) {

                httpPost.setURI(new URI(httpPost.getURI().toString()
                        + "?" + urlParams));
            }

            LOG.warn("exeAsyncReq getparams:" + httpPost.getURI());

            hc.execute(httpPost, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 向指定的url发送一次异步post请求,参数是字符串
     *
     * @param baseUrl   请求地址
     * @param urlParams 请求参数,格式是List<BasicNameValuePair>
     * @param callback  回调方法,格式是FutureCallback
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public void httpAsyncPost(String baseUrl, List<BasicNameValuePair> postBody,
                              List<BasicNameValuePair> urlParams, FutureCallback callback) throws Exception {
        if (baseUrl == null || "".equals(baseUrl)) {
            LOG.warn("we don't have base url, check config");
            throw new Exception("missing base url");
        }

        try {
            CloseableHttpAsyncClient hc = asyncHttpClientManager.getCloseableHttpAsyncClient(false);

            hc.start();

            HttpPost httpPost = new HttpPost(baseUrl);

            httpPost.setHeader("Connection", "close");

            if (null != postBody) {
                LOG.debug("exeAsyncReq post postBody={}", postBody);
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                        postBody, "UTF-8");
                httpPost.setEntity(entity);
            }

            if (null != urlParams) {

                String getUrl = EntityUtils
                        .toString(new UrlEncodedFormEntity(urlParams));

                httpPost.setURI(new URI(httpPost.getURI().toString()
                        + "?" + getUrl));
            }

            LOG.warn("exeAsyncReq getparams:" + httpPost.getURI());


            hc.execute(httpPost, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向指定的url发送一次异步get请求,参数是String
     *
     * @param baseUrl   请求地址
     * @param urlParams 请求参数,格式是String
     * @param callback  回调方法,格式是FutureCallback
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public void httpAsyncGet(String baseUrl, String urlParams, FutureCallback callback) throws Exception {

        if (baseUrl == null || "".equals(baseUrl)) {
            LOG.warn("we don't have base url, check config");
            throw new Exception("missing base url");
        }
        CloseableHttpAsyncClient hc = asyncHttpClientManager.getCloseableHttpAsyncClient(false);
        try {


            hc.start();

            HttpGet httpGet = new HttpGet(baseUrl);

            httpGet.setHeader("Connection", "close");

            if (null != urlParams || "".equals(urlParams)) {

                httpGet.setURI(new URI(httpGet.getURI().toString()
                        + "?" + urlParams));
            } else {
                httpGet.setURI(new URI(httpGet.getURI().toString()));
            }

            LOG.warn("exeAsyncReq getparams:" + httpGet.getURI());


            hc.execute(httpGet, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 向指定的url发送一次异步get请求,参数是List<BasicNameValuePair>
     *
     * @param baseUrl   请求地址
     * @param urlParams 请求参数,格式是List<BasicNameValuePair>
     * @param callback  回调方法,格式是FutureCallback
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public void httpAsyncGet(String baseUrl, List<BasicNameValuePair> urlParams, FutureCallback callback) throws Exception {
        if (baseUrl == null || "".equals(baseUrl)) {
            LOG.warn("we don't have base url, check config");
            throw new Exception("missing base url");
        }

        try {
            CloseableHttpAsyncClient hc = asyncHttpClientManager.getCloseableHttpAsyncClient(false);

            hc.start();

            HttpPost httpGet = new HttpPost(baseUrl);

            httpGet.setHeader("Connection", "close");

            if (null != urlParams) {

                String getUrl = EntityUtils
                        .toString(new UrlEncodedFormEntity(urlParams));

                httpGet.setURI(new URI(httpGet.getURI().toString()
                        + "?" + getUrl));
            }

            LOG.warn("exeAsyncReq getparams:" + httpGet.getURI());


            hc.execute(httpGet, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

