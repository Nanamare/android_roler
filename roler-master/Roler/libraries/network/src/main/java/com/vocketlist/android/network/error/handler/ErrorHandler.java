package com.vocketlist.android.network.error.handler;

import okhttp3.HttpUrl;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by SeungTaek.Lim on 2016. 11. 22..
 */

public abstract class ErrorHandler implements Func1<Throwable, Observable> {
    protected Retrofit mRetrofit;
    protected CallAdapter<?> mWrapped;
    protected HttpUrl mRequestUrl;

    public final void set(Retrofit retrofit, HttpUrl url, CallAdapter<?> wrapped) {
        mRetrofit = retrofit;
        mRequestUrl = url;
        mWrapped = wrapped;
    }
}
