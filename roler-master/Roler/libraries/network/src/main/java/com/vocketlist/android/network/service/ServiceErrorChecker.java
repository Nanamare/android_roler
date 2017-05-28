package com.vocketlist.android.network.service;

import com.vocketlist.android.network.error.RetrofitException;
import com.vocketlist.android.network.error.handler.FirebaseErrorHandler;
import com.vocketlist.android.roboguice.log.Ln;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by SeungTaek.Lim on 2016. 11. 17..
 */

public final class ServiceErrorChecker<T> implements Observable.Operator<Response<T>, Response<T>> {
    private static final String TAG = ServiceErrorChecker.class.getSimpleName();
    private final ErrorChecker<T> mErrorChecker;

    public ServiceErrorChecker(ErrorChecker<T> errorChecker) {
        mErrorChecker = errorChecker;
    }

    @Override
    public Subscriber<? super Response<T>> call(final Subscriber<? super Response<T>> subscriber) {
        return new Subscriber<Response<T>>() {
            @Override
            public void onCompleted() {
                if (checkUnsubscriber(subscriber)) {
                    return;
                }

                Ln.d(TAG, "onCompleted()");
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (checkUnsubscriber(subscriber)) {
                    return;
                }

                if (e instanceof RetrofitException) {
                    RetrofitException exception = ((RetrofitException) e);
                    Ln.e(TAG, "onError() >> ServiceErrorChecker url : " + exception.getUrl());
                    Ln.e(TAG, "onError() >> ServiceErrorChecker Cause : " + e.getCause().toString(), e.getCause());
                }

                Ln.d(TAG, "onError() : " + e.toString());
                subscriber.onError(e);
            }

            @Override
            public void onNext(Response<T> response) {
                if (checkUnsubscriber(subscriber)) {
                    return;
                }

                if (mErrorChecker == null) {
                    subscriber.onNext(response);
                    return;
                }

                try {
                    mErrorChecker.checkError(response.body());
                } catch (RuntimeException e) {
                    RetrofitException exception = RetrofitException.unexpectedError(response.raw().networkResponse().request().url(), response, e);
                    new FirebaseErrorHandler().call(exception);
                    Ln.e(e, "exception");

                    onError(exception);
                    return;
                }

                Ln.d(TAG, "onNext");
                subscriber.onNext(response);
                return;
            }
        };
    }

    protected boolean checkUnsubscriber(Subscriber<?> subscriber) {
        if (subscriber.isUnsubscribed()) {
            return true;
        }

        return false;
    }
}
