package com.vocketlist.android.network.service;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by SeungTaek Lim
 */
public class WebkitCookieJar implements CookieJar {
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies == null || cookies.isEmpty()) {
            return;
        }

        for (Cookie cookie : cookies) {
            CookieManager.getInstance().setCookie(url.host(), cookie.toString());
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookieList = new ArrayList<>();

        String cookieString = CookieManager.getInstance().getCookie(url.host());
        if (TextUtils.isEmpty(cookieString)) {
            return cookieList;
        }

        String[] cookies = cookieString.split(";");
        if (cookies == null) {
            return cookieList;
        }

        for (String cookie : cookies) {
            Cookie cookieClass = Cookie.parse(url, cookie);
            if (cookieClass == null) {
                continue;
            }
            cookieList.add(cookieClass);
        }

        return cookieList;
    }

}
