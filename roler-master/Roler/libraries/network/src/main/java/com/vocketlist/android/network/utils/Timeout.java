package com.vocketlist.android.network.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by lsit on 2017. 1. 30..
 */

public final class Timeout {
    public static final TimeUnit UNIT = TimeUnit.MILLISECONDS;

    private static int connectionTimeout = 10000;
    private static int readTimeout = 3000;

    public static int getConnectionTimeout() {
        return Timeout.connectionTimeout;
    }

    public static int getReadTimeout() {
        return Timeout.readTimeout;
    }

    public static void setConnectionTimeout(int time) {
        Timeout.connectionTimeout = time;
    }

    public static void setReadTimeout(int time) {
        Timeout.readTimeout = time;
    }
}
