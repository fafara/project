package com.ryx.payment.ruishua.usercenter;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_register_agreement)
public class RegisterAgreement extends BaseActivity {

    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tv_title)
    TextView mTitle;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.webview)
    WebView mWebView;

    @AfterViews
    public void initViews() {
        mTitle.setText(RyxAppdata.getInstance(this).getCurrentBranchName()+"服务协议");
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            mTitle.setText("瑞刷服务协议");
//        } else if (RyxAppconfig.BRANCH.equals("02")) {
//            mTitle.setText("瑞银信服务协议");
//        }
        mBackImg.setVisibility(View.VISIBLE);
        mMsgImg.setVisibility(View.GONE);
        WebSettings wSet = mWebView.getSettings();
        wSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wSet.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                LogUtil.showToast(RegisterAgreement.this, description);
            }
        });
        String url = PreferenceUtil.getInstance(RegisterAgreement.this).getString(
                RyxAppconfig.Notes_Regist, "");
        if (url.length() > 0) {
            LogUtil.showLog("htmlKey===" + RyxAppconfig.Notes_Regist + ",url==" + url);
            mWebView.loadUrl(url);
        }
    }

    @Click(R.id.tileleftImg)
    public void setmBackImg() {
        finish();
    }
}
