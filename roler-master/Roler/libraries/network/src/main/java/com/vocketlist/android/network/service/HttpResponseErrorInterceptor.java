package com.vocketlist.android.network.service;

import com.vocketlist.android.network.error.HttpResponseErrorException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public class HttpResponseErrorInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        okhttp3.Response response = chain.proceed(request);

        int responseCode = response.code();

        if (responseCode >= 400) {
            ResponseBody body = response.body();

            throw new HttpResponseErrorException("response code = " + responseCode
                    + "\nbody : " + ((body != null) ? body.toString() : ""));
        }

        return response;
    }
}
