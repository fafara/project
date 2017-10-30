package com.ryx.payment.ryxhttp.builder;

import com.ryx.payment.ryxhttp.OkHttpUtils;
import com.ryx.payment.ryxhttp.request.OtherRequest;
import com.ryx.payment.ryxhttp.request.RequestCall;

/**
 * Created by laomao on 16/4/21.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}