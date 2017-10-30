package com.ryx.ryxcredit.xjd;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.widget.RyxCreditLoadDialog;
import com.ryx.ryxcredit.widget.RyxCreditLoadDialogBuilder;

/**
 * Created by DIY on 2016/9/9.
 * 公共H5界面处理
 */
public class CommonH5Activity extends  BaseActivity {
    //社保
    private String someString = "https://open.shujumohe.com/box/result/0/112/%E8%B4%A6%E5%8F%B7%E6%88%96%E5%AF%86%E7%A0%81%E9%94%99%E8%AF%AF?box_token=14F98932A59D4802AA7A17B7920B44CF&cb=https:www.baidu.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_credit_common_h5);
         Bundle datas= getIntent().getExtras();
        setTitleLayout(datas.getString("title"),true,false);
        WebView wv=(WebView) findViewById(R.id.c_credit_common_pwv);
        wv.loadUrl(datas.getString("url_address"));
        RYXCommonWebChromeClient ryxCommonWebChromeClient= new RYXCommonWebChromeClient();
        wv.setWebChromeClient(ryxCommonWebChromeClient);
        RYXCommonWebViewClient ryxCommonWebViewClient=  new RYXCommonWebViewClient();

        wv.setWebViewClient(ryxCommonWebViewClient);


        //下载调用第三方
//        wv.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            }
//        });
        initSetings(wv);
    }

    private void initSetings(WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    private class RYXCommonWebChromeClient extends WebChromeClient{

    }
    RyxCreditLoadDialogBuilder loading =null;
    private  class RYXCommonWebViewClient extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loading = new RyxCreditLoadDialog().getInstance(CommonH5Activity.this);
            loading.setMessage("加载中......");
            loading.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(loading!=null)
                loading.dismiss();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

/*        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }*/
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //webView尝试访问链接时，会被此处拦截处理
            if(someString.startsWith("https")){
                // 拦截后做处理
                CLogUtil.showToast(getApplicationContext(),"完毕");
                return true;
            }
            CLogUtil.showToast(getApplicationContext(),"为完毕");
            return false;
        }

    }
}
