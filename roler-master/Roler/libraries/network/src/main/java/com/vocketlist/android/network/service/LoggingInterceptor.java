package com.vocketlist.android.network.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.vocketlist.android.roboguice.log.Ln;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Debug 모드에서만 HttpLoggingInterceptor가 사용되도록 처리.
 * Created by SeungTaek.Lim on 2016. 12. 6..
 */

public final class LoggingInterceptor implements Interceptor {
    private Interceptor mInterceptor;

    public LoggingInterceptor(Context context) {
        boolean isDebugMode = false;

        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            isDebugMode = ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        } catch (PackageManager.NameNotFoundException e) {
            Ln.e(e, e.toString());
            return;
        }


        if (isDebugMode) {
            mInterceptor = new HttpLoggingInterceptor(new HttpLogger()).setLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (mInterceptor == null) {
            Request request = chain.request();
            return chain.proceed(request);
        }

        return mInterceptor.intercept(chain);
    }
}
