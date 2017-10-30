package com.ryx.payment.ryxhttp.builder;

import java.util.Map;

/**
 * Created by laomao on 16/4/21.
 */
public interface HasParamsable
{
    public abstract OkHttpRequestBuilder params(Map<String, String> params);

    public abstract OkHttpRequestBuilder addParams(String key, String val);

}
