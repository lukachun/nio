package com.chungang.Demo;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
    static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    private static HttpClient httpClient = null;
    private static Map<String, HttpClient> httpClientMap = new ConcurrentHashMap<String,HttpClient>();

    private static HttpClient getSharedHttpClient(int soTimeout, int connTimeout){
        String key = "HC_" + soTimeout + "_" +connTimeout;
        HttpClient httpClient = httpClientMap.get(key);
        if(httpClient == null){
            httpClient = getNewInstance(soTimeout, connTimeout);
            httpClient.getParams().setContentCharset("utf-8");
            httpClientMap.put(key, httpClient);
        }
        return httpClient;
    }

    public static String getResponseBodyAsStringByGet(String url, Map<String, String> params, int soTimeout, int connTimeout){
        StringBuffer buffer = new StringBuffer(url);
        if(params!=null&&params.size()>0){
            buffer.append("?");
            for(Entry<String, String> entry:params.entrySet()){
                buffer.append(entry.getKey() + "="+ encode(entry.getValue()) + "&");
            }
        }
        LOGGER.debug("Final URL:" + buffer.toString());
        GetMethod method = new GetMethod(buffer.toString());
        try {
            try {
                int code = getSharedHttpClient(soTimeout, connTimeout).executeMethod(method);
                if (code == HttpStatus.SC_OK) {
                    return method.getResponseBodyAsString();
                }
            } finally {
                method.releaseConnection();
            }
        } catch (Throwable e) {
            LOGGER.warn("http error, url:"+ buffer.toString(), e);
        }
        return null;
    }

    public static InputStream getResponseBodyAsStreamByGet(String url, Map<String, String> params, int soTimeout, int connTimeout){
        StringBuffer buffer = new StringBuffer(url);
        if(params!=null&&params.size()>0){
            buffer.append("?");
            for(Entry<String, String> entry:params.entrySet()){
                buffer.append(entry.getKey() + "="+ encode(entry.getValue()) + "&");
            }
        }
        LOGGER.debug("Final URL:" + buffer.toString());
        GetMethod method = new GetMethod(buffer.toString());
        try {
            try {
                int code = getSharedHttpClient(soTimeout, connTimeout).executeMethod(method);
                if (code == HttpStatus.SC_OK) {
                    return method.getResponseBodyAsStream();
                }
            } finally {
                method.releaseConnection();
            }
        } catch (Throwable e) {
            LOGGER.warn("http error, url:"+ url, e);
        }
        return null;
    }

    public static String getResponseBodyAsStringByPost(String url, Map<String, String> params, int soTimeout, int connTimeout){
        StringBuffer buffer = new StringBuffer(url);
        PostMethod method = new PostMethod(buffer.toString());
        if(params!=null){
            for(Entry<String, String> entry:params.entrySet()){
                method.addParameter(entry.getKey(), entry.getValue());
            }
        }
        try {
            try {
                int code = getSharedHttpClient(soTimeout, connTimeout).executeMethod(method);
                if (code == HttpStatus.SC_OK) {
                    return method.getResponseBodyAsString();
                }
            } finally {
                method.releaseConnection();
            }
        } catch (Throwable e) {
            LOGGER.warn("http error, url:"+ url, e);
        }
        return null;
    }

    public static String getResponseBodyAsString(String url, int soTimeout, int connTimeout) {
        return getResponseBodyAsStringByGet(url, null, soTimeout, connTimeout);
    }

    public static String getResponseBodyAsString(String url) {
        GetMethod method = new GetMethod(url);
        try {
            try {
                int code = getHttpClient().executeMethod(method);
                if (code == HttpStatus.SC_OK) {
                    return method.getResponseBodyAsString();
                }
            } finally {
                method.releaseConnection();
            }
        } catch (Throwable e) {
            LOGGER.warn("http error, url:"+ url, e);
        }
        return null;
    }

    public static byte[] getResponseBodyAsByte(String url) {
        GetMethod method = new GetMethod(url);
        try {
            try {
                int code = getHttpClient().executeMethod(method);
                if (code == HttpStatus.SC_OK) {
                    return method.getResponseBody();
                }
            } finally {
                method.releaseConnection();
            }
        } catch (Throwable e) {
            LOGGER.warn("http error, url:"+ url, e);
        }
        return null;
    }

    public static byte[] getResponseBodyAsByte(String url, int retryCount) {
        GetMethod method = new GetMethod(url);
        do{
           try {
               try {
                   int code = getHttpClient().executeMethod(method);
                   if (code == HttpStatus.SC_OK) {
                       return method.getResponseBody();
                   }
               } finally {
                   method.releaseConnection();
               }
           } catch (Throwable e) {
               LOGGER.warn("http error, url:"+ url, e);
           }
        }while(--retryCount>0);

        return null;
    }

    public static String getResponseBodyAsString(String url, int retryCount) {
        GetMethod method = new GetMethod(url);
        do{
           try {
               try {
                   int code = getHttpClient().executeMethod(method);
                   if (code == HttpStatus.SC_OK) {
                       return method.getResponseBodyAsString();
                   }
               } finally {
                   method.releaseConnection();
               }
           } catch (Throwable e) {
               LOGGER.warn("http error, url:"+ url, e);
           }
        }while(--retryCount>0);

        return null;
    }

    public static HttpClient getHttpClient(){
        if(httpClient == null){
            httpClient = getNewInstance(5000,2000);
            httpClient.getParams().setContentCharset("utf-8");
        }
        return httpClient;
    }

    /**
     *
     * @param url
     * @param params
     * @return
     */
    public static String getResponseBodyAsStringByGet(String url, Map<String, String> params){
        StringBuffer buffer = new StringBuffer(url+"?");
        if(params!=null){
            for(Entry<String, String> entry:params.entrySet()){
                buffer.append(entry.getKey() + "="+ encode(entry.getValue()) + "&");
            }
        }
        LOGGER.debug("Final URL:" + buffer.toString());
        GetMethod method = new GetMethod(buffer.toString());
        try {
            try {
                int code = getHttpClient().executeMethod(method);
                if (code == HttpStatus.SC_OK) {
                    return method.getResponseBodyAsString();
                }
            } finally {
                method.releaseConnection();
            }
        } catch (Throwable e) {
            LOGGER.warn("http error, url:"+ url, e);
        }
        return null;
    }
    //获取post请求的返回信息，无论是否成功。因为验签失败返回值不是200，下面所有getAllResponse*函数理由相同
    public static String getAllResponseBodyAsStringByGet(String url, Map<String, String> params){
        StringBuffer buffer = new StringBuffer(url+"?");
        if(params!=null){
            for(Entry<String, String> entry:params.entrySet()){
                buffer.append(entry.getKey() + "="+ encode(entry.getValue()) + "&");
            }
        }
        LOGGER.info("Final URL:" + buffer.toString());
        GetMethod method = new GetMethod(buffer.toString());
        try {
            try {
                int code = getHttpClient().executeMethod(method);
                return method.getResponseBodyAsString();
            } finally {
                method.releaseConnection();
            }
        } catch (Throwable e) {
            LOGGER.warn("http error, url:"+ url, e);
        }
        return null;
    }

    /**
     *
     * @param url
     * @param params
     * @return
     */
    public static String getResponseBodyAsString(String url, Map<String, String> params){
        StringBuffer buffer = new StringBuffer(url+"?");
        if(params!=null){
            for(Entry<String, String> entry:params.entrySet()){
                buffer.append(entry.getKey() + "="+ encode(entry.getValue()) + "&");
            }
        }
        LOGGER.info("Final URL:" + buffer.toString());
        PostMethod method = new PostMethod(buffer.toString());
        try {
            try {
                int code = getHttpClient().executeMethod(method);
                if (code == HttpStatus.SC_OK) {
                    return method.getResponseBodyAsString();
                }
            } finally {
                method.releaseConnection();
            }
        } catch (Throwable e) {
            LOGGER.warn("http error, url:"+ url, e);
        }
        return null;
    }

    public static String getResponseBodyAsStringByPost(String url, Map<String, String> params){
        return getResponseBodyAsStringByPost(url, params, 1);
    }

    public static String getResponseBodyAsStringByPost(String url, Map<String, String> params, Map<String, String> headerMap){
        return getResponseBodyAsStringByPost(url, params, 1, headerMap);
    }

    public static String getResponseBodyAsStringByPost(String url, Map<String, String> params,int retryCount){
        return getResponseBodyAsStringByPost(url, params, retryCount, null);
    }
    public static String getResponseBodyAsStringByPost(String url, Map<String, String> params,int retryCount, Map<String, String> headerMap){
        StringBuffer buffer = new StringBuffer(url);
        PostMethod method = new PostMethod(buffer.toString());
        if(headerMap!=null){
            for(Entry<String, String> entry:headerMap.entrySet()){
                method.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        if(params!=null){
            for(Entry<String, String> entry:params.entrySet()){
                method.addParameter(entry.getKey(), entry.getValue());
            }
        }
        do{
            try {
                try {
                    int code = getHttpClient().executeMethod(method);
                    if (code == HttpStatus.SC_OK) {
                        return method.getResponseBodyAsString();
                    } else {
                    	LOGGER.info("code----------------- {}", code);
                    }
                } finally {
                    method.releaseConnection();
                }
            } catch (Throwable e) {
                LOGGER.warn("http error, url:"+ url, e);
            }
        }while(--retryCount>0);
        return null;
    }
    public static String getAllResponseBodyAsStringByPost(String url, Map<String, String> params){
        return getAllResponseBodyAsStringByPost(url, params, 1);
    }
    public static String getAllResponseBodyAsStringByPost(String url, Map<String, String> params,int retryCount){
        StringBuffer buffer = new StringBuffer(url);
        PostMethod method = new PostMethod(buffer.toString());
        if(params!=null){
            for(Entry<String, String> entry:params.entrySet()){
                method.addParameter(entry.getKey(), entry.getValue());
            }
        }
        do{
            try {
                try {
                    int code = getHttpClient().executeMethod(method);
                    return method.getResponseBodyAsString();
                } finally {
                    method.releaseConnection();
                }
            } catch (Throwable e) {
                LOGGER.warn("http error, url:"+ url, e);
            }
        }while(--retryCount>0);
        return null;
    }

    public static String getResponseBodyAsStringByPost(String url, RequestEntity requestEntity){
        return getResponseBodyAsStringByPost(url,requestEntity,1);
    }
    public static String getResponseBodyAsStringByPost(String url, RequestEntity requestEntity, Map<String, String> headerMap){
        return getResponseBodyAsStringByPost(url,requestEntity,1, headerMap);
    }
    public static String getResponseBodyAsStringByPost(String url, RequestEntity requestEntity,int retryCount){
        return getResponseBodyAsStringByPost(url,requestEntity,1, null);
    }
    public static String getResponseBodyAsStringByPost(String url, RequestEntity requestEntity,int retryCount, Map<String, String> headerMap){
        StringBuffer buffer = new StringBuffer(url);
        PostMethod method = new PostMethod(buffer.toString());
        if(headerMap!=null){
            for(Entry<String, String> entry:headerMap.entrySet()){
                method.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        if(requestEntity!=null){
            method.setRequestEntity(requestEntity);
        }
        do{
            try {
                try {
                    int code = getHttpClient().executeMethod(method);
                    if (code == HttpStatus.SC_OK) {
                        return method.getResponseBodyAsString();
                    }
                } finally {
                    method.releaseConnection();
                }
            } catch (Throwable e) {
                LOGGER.warn("http error, url:"+ url, e);
            }
        }while(--retryCount>0);
        return null;
    }
    protected static final int MAX_CONNECTION = 640;

    protected static final int TIMEOUT_CONNECTION = 10000;

    protected static final int TIMEOUT_SOCKET = 5000;

    protected static final int MAX_CONTENT_SIZE = 8 * 1024 * 1024;

    protected static final int MAX_CONNECTION_PER_IP = 64;

    public static HttpClient getNewInstance() {
        return getNewInstance(TIMEOUT_SOCKET, TIMEOUT_CONNECTION);
    }

    public static HttpClient getNewInstance(int soTimeout, int connTimeout) {
        return getNewInstance(soTimeout, connTimeout, MAX_CONNECTION,
                MAX_CONNECTION_PER_IP, MAX_CONTENT_SIZE);
    }

    public static HttpClient getNewInstance(int soTimeout, int connTimeout,
            int maxConn, int maxConnPerIp, int maxContentSize) {
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setMaxTotalConnections(maxConn);
        connectionManager.getParams().setConnectionTimeout(connTimeout);
        connectionManager.getParams().setSoTimeout(soTimeout);
        connectionManager.getParams().setSendBufferSize(maxContentSize);
        connectionManager.getParams().setReceiveBufferSize(maxContentSize);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnPerIp);
        return new HttpClient(connectionManager);
    }
    public static String encode(String value){
        try {
            return URLEncoder.encode(value,"utf-8");
        } catch (Exception e) {
            LOGGER.warn("", e);
        }
        return value;
    }
    public static void main(String[] args) {
        //http://api.dianping.com/v1/business/find_businesses?appkey=70762911&sign=48CF3CAEA93ED7BAE1F4A128D6E4BF9F03F68A1A&platform=2&keyword=&format=json&city=天津
        /*
        Map<String,String> paramMap= new HashMap<String,String>();
        paramMap.put("city", "天津");
        paramMap.put("format", "json");
        paramMap.put("keyword", "70762911");
        paramMap.put("platform", "2");
        String queryS = "http://api.dianping.com/v1/business/find_businesses?" + CommonUtils.getSignedUri(paramMap, "70762911", "721749fc342a4c978c3f5907d341d341");
        System.out.println(queryS);
        System.out.println(getResponseBodyAsString(queryS));*/
        Map<String,String> paramMap= new HashMap<String,String>();
        paramMap.put("errortype", "xFeedback");
        paramMap.put("shopid", "");
        paramMap.put("refid", "00002c2f096f4029b496c8d6abd6c3a1");
        paramMap.put("apikey", "YXBpa2V5PU5UQmhPR1psWlRobU5XVXhZ");
        paramMap.put("uid", "asiR6j_4BYD4k_d1");
        paramMap.put("curtel", "没有电话");
        paramMap.put("moreinfo", "有问题啊，调用返回的怎么是个空字符串!");
        paramMap.put("contact_tel", "没有电话");
        String res = getResponseBodyAsString("http://apis.dianhua.cn/correction/", paramMap);
        System.out.println("[" + res + "]");
    }

    public static String getResponseBodyAsStringByGet(String url, int retryCount, Map<String, String> headerMap){
        StringBuffer buffer = new StringBuffer(url);
        GetMethod method = new GetMethod(buffer.toString());
        if(headerMap!=null){
            for(Entry<String, String> entry : headerMap.entrySet()){
                method.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        do{
            try {
                try {
                    int code = getHttpClient().executeMethod(method);
                    if (code == HttpStatus.SC_OK) {
                        return method.getResponseBodyAsString();
                    }
                } finally {
                    method.releaseConnection();
                }
            } catch (Throwable e) {
                LOGGER.warn("http error, url:"+ url, e);
            }
        }while(--retryCount>0);
        return null;
    }
}

