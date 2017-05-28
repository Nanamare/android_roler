package com.vocketlist.android.network.guide;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lsit on 2017. 1. 30..
 */
class User {
    @SerializedName("id") public int id;
    @SerializedName("name") public String name;
    @SerializedName("email") public String email;
    // address
    @SerializedName("phone") public String phone;
    @SerializedName("website") public String website;
    // company
}
