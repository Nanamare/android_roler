package com.vocketlist.android.network.guide;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by lsit on 2017. 1. 30..
 */

interface JsonplaceholderService {
    @GET("users")
    Observable<Response<List<User>>> getUsers();
}
