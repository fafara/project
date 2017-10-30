package com.ryx.payment.ruishua.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.activity.MainFragmentActivity;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.utils.Base64Utils;
import com.ryx.payment.ruishua.utils.GlideUtils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * 会员中心
 */
@EFragment(R.layout.fragment_member)
public class MemberFragment extends BaseFragment {
    @ViewById
    WebView memberfrg_webview;
    @ViewById
    ImageView memberfrg_imgview;
    private static MemberFragment thisInstance;
    private FragmentListener mListener;
    @AfterInject
    public void create() {
        thisInstance = this;
    }

    public MemberFragment getInstance() {
        return thisInstance;
    }

    @Override
    public void onAttach(Context context) {
        try {
            mListener = (MainFragmentActivity) getBaseActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);
    }

    @AfterViews
    public void iniView(){
        memberfrg_imgview.setVisibility(View.VISIBLE);
        memberfrg_webview.setVisibility(View.INVISIBLE);
        GlideUtils.getInstance().load(getContext(),R.drawable.frg_member_bag,memberfrg_imgview);
       String customerid= RyxAppdata.getInstance(getContext()).getCustomerId();

        WebSettings wSet = memberfrg_webview.getSettings();
        wSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wSet.setJavaScriptEnabled(true);
        wSet.setDomStorageEnabled(true);
        wSet.setAllowFileAccess(true);
        memberfrg_webview.addJavascriptInterface(new MemberFragment.JavaScriptinterface(getContext()), "ryx");
        memberfrg_webview.setVerticalScrollBarEnabled(false); //垂直不显示
        memberfrg_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        memberfrg_webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, final String failingUrl) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtil.showLog("url=="+url);
                if(memberfrg_webview!=null&&memberfrg_webview.getVisibility()==View.INVISIBLE){
                    memberfrg_webview.setVisibility(View.VISIBLE);
                    memberfrg_imgview.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        String token= RyxAppdata.getInstance(getContext()).getToken();
        String phone=  RyxAppdata.getInstance(getContext()).getPhone();
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("customerId",customerid);
            jsonObject.put("token",token);
            jsonObject.put("phone",phone);
            jsonObject.put("appUser",RyxAppconfig.APPUSER);
            jsonObject.put("ryxpayUrl",RyxAppconfig.BASE_RELEASE_URL);
            String paramsJsonStr= jsonObject.toString();
            LogUtil.showLog("paramsJsonStr=="+paramsJsonStr);
            String params=    Base64Utils.strEncodeHex(paramsJsonStr);
            LogUtil.showLog("params=="+params);
            memberfrg_webview.loadUrl(RyxAppconfig.BASE_MEMBER_URL+"app/member?random="+params);
        }catch (Exception e){

        }

    }
    /**
     * 网页webview加载失败后默认页显示（调用当前方法页面布局一定要有<include layout="@layout/ryxpaynointernetlyout"></include>）
     * @param context Activty
     * @param webView webView对象
     * @param completeResultListen  点击刷新按钮后监听事件
     */
    protected void htmlNetworkFail(Activity context, final WebView webView, final BaseActivity.CompleteResultListen completeResultListen){
        Button button= (Button)context.findViewById(R.id.button);
        final LinearLayout linearLayout= (LinearLayout)context.findViewById(R.id.ll_noInternet);
        webView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                LogUtil.showLog("htmlNetworkFail==刷新");
                webView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                completeResultListen.compleResultok();
            }
        });
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
        public void againLogin() {
            toAgainLogin(context, RyxAppconfig.TOLOGINACT);
        }

    }

    public  boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK&& memberfrg_webview!=null&&memberfrg_webview.canGoBack()) {
            memberfrg_webview.goBack();
            return true;
        }
        return false;
    }
}
