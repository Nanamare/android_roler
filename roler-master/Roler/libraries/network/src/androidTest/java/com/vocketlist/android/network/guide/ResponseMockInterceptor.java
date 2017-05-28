package com.vocketlist.android.network.guide;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by SeungTaek.Lim on 2016. 11. 24..
 */

public class ResponseMockInterceptor implements Interceptor {
    // Mock RESPONSES.
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        String responseString = "{\"version\":\"1.0.0\"}";

        response = new Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();

        return response;
    }
}