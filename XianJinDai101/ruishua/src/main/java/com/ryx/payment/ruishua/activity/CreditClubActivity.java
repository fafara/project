package com.ryx.payment.ruishua.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.view.ProgressWebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by laomao on 16/12/27.
 */
@EActivity(R.layout.activity_creditclub_html)
public class CreditClubActivity extends BaseActivity {

    @ViewById
    ProgressWebView webview;
    @ViewById
    ImageView imageView;
    @ViewById
    TextView textView;
    @ViewById
    Button button;
    @ViewById
    LinearLayout ll_noInternet;
    String failUrl="";
    String rootUrl="";

    @AfterViews
    public void afterInitView() {
showLoading("努力加载中...");
        try {
            webview.setProgressbarDrawable(getResources().getDrawable(R.drawable.redorange));
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
            webview.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                 //   LogUtil.showToast(CreditClubActivity.this, description);
                    failUrl=failingUrl;
                    htmlNetworkFail(CreditClubActivity.this, view, new CompleteResultListen() {
                        @Override
                        public void compleResultok() {
                            webview.loadUrl(failUrl);
                        }
                    });
                    cancleLoading();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
//                    Map map=new HashMap<String, String>();
//                    map.put("httpUrl",url);
//                    map.put("phoneNumber", RyxAppdata.getInstance(CreditClubActivity.this).getPhone());
//                    map.put("appUser",  RyxAppconfig.APPUSER);
//                    MobclickAgent.onEvent(CreditClubActivity.this, "CreditClub", map);
                      setTitleLayout(view.getTitle(), true, true);
                    cancleLoading();
                }
            });
            String url = PreferenceUtil.getInstance(CreditClubActivity.this).getString(urlkey, "")+"?app="+ RyxAppconfig.APPUSER;
            LogUtil.showLog("htmlKey===" + urlkey + ",url==" + url);
            if (TextUtils.isEmpty(ccurl)){
                webview.loadUrl(url);
            }
            else{
                webview.loadUrl(ccurl);
            }
        } catch (Exception e) {
            LogUtil.showToast(CreditClubActivity.this, "数据异常！！！");
        }
    }
    @Click(R.id.button)
    public void refreshPage() {
        webview.setVisibility(View.VISIBLE);
        ll_noInternet.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(failUrl)) {
            LogUtil.showLog("failingUrl==" + failUrl);
            webview.loadUrl(failUrl);
        } else {
            LogUtil.showLog("loadrootUrl==" + rootUrl);
            webview.loadUrl(rootUrl);
        }
    }
    public void setTitleLayout(String title, boolean... leftRightisShow) {

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        ImageView leftImageView = (ImageView) findViewById(R.id.tileleftImg);
        ImageView rightImageView = (ImageView) findViewById(R.id.tilerightImg);
        if (leftRightisShow.length > 0) {
            //第一个代表左侧返回图标
            boolean leftIshow = leftRightisShow[0];
            if (leftIshow) {
                leftImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(webview.canGoBack())
                            webview.goBack();
                        else
                            CreditClubActivity.this.finish();
                    }
                });
                leftImageView.setVisibility(View.VISIBLE);
            } else {
                leftImageView.setVisibility(View.INVISIBLE);
            }
            //第二个代表右侧帮助图标
            boolean rightIshow = leftRightisShow[leftRightisShow.length - 1];
            if (rightIshow) {
                rightImageView.setVisibility(View.VISIBLE);
            } else {
                rightImageView.setVisibility(View.INVISIBLE);
            }
            rightImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreditClubActivity.this.finish();
                }
            });
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
               ) {
            if(webview.canGoBack())
                webview.goBack();
            else
                CreditClubActivity.this.finish();
        }
        return false;
//        return super.onKeyDown(keyCode, event);
    }
}
