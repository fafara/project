package com.ryx.ryxcredit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.ryx.payment.ryxhttp.OkHttpUtils;
import com.ryx.payment.ryxhttp.callback.FileCallBack;
import com.ryx.payment.ryxhttp.callback.StringCallback;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.services.UICallBack;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import okhttp3.Call;

/**
 * Created by laomao on 16/6/21.
 */
public class HttpUtil {
    private static HttpUtil instance = null;
    private static Context mContext;
    private String SitTestCode = "0";

    /**
     * 获取http请求实例
     *
     * @return
     */
    public static HttpUtil getInstance(Context context) {
        mContext = context;
        if (null == instance) {
            instance = new HttpUtil();
        }
        return instance;
    }

    private HttpUtil() {
    }

    /**
     * 判断网络是否链接
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 封装网络请求
     *
     * @param t
     * @param reqAction
     * @param clazz
     * @param callback
     * @param <T>
     * @param <E>
     */
    public <T, E> void httpsPost(T t, final ReqAction reqAction, final Class<E> clazz, final ICallback<E> callback,
                                 final UICallBack uiCallBack) {
        String params = CJSONUtils.getInstance().toJSONString(t);
        CLogUtil.showLog("xd", "原始参数for小贷:" + params);
        actionBaseRequest(params, reqAction, new ICallback<String>() {
            @Override
            public void success(String s) {
                uiCallBack.complete();
                JSONObject jObj = JSONObject.parseObject(s);
                Integer code = jObj.getInteger("code");
                if (code / 1000 == 2) {
                    E response = CJSONUtils.getInstance().parseJSONObject(s, clazz);
                    callback.success(response);
                } else {
                    callback.failture(jObj.getString("message"));
                }
            }

            @Override
            public void failture(String tips) {
                uiCallBack.complete();
                callback.failture(tips);
            }
        });
    }

    private void actionBaseRequest(String requestParams, final ReqAction reqAction, final ICallback<String> callback) {
        CbaseRequest cbaseRequest = new CbaseRequest();
        try {
            requestParams = CommonUtils.byte2Str(requestParams.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cbaseRequest.setParams(requestParams);
        String phoneNo = RyxcreditConfig.getPhoneNo();
        if (TextUtils.isEmpty(phoneNo)) {
            callback.failture("phoneNo参数异常");
            return;
        }
        String appUser = RyxcreditConfig.getAppuser();
        if (TextUtils.isEmpty(appUser)) {
            callback.failture("appUser参数异常");
            return;
        }
        String token = RyxcreditConfig.getToken();
        if (TextUtils.isEmpty(token)) {
            callback.failture("token参数异常");
            return;
        }
        String systemVersion = "android" + android.os.Build.VERSION.RELEASE;
        if (TextUtils.isEmpty(systemVersion)) {
            callback.failture("systemVersion参数异常");
            return;
        }
        String appVersion = RyxcreditConfig.getAppVersion();
        if (TextUtils.isEmpty(appVersion)) {
            callback.failture("appVersion参数异常");
            return;
        }

        if (TextUtils.isEmpty(SitTestCode)) {
            callback.failture("SitTestCode参数异常");
            return;
        }
        cbaseRequest.setPhone(phoneNo);
        cbaseRequest.setAppUser(appUser);
        cbaseRequest.setToken(token);
        cbaseRequest.setTransTime(CDateUtil.DateToStr(new Date()));
        cbaseRequest.setSystemVersion(systemVersion);
        cbaseRequest.setAppVersion(appVersion);
        cbaseRequest.setUrlType(SitTestCode);
        final long transLogNo = CPreferenceUtil.getInstance().getLong(CConstants.C_TRANS_LOG_NO, 1);
        cbaseRequest.setTransLogNo(String.format("%06d", transLogNo));
        String sign = CommonUtils.getSign(phoneNo, requestParams, token, appUser, transLogNo);
        cbaseRequest.setSign(sign);
        String url = RyxcreditConfig.getBaseUrl() + reqAction.getContent();
        String postString = CJSONUtils.getInstance().toJSONString(cbaseRequest);
        CLogUtil.showLog("xd", "网络请求url======:" + url);
        CLogUtil.showLog("xd", "请求参数======" + postString);
//        RyxCreditLoadDialog.getInstance(mContext).show();
        OkHttpUtils.postString().url(url).content(postString).tag(reqAction.getName()).build()
                .connTimeOut(45000).readTimeOut(45000).writeTimeOut(45000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
//                        RyxCreditLoadDialog.getInstance(mContext).dismiss();
                CLogUtil.showLog("error:" + e.getMessage());
                callback.failture("系统内部错误");
            }

            @Override
            public void onResponse(String response) {
//                        RyxCreditLoadDialog.getInstance(mContext).dismiss();
                CPreferenceUtil.getInstance().saveLong(CConstants.C_TRANS_LOG_NO, transLogNo + 1);
                CLogUtil.showLog("xd", "响应=====" + response);
                JSONObject jObj = JSONObject.parseObject(response);
                if ("0000".equals(jObj.getString("code"))) {
                    callback.success(jObj.getString("result"));
                } else if ("5040".equals(jObj.getString("code"))) {
                    try {
                        RyxcreditConfig.clearParams();
                        CLogUtil.showToast(mContext, "为保证账户安全，请你重新登录！");
                        Intent intent = new Intent(mContext,
                                Class.forName(mContext.getApplicationContext().getPackageName()+".usercenter.LoginActivity_"));
                        //清空activity栈
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //添加token失效的标示
                        intent.putExtra("tokenIntent", true);
                        mContext.startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    callback.failture(jObj.getString("msg"));
                }
            }
        });

    }
//    public void httpsPost(String tag,String queryString,Callback callback)
//    {
//        String host = RyxcreditConfig.getBaseUrl();
//        OkHttpUtils.postString().url(host).content(queryString).tag(tag).build().execute(callback);
//    }

    /**
     * 文件下载网络请求
     *
     * @param tag
     * @param queryString
     * @param callback
     */
    public void httpsFilePost(String hostUrl, String tag, String queryString, FileCallBack callback) {
        OkHttpUtils.get().url(hostUrl).build().execute(callback);
    }

    /**
     * 取消网络请求
     *
     * @param tag
     */
    public void canelHttpsPost(String tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }


}
