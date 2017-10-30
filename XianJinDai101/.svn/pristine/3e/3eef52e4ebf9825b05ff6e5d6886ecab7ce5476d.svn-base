package com.ryx.payment.ryxhttp.cookie;


import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import com.ryx.payment.ryxhttp.cookie.store.CookieStore;
import com.ryx.payment.ryxhttp.cookie.store.HasCookieStore;
import com.ryx.payment.ryxhttp.utils.Exceptions;

/**
 * Created by laomao on 16/4/21.
 */
public class CookieJarImpl implements CookieJar, HasCookieStore
{
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore)
    {
        if (cookieStore == null) Exceptions.illegalArgument("cookieStore can not be null.");
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        cookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url)
    {
        return cookieStore.get(url);
    }

    @Override
    public CookieStore getCookieStore()
    {
        return cookieStore;
    }
}
