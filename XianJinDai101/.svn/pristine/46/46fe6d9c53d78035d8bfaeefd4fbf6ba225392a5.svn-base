package com.ryx.payment.ruishua.widget;


import android.content.Context;

import com.ryx.payment.ruishua.utils.LogUtil;

/**
 * 全局异常捕获，暂时未用于项目,防止友盟统计有误
 * Created by XCC on 2017/1/9.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.showLog(""+ex.getMessage());
    }
}
