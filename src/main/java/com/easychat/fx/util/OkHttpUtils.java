package com.easychat.fx.util;

import com.easychat.fx.controller.Cache;
import okhttp3.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: Zed
 * date: 2019/08/26.
 * description:
 */
public class OkHttpUtils {
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(20,
                    5*60, TimeUnit.SECONDS))
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    /**
     * 根据map获取get请求参数
     * @param queries 参数集合
     * @return 组装参数后的url
     */
    private static String getQueryString(String url, Map<String,String> queries){
        StringBuilder sb = new StringBuilder(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry<String,String> entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 调用okhttp的newCall方法
     * @param request 请求
     * @return 调用返回的json
     */
    private static String execNewCall(Request request){
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
            System.out.println("网络调用繁忙");
        } catch (Exception e) {
            System.out.println("网络调用繁忙");
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * get method
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return json字符串
     */
    public static String get(String url, Map<String, String> queries) {
        url = getQueryString(url,queries);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("token", Cache.currentUser.getToken());
        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(Headers.of(headerMap))
                .build();
        return execNewCall(request);
    }


    /**
     * post以map传递参数
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public static String postFormParams(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("token", Cache.currentUser.getToken());
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .headers(Headers.of(headerMap))
                .build();
        return execNewCall(request);
    }


    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * @param url 请求的url
     * @param jsonParams JSON字符串的参数
     */
    public static String postJsonParams(String url, String jsonParams) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
    }
    
    
}
