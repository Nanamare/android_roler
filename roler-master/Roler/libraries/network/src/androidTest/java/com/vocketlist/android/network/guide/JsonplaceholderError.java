package com.vocketlist.android.network.guide;

import java.util.List;

/**
 * Created by lsit on 2017. 1. 30..
 */

class JsonplaceholderError extends RuntimeException {
    public JsonplaceholderError() {
        super();
    }

    public JsonplaceholderError(List<User> userList) {
        super("JsonplaceholderError");
    }
}
