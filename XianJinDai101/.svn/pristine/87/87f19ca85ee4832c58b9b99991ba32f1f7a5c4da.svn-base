package com.ryx.payment.ruishua.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;

/**
 *  Html信息展示（因kotlin页面无法跳转AA注解页面,顾重写HtmlMessageActivity,功能和HtmlMessageActivity一致）
 */
public class HtmlMsgKotlinMiddleWareAct extends BaseActivity {
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_message);
        webview=(WebView)findViewById(R.id.webview);
        afterInitView();
    }
    public void afterInitView() {
        try {
            String ccurl = (String) getIntent().getExtras().get("ccurl");
//            ccurl="http://www.baidu.com";
//            LogUtil.showToast(HtmlMessageActivity.this,ccurl);
            final String title = (String) getIntent().getExtras().get("title");
            String urlkey = (String) getIntent().getExtras().get("urlkey");
            String mytitle="";
            if(TextUtils.isEmpty(title)){
                mytitle= RyxAppdata.getInstance(this).getCurrentBranchName();
            }else{
                mytitle=title;
            }
            setTitleLayout(mytitle, true, false);
            WebSettings wSet = webview.getSettings();
            wSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            wSet.setJavaScriptEnabled(true);
            wSet.setDomStorageEnabled(true);
            if (getIntent().getExtras().containsKey("webPage")) {
                boolean isWebPage = (boolean) getIntent().getExtras().get("webPage");
                if (isWebPage)//如果是访问外部页面，则增加网页缩放功能，适配手机屏幕
                    supportZoom(wSet);
            }
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    showLoading("努力加载中，请耐心等待...");
                    super.onPageStarted(view, url, favicon);
                }

                public void onReceivedError(final WebView view, int errorCode, String description, final String failUrl) {
                    LogUtil.showToast(HtmlMsgKotlinMiddleWareAct.this, description);
                    htmlNetworkFail(HtmlMsgKotlinMiddleWareAct.this, view, new CompleteResultListen() {
                        @Override
                        public void compleResultok() {
                            view.loadUrl(failUrl);
                        }
                    });
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    cancleLoading();
                    if(TextUtils.isEmpty(title)){
                        setTitleLayout(view.getTitle(), true, false);
                    }
                }
            });
            String url = PreferenceUtil.getInstance(HtmlMsgKotlinMiddleWareAct.this).getString(urlkey, "");
            LogUtil.showLog("htmlKey===" + urlkey + ",url==" + url+",ccurl=="+ccurl);
            if (TextUtils.isEmpty(ccurl)){
                webview.loadUrl(url);
            }
            else{
                webview.loadUrl(ccurl);
            }
        } catch (Exception e) {
            LogUtil.showToast(HtmlMsgKotlinMiddleWareAct.this, "数据异常！！！");
        }
    }
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void supportZoom(WebSettings webSettings) {
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSettings.setUseWideViewPort(true);
        //设置默认加载的可视范围是大视野范围
        webSettings.setLoadWithOverviewMode(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }


}
