package com.ryx.ryxpay;


import android.app.Application;

import java.util.concurrent.TimeUnit;

import com.ryx.payment.ryxhttp.OkHttpUtils;

/**
 * Created by laomao on 16/4/19.
 */
public class RyxApplicatioin extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttp();
    }

    private void initOkHttp()
    {

        OkHttpUtils.getInstance().debug("OkHttpUtils").setConnectTimeout(100000, TimeUnit.MILLISECONDS);
        OkHttpUtils.getInstance().setCertificates();

    }
}

