package com.ryx.payment.ryxhttp.builder;

import java.util.Map;

import com.ryx.payment.ryxhttp.request.RequestCall;

/**
 * Created by laomao on 16/4/21.
 */
public abstract class OkHttpRequestBuilder
{
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;

    public abstract OkHttpRequestBuilder url(String url);

    public abstract OkHttpRequestBuilder tag(Object tag);

    public abstract OkHttpRequestBuilder headers(Map<String, String> headers);

    public abstract OkHttpRequestBuilder addHeader(String key, String val);

    public abstract RequestCall build();


}