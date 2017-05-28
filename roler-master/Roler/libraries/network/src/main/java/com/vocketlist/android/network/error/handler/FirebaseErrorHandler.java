package com.vocketlist.android.network.error.handler;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import rx.Observable;

/**
 * Created by SeungTaek.Lim on 2017. 2. 22..
 */

public class FirebaseErrorHandler extends ErrorHandler {
    @Override
    public Observable call(Throwable throwable) {
        FirebaseCrash.logcat(Log.ERROR, "API", throwable.toString());
        FirebaseCrash.report(throwable);

        return Observable.error(throwable);
    }
}
