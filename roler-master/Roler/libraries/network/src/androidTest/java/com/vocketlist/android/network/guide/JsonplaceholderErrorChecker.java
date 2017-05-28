package com.vocketlist.android.network.guide;

import com.vocketlist.android.network.service.ErrorChecker;

import java.util.List;

/**
 * Created by lsit on 2017. 1. 30..
 */

public class JsonplaceholderErrorChecker implements ErrorChecker<List<User>> {
    @Override
    public void checkError(List<User> data) throws RuntimeException {
        if (data == null) {
            throw new JsonplaceholderError();
        }
    }
}
