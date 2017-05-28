package com.vocketlist.android.network.error;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by SeungTaek.Lim on 2016. 12. 23..
 */

public final class ExceptionHelper {
    public static boolean isNetworkError(Throwable throwable) {
        if ((throwable instanceof RetrofitException) == false) {
            return false;
        }

        RetrofitException exception = (RetrofitException) throwable;
        Throwable cause = exception.getCause();

        if (cause instanceof HttpException
                || cause instanceof IOException) {
            return true;
        }

        return false;
    }

    public static String getFirstErrorMessage(Throwable throwable) {
        Throwable cause = throwable;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause.getMessage();
    }

    public static <T> T getCause(Throwable throwable) throws ClassCastException {
        Throwable cause = throwable.getCause();

        if (cause == null) {
            cause = throwable;
        }

        return (T) cause;
    }
}
