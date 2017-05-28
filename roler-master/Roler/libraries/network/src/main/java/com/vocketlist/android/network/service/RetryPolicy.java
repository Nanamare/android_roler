package com.vocketlist.android.network.service;

import com.vocketlist.android.network.error.ExceptionHelper;
import com.vocketlist.android.roboguice.log.Ln;

import rx.functions.Func2;

/**
 * Created by SeungTaek.Lim on 2016. 12. 1..
 */

public class RetryPolicy implements Func2<Integer, Throwable, Boolean> {
    @Override
    public Boolean call(Integer count, Throwable throwable) {
        Ln.d("RetryPolicy count = " + count);
        if (count >= 3) {
            return false;
        }

        if (ExceptionHelper.isNetworkError(throwable)) {
            // 네트워크 관련 오류인 경우에만 retry를 허용한다
            return true;
        }

        return false;
    }
}
