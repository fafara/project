package com.ryx.payment.ryxhttp.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * Created by laomao on 16/4/21.
 */
public abstract class BitmapCallback extends Callback<Bitmap>
{
    @Override
    public Bitmap parseNetworkResponse(Response response) throws Exception
    {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

}