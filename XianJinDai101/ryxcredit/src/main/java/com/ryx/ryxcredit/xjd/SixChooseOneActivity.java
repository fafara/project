package com.ryx.ryxcredit.xjd;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.utils.CLogUtil;

public class SixChooseOneActivity extends BaseActivity {
    private String OCTOPUS_URL_STR = "http://taskid";
    //社保
    private String fundFnformationCertificationUrl ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six_choose_one);
        Bundle datas= getIntent().getExtras();
        setTitleLayout(datas.getString("title"),true,false);
        WebView wv=(WebView) findViewById(R.id.c_credit_common_pwv);
        fundFnformationCertificationUrl = datas.getString("url_address");
        wv.loadUrl(fundFnformationCertificationUrl);
        initSetings(wv);
        wv.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("onPageStarted", "onPageStarted" + url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(fundFnformationCertificationUrl!= null && fundFnformationCertificationUrl.endsWith(OCTOPUS_URL_STR)){
                    // 拦截后做处理
                    CLogUtil.showToast(getApplicationContext(),"拦截成功");
                    Log.i("taowuhua","============hahah"+url);

                    return false;
                }else
                    CLogUtil.showToast(getApplicationContext(), "拦截失败");
                    Log.i("taowuhua", "============hahah" + url);
                    return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    private void initSetings(WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
         wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

}
