package com.vocketlist.android.network.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LazyMockInterceptor implements Interceptor {
    public String mBody = null;

    public void setResponse(String body) {
        mBody = body;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (mBody == null) {
            Request request = chain.request();
            return chain.proceed(request);
        }

        return new Response.Builder()
                .code(200)
                .message(mBody)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), mBody.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
    }
}