package com.snoopy.scancode.util;

import android.content.SharedPreferences;
import android.util.Log;
import com.snoopy.scancode.enumm.RequestTypeEnum;
import com.snoopy.scancode.exception.ServerException;
import com.snoopy.scancode.listener.HttpCallbackListener;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @Author: snoopy
 * @Date: 2018/10/22 15:13
 */

public class HttpUtil {
    private OkHttpClient client;//所有请求都由同一个client进行，减少资源消耗
    private String token;
    private SharedPreferences sp;//加入持久化，可以对一些特殊变量进行保存，比如用户id、用于登录验证的token等
    private SharedPreferences.Editor editor;

    private HttpUtil() {
        client = new OkHttpClient();
//        sp = ContextApplication.getContext()
//                .getSharedPreferences("login_data", Context.MODE_PRIVATE);
//        editor = sp.edit();
//        token = sp.getString("token", "");
    }

    private static HttpUtil httpUtil = null;

    //单例模式

    public static HttpUtil getInstance() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    public void sendRequestWithCallback(final RequestTypeEnum method, final String address, final RequestBody body, final HttpCallbackListener listener
    ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder()
                        .url(address);
                switch (method) {
                    case POST:
                        builder.post(body);
                        break;
                    case PUT:
                        builder.put(body);
                        break;
                    case DELETE:
                        builder.delete(body);
                        break;
                    default:
                        builder.get();
                        break;
                }

                Request request = builder.build();
                try {
                    //实际进行请求的代码
                    Log.i("url = ", address);
                    Response response = client.newCall(request).execute();
                    token = response.header("token");
                    if (token == null) {
                        token = "b06804b910ea4f96a714a84d686d8583";
                        Log.i("", "没有token使用默认token");
                    } else {
                        Log.i("", "收到token：" + token);
                    }


                    String result = response.body().string();
                    if (result != null && listener != null) {
                        //当response的code大于200，小于300时，视作请求成功
                        if (response.isSuccessful()) {
                            listener.onFinish(result);
                        } else {
                            listener.onError(new ServerException(result));
                        }
                    }

                } catch (IOException e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }).start();

    }

    public void post(final String address, RequestBody body, final HttpCallbackListener listener) {
        sendRequestWithCallback(RequestTypeEnum.POST, address, body, listener);
    }

    public void get(String address, HttpCallbackListener listener) {
        sendRequestWithCallback(RequestTypeEnum.GET, address, null, listener);
    }

    public void delete(String address, RequestBody body, HttpCallbackListener listener) {
        sendRequestWithCallback(RequestTypeEnum.DELETE, address, body, listener);
    }

    public void put(String address, RequestBody body, HttpCallbackListener listener) {
        sendRequestWithCallback(RequestTypeEnum.PUT, address, body, listener);
    }


}
