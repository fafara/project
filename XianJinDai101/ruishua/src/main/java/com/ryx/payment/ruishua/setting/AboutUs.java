package com.ryx.payment.ruishua.setting;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_about_us)
public class AboutUs extends BaseActivity {

    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tv_title)
    TextView mTitle;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.webview)
    WebView mWebView;

    String url = "";

    @AfterViews
    public void initViews() {
        mTitle.setText("关于我们");
        mBackImg.setVisibility(View.VISIBLE);
        mMsgImg.setVisibility(View.GONE);
        WebSettings wSet = mWebView.getSettings();
        wSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wSet.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                LogUtil.showToast(AboutUs.this, description);
            }
        });
        url = PreferenceUtil.getInstance(AboutUs.this).getString(RyxAppconfig.Notes_About, "");
        mWebView.loadUrl(url);
    }

    @Click(R.id.tileleftImg)
    public void backBtnClick() {
        finish();
    }
}
