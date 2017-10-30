package com.ryx.payment.ryxhttp.utils;

import android.util.Log;

/**
 * Created by laomao on 16/4/21.
 */
public class L
{
    private static boolean debug = false;

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e("OkHttp", msg);
        }
    }

}

