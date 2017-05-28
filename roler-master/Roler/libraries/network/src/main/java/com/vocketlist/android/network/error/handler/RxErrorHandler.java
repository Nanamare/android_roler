package com.vocketlist.android.network.error.handler;

import com.vocketlist.android.network.error.RetrofitException;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2016. 11. 22..
 */

public class RxErrorHandler extends ErrorHandler {
    private static final String TAG = RxErrorHandler.class.getSimpleName();

    @Override
    public Observable call(Throwable throwable) {
        return Observable.error(asRetrofitException(throwable));
    }

    private Throwable asRetrofitException(Throwable throwable) {
        // We had non-200 http error
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            Response response = httpException.response();
            return RetrofitException.httpError(response.raw().request().url(), response, httpException, mRetrofit);
        }
        // A network error happened
        if (throwable instanceof IOException) {
            return RetrofitException.networkError(mRequestUrl, (IOException) throwable, mRetrofit);
        }

        return RetrofitException.unexpectedError(mRequestUrl, null, throwable);
    }
}
