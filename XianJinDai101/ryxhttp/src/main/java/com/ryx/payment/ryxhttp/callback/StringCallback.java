package com.ryx.payment.ryxhttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by laomao on 16/4/21.
 */
public abstract class StringCallback extends Callback<String>
{
    @Override
    public String parseNetworkResponse(Response response) throws IOException
    {
        return response.body().string();
    }

}
