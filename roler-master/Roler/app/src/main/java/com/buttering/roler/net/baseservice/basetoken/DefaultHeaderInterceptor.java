package com.buttering.roler.net.baseservice.basetoken;

import android.text.TextUtils;


import com.buttering.roler.util.SharePrefUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nanamare 2017. 2. 25..
 */
public class DefaultHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = SharePrefUtil.getSharedPreference("accessToken");

        Request.Builder requestBuilder = original.newBuilder();

        if (TextUtils.isEmpty(token) == false) {
                requestBuilder.header("access_token", token);
        }

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
