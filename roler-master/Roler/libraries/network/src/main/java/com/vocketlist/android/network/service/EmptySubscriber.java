package com.vocketlist.android.network.service;

import com.vocketlist.android.network.error.ExceptionHelper;
import com.vocketlist.android.roboguice.log.Ln;

import rx.Subscriber;

/**
 * Created by SeungTaek.Lim on 2017. 1. 5..
 */

public class EmptySubscriber<Result> extends Subscriber<Result> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Ln.e(e, "onError : " + ExceptionHelper.getFirstErrorMessage(e));
    }

    @Override
    public void onNext(Result result) {

    }
}
