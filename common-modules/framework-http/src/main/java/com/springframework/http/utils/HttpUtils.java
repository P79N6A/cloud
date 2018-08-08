package com.springframework.http.utils;

import com.google.common.collect.Maps;
import com.springframework.http.service.HttpClientManager;
import com.springframework.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author summer
 */
public class HttpUtils {
    private static final transient Logger DB_LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    public static final int DEFAULT_TIMEOUT = 60 * 1000;
    // http请求异常出现，设置statuscode
    public static final int EXCEPTION_HTTP_STATUSCODE = 9999;
    /**
     * http请求状态：9001：HttpHostConnectException
     */
    public static final int HTTP_STATUSCODE_HTTP_HOST_CONNECT_EXCEPTION = 9001;
    /**
     * http请求状态：9002：ConnectTimeoutException
     */
    public static final int HTTP_STATUSCODE_CONNECT_TIMEOUT_EXCEPTION = 9002;
    /**
     * http请求状态：9003：SocketTimeoutException
     */
    public static final int HTTP_STATUSCODE_SOCKET_TIMEOUT_EXCEPTION = 9003;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static Map<String, AtomicLong> hostCountMap = new ConcurrentHashMap<String, AtomicLong>();


    private HttpClientManager httpClientManager;

    public HttpUtils() {
    }

    public HttpUtils(HttpClientManager httpclient) {
        this.httpClientManager = httpclient;
    }

    /**
     * 查询串提取
     *
     * @param queryStr
     * @param encode
     * @return
     */
    private static Pattern QUERY_MAP_PATTERN = Pattern.compile("&?([^=&]+)=");

    static {
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
    }

    public CloseableHttpClient getHttpClient() {
        try {
            return httpClientManager.getObject();
        } catch (Exception e) {
            DB_LOGGER.error("获取CloseableHttpClient 失败");
        }
        return null;
    }

    private static CookieStore getCookieStore(List<BasicClientCookie> cookies) {
        CookieStore cookieStore = new BasicCookieStore();
        if (cookies != null) {
            for (BasicClientCookie cookie : cookies) {
                cookieStore.addCookie(cookie);
            }
        }
        return cookieStore;
    }

    public static void setMaxConnectionsPerHost(String url) {
        try {
            URI uri = new URI(url);
            HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        } catch (Exception e) {

        }

    }


    public HttpResult getUrlWithCredentials(String url, String uname, String pwd) {
        HttpGet httpGet = getHttpGet(url, null, null);
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(uname, pwd);
        provider.setCredentials(AuthScope.ANY, credentials);
        //
        Map<String, String> headerMap = new HashMap<>();
        //测试
        headerMap.put("Authorization", "Basic c2NtX3Rlc3RlcjoxMjM0NTY=");
        HttpResult result = executeHttpRequestWithCredentials(httpGet, headerMap, DEFAULT_TIMEOUT, DEFAULT_CHARSET, provider);
        return result;
    }

    public HttpResult getUrlAsString(String url) {
        HttpGet httpGet = getHttpGet(url, null, null);
        HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_TIMEOUT, DEFAULT_CHARSET);
        return result;
    }

    public HttpResult getUrlAsString(String url, Map<String, String> params) {
        HttpGet httpGet = getHttpGet(url, params, DEFAULT_CHARSET);
        HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_TIMEOUT, DEFAULT_CHARSET);
        return result;
    }

    public HttpResult getUrlAsString(String url, String params) {
        try {
            Map jsonToMap = JsonUtils.readJsonToMap(params);
            HttpGet httpGet = getHttpGet(url, jsonToMap, DEFAULT_CHARSET);
            HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_TIMEOUT, DEFAULT_CHARSET);
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    public HttpResult getUrlAsString(String url, String params, Map<String, String> reqHeader) {
        try {
            Map jsonToMap = JsonUtils.readJsonToMap(params);
            HttpGet httpGet = getHttpGet(url, jsonToMap, DEFAULT_CHARSET);
            HttpResult result = executeHttpRequest(httpGet, reqHeader, DEFAULT_TIMEOUT, DEFAULT_CHARSET);
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    public HttpResult purgeUrlAsString(String url, HttpMethod method, Map<String, String> params, Map<String, String> reqHeader) {
        HttpOther httpOther = getHttpOtherMethod(url, method, params, DEFAULT_CHARSET);
        HttpResult result = executeHttpRequest(httpOther, reqHeader, DEFAULT_TIMEOUT, DEFAULT_CHARSET);
        return result;
    }

    public HttpResult purgeBodyAsString(String url, HttpMethod method, String body) {
        return purgeBodyAsString2(url, method, body, DEFAULT_CHARSET, null);
    }

    public HttpResult purgeBodyAsString2(String url, HttpMethod method, String body, String encode, Map<String, String> reqHeader) {
        HttpOther httpOther = getHttpOtherMethod2(url, method, body, encode);
        HttpResult result = executeHttpRequest(httpOther, reqHeader, DEFAULT_TIMEOUT, encode);
        return result;
    }

    public HttpResult getUrlAsString(String url, Map<String, String> params, int timeoutMills) {
        HttpGet httpGet = getHttpGet(url, params, DEFAULT_CHARSET);
        HttpResult result = executeHttpRequest(httpGet, null, timeoutMills, DEFAULT_CHARSET);
        return result;
    }

    public HttpResult getUrlAsString(String url, Map<String, String> params, List<BasicClientCookie> cookies) {
        HttpGet httpGet = getHttpGet(url, params, DEFAULT_CHARSET);
        HttpResult result = executeHttpRequestWithCookie(httpGet, null, DEFAULT_TIMEOUT, DEFAULT_CHARSET, cookies);
        return result;
    }

    public HttpResult getUrlAsString(String url, Map<String, String> params, String encode) {
        HttpGet httpGet = getHttpGet(url, params, encode);
        HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_TIMEOUT, encode);
        return result;
    }

    public HttpResult getUrlAsString(String url, Map<String, String> params, Map<String, String> reqHeader, String encode) {
        HttpGet httpGet = getHttpGet(url, params, encode);
        HttpResult result = executeHttpRequest(httpGet, reqHeader, DEFAULT_TIMEOUT, encode);
        return result;
    }

    public HttpResult getUrlAsString(String url, Map<String, String> params, Map<String, String> reqHeader, List<BasicClientCookie> cookies, String encode) {
        HttpGet httpGet = getHttpGet(url, params, encode);
        HttpResult result = executeHttpRequestWithCookie(httpGet, reqHeader, DEFAULT_TIMEOUT, encode, cookies);
        return result;
    }

    public boolean getUrlAsInputStream(String url, Map<String, String> params, RequestCallback callback) {
        HttpGet httpGet = getHttpGet(url, params, DEFAULT_CHARSET);
        return executeHttpRequest(httpGet, null, null, callback, DEFAULT_TIMEOUT);
    }

    public boolean getUrlAsInputStream(String url, Map<String, String> params, RequestCallback callback, String encode) {
        HttpGet httpGet = getHttpGet(url, params, encode);
        return executeHttpRequest(httpGet, null, null, callback, DEFAULT_TIMEOUT);
    }

    public boolean getUrlAsInputStream(String url, Map<String, String> params, RequestCallback callback, String encode, int timeoutMills) {
        HttpGet httpGet = getHttpGet(url, params, encode);
        return executeHttpRequest(httpGet, null, null, callback, timeoutMills);
    }

    public HttpResult postUrlAsString(String url, Map<String, String> params) {
        HttpPost httpPost = getHttpPost(url, params, DEFAULT_CHARSET);
        return executeHttpRequest(httpPost, null, DEFAULT_TIMEOUT, DEFAULT_CHARSET);
    }

    public HttpResult postUrlAsString(String url, Map<String, String> params, List<BasicClientCookie> cookies) {
        HttpPost httpPost = getHttpPost(url, params, DEFAULT_CHARSET);
        HttpResult result = executeHttpRequestWithCookie(httpPost, null, DEFAULT_TIMEOUT, DEFAULT_CHARSET, cookies);
        return result;
    }

    public HttpResult postUrlAsString(String url, Map<String, String> params, int timeoutMills) {
        HttpPost httpPost = getHttpPost(url, params, DEFAULT_CHARSET);
        return executeHttpRequest(httpPost, null, timeoutMills, DEFAULT_CHARSET);
    }

    public HttpResult postBodyAsString(String url, String body) {
        return postBodyAsString2(url, body, DEFAULT_CHARSET);
    }

    public HttpResult postBodyAsString2(String url, String body, String encode) {
        HttpPost httpPost = getHttpPost(url, body, encode);
        return executeHttpRequest(httpPost, null, DEFAULT_TIMEOUT, encode);
    }

    public HttpResult postBodyAsString2(String url, String body, String encode, Map<String, String> header) {
        HttpPost httpPost = getHttpPost(url, body, encode);
        return executeHttpRequest(httpPost, header, DEFAULT_TIMEOUT, encode);
    }

    public HttpResult postBodyAsString2(String url, byte[] body, String mimeType, String charset, Map<String, String> header) {
        HttpPost httpPost = getHttpPost(url, body, mimeType, charset);
        return executeHttpRequest(httpPost, header, DEFAULT_TIMEOUT, charset);
    }

    public HttpResult postBodyAsString(String url, String body, int timeoutMills) {
        return postBodyAsString2(url, body, DEFAULT_CHARSET, timeoutMills);
    }

    public HttpResult postBodyAsString2(String url, String body, String encode, int timeoutMills) {
        HttpPost httpPost = getHttpPost(url, body, encode);
        return executeHttpRequest(httpPost, null, timeoutMills, encode);
    }

    public HttpResult postUrlAsString(String url, Map<String, String> params, Map<String, String> reqHeader, String encode) {
        HttpPost httpPost = getHttpPost(url, params, encode);
        return executeHttpRequestWithCookie(httpPost, reqHeader, DEFAULT_TIMEOUT, encode, null);
    }

    public HttpResult postUrlAsString3(String url, Map<String, String> params, Map<String, String> reqHeader, String encode, int timeoutMills) {
        HttpPost httpPost = getHttpPost(url, params, encode);
        return executeHttpRequestWithCookie(httpPost, reqHeader, timeoutMills, encode, null);
    }

    public boolean postUrlAsInputStream(String url, Map<String, String> params, RequestCallback callback) {
        HttpPost httpPost = getHttpPost(url, params, DEFAULT_CHARSET);
        return executeHttpRequest(httpPost, null, null, callback, DEFAULT_TIMEOUT);
    }

    /**
     * @param url       上传的URL
     * @param params    其他参数
     * @param
     * @param inputName <input name="xxxx"../>
     * @param fileName  sss.jpg
     * @return
     */
    public HttpResult uploadFile(String url, Map<String, String> params, byte[] bytes, String inputName, String fileName) {
        return uploadFile(url, params, bytes, inputName, fileName, DEFAULT_CHARSET);
    }

    public HttpResult uploadFile(String url, Map<String, String> params, byte[] bytes, String inputName, String fileName, String encode) {
        Map<String, byte[]> uploadMap = Maps.newHashMap();
        uploadMap.put(inputName, bytes);
        Map<String, String> fileNameMap = Maps.newHashMap();
        fileNameMap.put(inputName, fileName);
        return uploadFile(url, params, uploadMap, fileNameMap, encode);
    }

    public HttpResult uploadFile(String url, Map<String, String> params, Map<String, byte[]> uploadMap, Map<String, String> fileNameMap) {
        return uploadFile(url, params, uploadMap, fileNameMap, DEFAULT_CHARSET);
    }

    public HttpResult uploadFile(String url, Map<String, String> params, Map<String, byte[]> uploadMap, Map<String, String> fileNameMap, String encode) {
        return uploadFile(url, params, uploadMap, fileNameMap, encode, DEFAULT_TIMEOUT);
    }

    /**
     * @param url
     * @param params
     * @param uploadMap
     * @param fileNameMap
     * @param encode
     * @return
     */
    public HttpResult uploadFile(String url, Map<String, String> params, Map<String, byte[]> uploadMap, Map<String, String> fileNameMap, String encode, int timeout) {
        /**
         * DefaultHttpClient client = new DefaultHttpClient();
         * client.getParams().setIntParameter("http.socket.timeout",
         * LONG_TIMEOUT); client.getParams().setBooleanParameter(
         * "http.protocol.expect-continue", false);
         * client.getParams().setIntParameter("http.connection.timeout",
         * CONNECTION_TIMEOUT);
         */

        CookieStore cookieStore = getCookieStore(null);
        CloseableHttpClient client = getHttpClient();
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setCharset(Charset.forName(encode));

        HttpPost request = new HttpPost(url);

        // MultipartEntity reqEntity = new MultipartEntity();
        for (String input : uploadMap.keySet()) {
            ByteArrayBody isb = new ByteArrayBody(uploadMap.get(input), fileNameMap.get(input));
            multipartEntityBuilder.addPart(input, isb);
        }
        try {
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    multipartEntityBuilder.addTextBody(key, params.get(key), ContentType.create("text/plain", Charset.forName(encode)));
                }
            }
            request.setEntity(multipartEntityBuilder.build());
            List<Cookie> reqcookie = cookieStore.getCookies();
            CloseableHttpResponse response = client.execute(request);
            try {
                String result = "";
                HttpEntity entity = getEntity(response);
                if (entity != null) {
                    result = EntityUtils.toString(entity, encode);
                }
                if (isSuccess(response)) {

                    HttpResult ret = HttpResult.getSuccessReturn(result);
                    addHeader(ret, response);
                    List<Cookie> cookies = cookieStore.getCookies();
                    addCookie(ret, cookies, reqcookie);
                    return ret;
                } else {
                    int statusCode = response.getStatusLine().getStatusCode();
                    String msg = "httpStatus:" + statusCode + response.getStatusLine().getReasonPhrase() + ", Header: ";
                    Header[] headers = response.getAllHeaders();
                    for (Header header : headers) {
                        msg += header.getName() + ":" + header.getValue();
                    }
                    request.abort();
                    DB_LOGGER.error("ERROR HttpUtils:" + msg + request.getURI());
                    return HttpResult.getFailure("httpStatus:" + response.getStatusLine().getStatusCode(), statusCode, result);
                }
            } finally {
                response.close();
            }
        } catch (HttpHostConnectException e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), HTTP_STATUSCODE_HTTP_HOST_CONNECT_EXCEPTION);
        } catch (ConnectTimeoutException e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), HTTP_STATUSCODE_CONNECT_TIMEOUT_EXCEPTION);
        } catch (SocketTimeoutException e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), HTTP_STATUSCODE_SOCKET_TIMEOUT_EXCEPTION);
        } catch (Exception e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), EXCEPTION_HTTP_STATUSCODE);
        }
    }

    private void addCookie(HttpResult ret, List<Cookie> cookies, List<Cookie> reqcookie) {
        try {
            for (Cookie cookie : cookies) {
                if (!reqcookie.contains(cookie)) {
                    ret.addCookie(cookie.getName(), cookie.getPath(), cookie.getValue(), cookie.getExpiryDate());
                }
            }
        } catch (Exception e) {
        }
    }

    private void addHeader(HttpResult ret, HttpResponse response) {
        try {
            Header[] headers = response.getAllHeaders();
            for (Header header : headers) {
                ret.addHeader(header.getName(), header.getValue());
            }
        } catch (Exception e) {
        }
    }

    private HttpPost getHttpPost(String url, Map<String, String> params, String encoding) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept-Encoding", "gzip,deflate");
        if (params != null) {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            for (String name : params.keySet()) {
                form.add(new BasicNameValuePair(name, params.get(name)));
            }
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, encoding);
                httpPost.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                DB_LOGGER.error("", e);
            }
        }
        return httpPost;
    }

    private HttpOther getHttpOtherMethod(String url, HttpMethod method, Map<String, String> params, String encoding) {
        HttpOther httpOther = getHttpOtherMethod(url, method);
        if (params != null) {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            for (String name : params.keySet()) {
                form.add(new BasicNameValuePair(name, params.get(name)));
            }
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, encoding);
                httpOther.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                DB_LOGGER.error("", e);
            }
        }
        return httpOther;
    }

    private HttpOther getHttpOtherMethod(String url, HttpMethod method) {
        HttpOther httpOther = new HttpOther(method);
        httpOther.setURI(URI.create(url));
        httpOther.setHeader("Accept-Encoding", "gzip,deflate");
        return httpOther;
    }

    private HttpOther getHttpOtherMethod2(String url, HttpMethod method, String body, String encoding) {
        HttpOther httpOther = getHttpOtherMethod(url, method);
        if (body != null) {
            try {
                HttpEntity entity = new StringEntity(body, encoding);
                httpOther.setEntity(entity);
            } catch (Exception e) {
                DB_LOGGER.error("", e);
            }
        }
        return httpOther;
    }

    private HttpPost getHttpPost(String url, String body, String encoding) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept-Encoding", "gzip,deflate");
        if (body != null) {

            try {
                HttpEntity entity = new StringEntity(body, encoding);
                httpPost.setEntity(entity);
            } catch (Exception e) {// UnsupportedEncodingException e) {
                DB_LOGGER.error("", e);
            }
        }
        return httpPost;
    }

    private HttpPost getHttpPost(String url, byte[] body, String mimeType, String charset) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept-Encoding", "gzip,deflate");
        if (body != null) {
            try {
                ContentType contentType = ContentType.create(mimeType, charset);
                HttpEntity entity = new ByteArrayEntity(body, contentType);
                httpPost.setEntity(entity);
            } catch (Exception e) {// UnsupportedEncodingException e) {
                DB_LOGGER.error("", e);
            }
        }
        return httpPost;
    }

    private HttpGet getHttpGet(String url, Map<String, String> params, String encode) {
        String fullUrl = getFullUrl(url, params, encode);
        HttpGet httpGet = new HttpGet(fullUrl);
        httpGet.setHeader("Accept-Encoding", "gzip,deflate");
        return httpGet;
    }

    public static String getFullUrl(String url, Map<String, String> params, String encode) {
        if (params != null) {
            if (url.indexOf('?') == -1) {
                url += "?";
            } else {
                url += "&";
            }
            for (String name : params.keySet()) {
                try {
                    if (StringUtils.isNotBlank(params.get(name))) {
                        url += name + "=" + URLEncoder.encode(params.get(name), encode) + "&";
                    }
                } catch (UnsupportedEncodingException e) {
                }
            }
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    private HttpResult executeHttpRequest(HttpUriRequest request, Map<String, String> reqHeader, int timeoutMills, String charset) {
        return executeHttpRequestWithCookie(request, reqHeader, timeoutMills, charset, null);
    }

    private HttpResult executeHttpRequestWithCredentials(HttpUriRequest request, Map<String, String> reqHeader, int timeoutMills, String charset, CredentialsProvider provider) {
        CloseableHttpClient client = getHttpClient();
        if (reqHeader != null) {
            for (String name : reqHeader.keySet()) {
                request.addHeader(name, reqHeader.get(name));
            }
        }
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(provider);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request, context);
            HttpEntity entity = getEntity(response);
            String result = "";
            if (entity != null) {
                result = EntityUtils.toString(getEntity(response), charset);
            }
            if (isSuccess(response)) {
                HttpResult ret = HttpResult.getSuccessReturn(result);
                addHeader(ret, response);
                return ret;
            } else {
                int statusCode = response.getStatusLine().getStatusCode();
                HttpResult ret = HttpResult.getFailure("httpStatus:" + response.getStatusLine().getStatusCode(), statusCode, result);
                addHeader(ret, response);
                String msg = "httpStatus:" + statusCode + response.getStatusLine().getReasonPhrase() + ", Header: " + ret.getAllHeaders();
                DB_LOGGER.error("ERROR HttpUtils:" + msg + request.getURI());
                request.abort();
                return ret;
            }
        } catch (Exception e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), HTTP_STATUSCODE_HTTP_HOST_CONNECT_EXCEPTION);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    DB_LOGGER.error(request.getURI() + ":" + e);
                    e.printStackTrace();
                }
            }
        }
    }

    private HttpResult executeHttpRequestWithCookie(HttpUriRequest request, Map<String, String> reqHeader, int timeoutMills, String charset, List<BasicClientCookie> cookies) {
        hostCount(request);

        CookieStore cookieStore = getCookieStore(cookies);
        CloseableHttpClient client = getHttpClient();
        if (reqHeader != null) {
            for (String name : reqHeader.keySet()) {
                request.addHeader(name, reqHeader.get(name));
            }
        }
        try {
            List<Cookie> reqcookie = cookieStore.getCookies();
            CloseableHttpResponse response = client.execute(request);
            try {
                HttpEntity entiry = getEntity(response);
                String result = "";
                if (entiry != null) {
                    result = EntityUtils.toString(getEntity(response), charset);
                }
                if (isSuccess(response)) {
                    HttpResult ret = HttpResult.getSuccessReturn(result);
                    addHeader(ret, response);
                    List<Cookie> cookieList = cookieStore.getCookies();
                    addCookie(ret, cookieList, reqcookie);
                    return ret;
                } else {
                    int statusCode = response.getStatusLine().getStatusCode();
                    HttpResult ret = HttpResult.getFailure("httpStatus:" + response.getStatusLine().getStatusCode(), statusCode, result);
                    addHeader(ret, response);
                    String msg = "httpStatus:" + statusCode + response.getStatusLine().getReasonPhrase() + ", Header: " + ret.getAllHeaders();
                    DB_LOGGER.error("ERROR HttpUtils:" + msg + request.getURI());
                    request.abort();
                    return ret;
                }
            } finally {
                response.close();
            }
        } catch (HttpHostConnectException e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), HTTP_STATUSCODE_HTTP_HOST_CONNECT_EXCEPTION);
        } catch (ConnectTimeoutException e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), HTTP_STATUSCODE_CONNECT_TIMEOUT_EXCEPTION);
        } catch (SocketTimeoutException e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), HTTP_STATUSCODE_SOCKET_TIMEOUT_EXCEPTION);
        } catch (Exception e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), EXCEPTION_HTTP_STATUSCODE);
        }
    }

    private boolean isSuccess(HttpResponse response) {
        return response.getStatusLine().getStatusCode() >= HttpStatus.SC_OK
                && response.getStatusLine().getStatusCode() <= HttpStatus.SC_MULTI_STATUS;
    }

    private boolean executeHttpRequest(HttpUriRequest request, Map<String, String> reqHeader, BasicClientCookie cookie, RequestCallback callback, int timeoutMills) {
        hostCount(request);

        CookieStore cookieStore = getCookieStore(Arrays.asList(cookie));
        CloseableHttpClient client = getHttpClient();

        if (reqHeader != null) {
            for (String name : reqHeader.keySet()) {
                request.addHeader(name, reqHeader.get(name));
            }
        }
        try {
            CloseableHttpResponse response = client.execute(request);
            try {
                if (isSuccess(response)) {
                    HttpEntity entity = getEntity(response);
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        try {
                            return callback.processResult(instream, getAllHeaders(response));
                        } catch (Exception e) {
                            DB_LOGGER.error(request.getURI() + ":" + e);
                        } finally {
                            if (instream != null) {
                                instream.close();
                            }
                        }
                    }
                } else {
                    String msg = "httpStatus:" + response.getStatusLine().getStatusCode() + response.getStatusLine().getReasonPhrase() + ", Header: ";
                    Header[] headers = response.getAllHeaders();
                    for (Header header : headers) {
                        msg += header.getName() + ":" + header.getValue();
                    }
                    HttpEntity entity = getEntity(response);
                    if (entity != null) {
                        EntityUtils.toString(entity);
                    }
                    request.abort();
                    DB_LOGGER.error("ERROR HttpUtils:" + msg + request.getURI());
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            request.abort();
            DB_LOGGER.error(request.getURI() + ":" + e);
        }
        return false;
    }

    private static Map<String, String> getAllHeaders(HttpResponse response) {
        Header[] headers = response.getAllHeaders();
        Map<String, String> headerMap = new HashMap<>();
        for (Header header : headers) {
            if (StringUtils.isNotBlank(header.getName()) && StringUtils.isNotBlank(header.getValue())) {
                headerMap.put(header.getName(), header.getValue());
            }
        }
        return headerMap;
    }

    /**
     * @param queryString encoded queryString queryString is already encoded (e.g %20 and
     *                    & may be present)
     * @param encode
     * @return
     */
    public static Map<String, String> parseQueryStr(String queryString, String encode) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        if (StringUtils.isBlank(queryString)) {
            return map;
        }
        Matcher matcher = QUERY_MAP_PATTERN.matcher(queryString);
        String key = null, value;
        int end = 0;
        while (matcher.find()) {
            if (key != null) {
                try {
                    value = queryString.substring(end, matcher.start());
                    if (StringUtils.isNotBlank(value)) {
                        value = URLDecoder.decode(value, encode);
                        map.put(key, value);
                    }
                } catch (UnsupportedEncodingException e) {
                    DB_LOGGER.error("" + e);
                }
            }
            key = matcher.group(1);
            end = matcher.end();
        }
        if (key != null) {
            try {
                value = queryString.substring(end);
                if (StringUtils.isNotBlank(value)) {
                    value = URLDecoder.decode(value, encode);
                    map.put(key, value);
                }
            } catch (UnsupportedEncodingException e) {
                DB_LOGGER.error(e + "");
            }
        }
        return map;
    }

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

    private static HttpEntity getEntity(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        Header header = entity.getContentEncoding();
        if (header != null) {
            for (HeaderElement element : header.getElements()) {
                if (element.getName().toLowerCase().indexOf("gzip") != -1) {
                    entity = new GzipDecompressingEntity(entity);
                    break;
                }
            }
        }
        return entity;
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
            DB_LOGGER.error(e + "");
        }
    }

    private static class MyDefaultRedirectStrategy extends DefaultRedirectStrategy {
        @Override
        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
            boolean isRedirect = false;
            try {
                isRedirect = super.isRedirected(request, response, context);
            } catch (ProtocolException e) {
                DB_LOGGER.error("isRedirected:" + e);
            }
            if (!isRedirect) {
                int responseCode = response.getStatusLine().getStatusCode();
                if (responseCode == /*301*/HttpStatus.SC_MOVED_PERMANENTLY || responseCode == /*302*/HttpStatus.SC_MOVED_TEMPORARILY) {
                    return true;
                }
            }
            return isRedirect;
        }
    }

    public static class FileRequestCallback implements RequestCallback {
        private File file;

        public FileRequestCallback(File file) {
            this.file = file;
        }

        @Override
        public boolean processResult(InputStream stream, Map<String, String> headerMap) {
            OutputStream os = null;
            try {
                os = new FileOutputStream(file);
                IOUtils.copy(stream, os);
                os.close();
                return true;
            } catch (Exception e) {
                DB_LOGGER.error("", e);
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (Exception e) {
                }
            }
            return false;
        }
    }

}
