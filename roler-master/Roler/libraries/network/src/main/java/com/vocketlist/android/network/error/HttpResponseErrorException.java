package com.vocketlist.android.network.error;

import java.io.IOException;

/**
 * Created by SeungTaek.Lim on 2017. 3. 4..
 */

public class HttpResponseErrorException extends IOException {
    public HttpResponseErrorException(String msg) {
        super(msg);
    }
}
