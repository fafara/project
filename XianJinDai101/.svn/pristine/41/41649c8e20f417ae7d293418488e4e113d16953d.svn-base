package com.ryx.payment.ruishua.sjfx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.RuibeanActivity;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.DeViceListActivity_;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.view.ProgressWebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/29.
 */

@EActivity(R.layout.activity_sjfx_advertisement)
public class ADHtmlActivity extends BaseActivity {
    @ViewById
    ProgressWebView pv_advert;
    String url;
    String title;

    @AfterViews
    public void afterView() {
        Intent intent = getIntent();
        if (intent.hasExtra("url"))
            url = intent.getStringExtra("url");
        if (intent.hasExtra("title"))
        title = intent.getStringExtra("title");

        setTitleLayout(title, true, false);
        initQtPatParams();
        initWebView();
    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        pv_advert.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        pv_advert.getSettings().setJavaScriptEnabled(true);
        String ua = pv_advert.getSettings().getUserAgentString();
        pv_advert.getSettings().setUserAgentString(ua+" "+RyxAppconfig.APPUSER+"/"+RyxAppconfig.CLIENTVERSION);
        pv_advert.addJavascriptInterface(new ADHtmlActivity.JavaScriptinterface(this), "ryx"
        );
        pv_advert.setVerticalScrollBarEnabled(false); //垂直不显示
        pv_advert.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        pv_advert.getSettings().setDomStorageEnabled(true);//DOM Storage
        pv_advert.getSettings().setGeolocationEnabled(true);
        class MyWebChromeClient extends WebViewClient {


            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
                showLoading("努力加载中，请耐心等待...");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                cancleLoading();
                setTitleLayout(view.getTitle(), true, false);
            }
        }
        pv_advert.setWebViewClient(new MyWebChromeClient());
        LogUtil.showLog("ADHTMLUri=="+url);
        pv_advert.loadUrl(url);
    }

    /**
     * Android与HTML页面交互接口
     */
    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void alert(String ssss) {
            Toast.makeText(context, ssss, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void shareInvite(final String content) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    invitationCode(content);
                }
            });
        }
        @JavascriptInterface
        public void shareBuyRedbean(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ADHtmlActivity.this,RuibeanActivity.class);
                    startActivity(intent);
                }
            });
        }
        @JavascriptInterface
        public void shareTo(final String content) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    shareContent(content);
                }
            });
        }
        @JavascriptInterface
        public void jumpPage(final String pageClassStr){
//          例如jumpPage(".authenticate.Authenticate_");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = new Intent(ADHtmlActivity.this,
                                Class.forName(getApplicationContext().getPackageName()+pageClassStr));
                        startActivity(intent);
                    }catch (Exception e){
                        LogUtil.showToast(ADHtmlActivity.this,"跳转失败"+e.getLocalizedMessage());
                    }
                }
            });
        }

        @JavascriptInterface
        public void againLogin() {
            toAgainLogin(context, RyxAppconfig.TOLOGINACT);
        }

    }

    /**
     * 邀请码获取
     */
    public void invitationCode(final String content) {
        if (!QtpayAppData.getInstance(this).isLogin()) {
            toAgainLogin(this,RyxAppconfig.TOLOGINACT);
            return;
        }

        qtpayApplication.setValue("InvitationCode.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("invitationCodeTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    String data = payResult.getData();
                    JSONObject dataJsonObj = new JSONObject(data);
                    String code = JsonUtil.getValueFromJSONObject(dataJsonObj, "code");
                    if (RyxAppconfig.QTNET_SUCCESS.equals(code)) {
                        JSONObject resultOj = dataJsonObj.getJSONObject("result");
                        final String code_url = JsonUtil.getValueFromJSONObject(resultOj, "code_url");
                        if(!TextUtils.isEmpty(code_url)){
                            shareContent(content+code_url);
                        }else{
                            LogUtil.showToast(ADHtmlActivity.this,"邀请码有误!");
                        }
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onOtherState(String rescode, String resDesc) {
                if("9127".equals(rescode)){
                    showDialog("因为您未绑定终端无法获取邀请码,是否现在去绑定?");
                }
            }
        });

    }
public void shareContent(String content){
    try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, content);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(Intent.createChooser(intent, getTitle()));
            } catch (Exception e) {
                LogUtil.showToast(ADHtmlActivity.this, "调取分享失败,请尝试长按链接复制!");
            }
}
    private void showDialog(String content){
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(ADHtmlActivity.this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                Intent intent=new Intent(ADHtmlActivity.this, DeViceListActivity_.class);
                intent.putExtra("flag","banding");
                startActivity(intent);
                finish();
            }
            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent(content);
    }
}
