package com.ryx.payment.ruishua.sjfx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.SjfxDevPayQuickPayActivity_;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.widget.RyxLoadDialogBuilder;
import com.ryx.quickadapter.inter.NoDoubleClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

@EActivity(R.layout.activity_dev_purchase)
public class DevPurchaseActivity extends BaseActivity {
    @ViewById
    WebView devpurchase_webview;
    @ViewById(R.id.tileleftImg)
    ImageView tileleftImg;
    //展示订单页面
    private int MYORDERFLAG=0x908;
    //去二维码展示页面
    private int TOQRCODEFLAG=0x909;
    //去快捷支付页面
    private int TOKJZFFLAG=0x910;
    RyxLoadDialogBuilder ryxLoadDialogBuilder=null;
    /**
     * 去我的订单页面
     */
    public void toMyOrderPage(){
        devpurchase_webview.loadUrl(RyxAppconfig.BASE_DEVPURCH_URL+"mall/my_order.html");
    }

    /**
     * 去快捷支付页面
     */
    public void toQuickPayActivity(String amount,String orderId){
//        String custoemrid=resultObject.getString("custoemrid");
//        String username=resultObject.getString("username");
//        String bankid=resultObject.getString("bankid");
//        String qrcode=resultObject.getString("qrcode");
////                        String cardno=resultObject.getString("cardno");//收款人账号此字段无用，因为不一定是此账号收款
//        String cardidx=resultObject.getString("cardidx");
//        String mobileno=resultObject.getString("mobileno");
        Intent intent=new Intent(DevPurchaseActivity.this,SjfxDevPayQuickPayActivity_.class);
        Bundle bundle=new Bundle();
        bundle.putString("orderId",orderId);
        bundle.putString("amount",amount);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    //去二维码支付页面
    public void toQrcodeActivity(String url){
        if(!TextUtils.isEmpty(url)){
            Intent intent=new Intent(DevPurchaseActivity.this,DevPurchaseQrcodeActivity_.class);
            intent.putExtra("qrcodeurl",url);
            startActivityForResult(intent,TOQRCODEFLAG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==MYORDERFLAG){
            toMyOrderPage();
        }
    }

    @AfterViews
    public void afterView(){
        setTitleLayout("机具申购", true, true);
        ryxLoadDialogBuilder =new RyxLoadDialogBuilder(
                DevPurchaseActivity.this, R.style.CustomProgressDialog);
        tileleftImg.setImageDrawable(getResources().getDrawable(R.drawable.ryxtitleclose));
        initQtPatParams();
        initWebView();
        ImageView imageView=getRightImgView();
        imageView.setImageResource(R.drawable.myorderlist);
        imageView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                toMyOrderPage();
            }
        });
    }
    /**
     * 初始化webview
     */
    private void initWebView() {
//        showLoading();
        devpurchase_webview.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        devpurchase_webview.getSettings().setJavaScriptEnabled(true);
        devpurchase_webview.getSettings().setDomStorageEnabled(true);
        devpurchase_webview.addJavascriptInterface(new JavaScriptinterface(this),"ryx"
                );
        devpurchase_webview.setVerticalScrollBarEnabled(false); //垂直不显示
        devpurchase_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        String ua = devpurchase_webview.getSettings().getUserAgentString();
        devpurchase_webview.getSettings().setUserAgentString(ua+" "+RyxAppconfig.APPUSER+"/"+RyxAppconfig.CLIENTVERSION);
       LogUtil.showLog(ua+"userAgent=="+devpurchase_webview.getSettings().getUserAgentString());
        class MyWebChromeClient extends WebViewClient
        {


            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {

//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
                if(ryxLoadDialogBuilder==null){
                    ryxLoadDialogBuilder =new RyxLoadDialogBuilder(
                            DevPurchaseActivity.this, R.style.CustomProgressDialog);
                }
                ryxLoadDialogBuilder.setCanceledOnTouchOutside(false);
                ryxLoadDialogBuilder.setMessage("努力加载中...");
                ryxLoadDialogBuilder.show();
//                    }
//                });

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//               mHandler.post(new Runnable() {
//                   @Override
//                   public void run() {
                if(ryxLoadDialogBuilder!=null){
                    ryxLoadDialogBuilder.cancel();
                }
                String webtitle=view.getTitle();
                if(TextUtils.isEmpty(webtitle)){
                    setTitleLayout("机具申购", true, true);
                }else{
                    setTitleLayout(webtitle, true, true);
                }

//                   }
//               });
            }
        }
        devpurchase_webview.setWebViewClient(new MyWebChromeClient());
//        devpurchase_webview.loadUrl("file:///android_asset/sjfx/devPurchase.html");
        devpurchase_webview.loadUrl(RyxAppconfig.BASE_DEVPURCH_URL+"mall/carts.html");
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                ) {
//            if(devpurchase_webview.canGoBack()){
//
//                devpurchase_webview.goBack();
//            }
//            else{
//                DevPurchaseActivity.this.finish();
//            }
//        }
//        return false;
//    }

    /**
     * Android与HTML页面交互接口
     */
    public class JavaScriptinterface {
        Context context;
        public JavaScriptinterface(Context c) {
            context= c;
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void alert(String ssss) {
            Toast.makeText(context, ssss, Toast.LENGTH_LONG).show();
        }
        @JavascriptInterface
        public void showToast(String ssss) {

            Toast.makeText(context, ssss, Toast.LENGTH_LONG).show();
        }
        @JavascriptInterface
        public void getUserMsg(){
            final JSONObject userMsgObj=new JSONObject();
            try {
                String realName= QtpayAppData.getInstance(DevPurchaseActivity.this)
                        .getRealName();
                String mobileNo=  QtpayAppData.getInstance(DevPurchaseActivity.this)
                        .getMobileNo();
            String country=    PreferenceUtil.getInstance(DevPurchaseActivity.this).getString("country","");
             String address=   PreferenceUtil.getInstance(DevPurchaseActivity.this).getString("address","");
                String province=    PreferenceUtil.getInstance(DevPurchaseActivity.this).getString("provice","");
                String city=     PreferenceUtil.getInstance(DevPurchaseActivity.this).getString("city","");
                String district=     PreferenceUtil.getInstance(DevPurchaseActivity.this).getString("district","");
                userMsgObj.put("realName",realName);
                userMsgObj.put("mobileNo",mobileNo);
                userMsgObj.put("country",country);
                userMsgObj.put("address",address);
                userMsgObj.put("province",province);
                userMsgObj.put("city",city);
                userMsgObj.put("district",district);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        devpurchase_webview.loadUrl("javascript:getUserMsgback('"+userMsgObj.toString()+ "');");
                    }
                });

            }catch (Exception e){
            }
        }
        /**
         * 产品列表展示
         */
        @JavascriptInterface
        public void termSaleListShow(){
//            cancleLoading();
            qtpayApplication.setValue("TermSaleShow.Req");
            qtpayAttributeList.add(qtpayApplication);
            httpsPost("TermSaleShowtag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    String data=payResult.getData();
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        final String resultBeanStr=  JsonUtil.getValueFromJSONObject(jsonObject,"resultBean");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                devpurchase_webview.loadUrl("javascript:termSaleListShowback('"+resultBeanStr+ "');");
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        /**
         *订单详情页（去支付按钮点击）
         */
        @JavascriptInterface
        public void TermSaleOrderDetail(String orderId){
            qtpayApplication.setValue("TermSaleOrderDetail.Req");
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(new Param("orderId",orderId));
            httpsPost("TermSaleOrderDetailTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    devpurchase_webview.loadUrl("javascript:TermSaleOrderDetailback('"+payResult.getData()+"');");
                }
            });
        }
        /**
         *订单修改
         */
        @JavascriptInterface
        public void TermSaleOrderEdit(String order_detail){
            qtpayApplication.setValue("TermSaleOrderEdit.Req");
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(new Param("order_detail",order_detail));
            httpsPost("TermSaleOrderEditTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    devpurchase_webview.loadUrl("javascript:TermSaleOrderEditback('"+payResult.getData()+"');");
                }
            });
        }
        /**
         * 下订单接口
         * @param order_detail
         */
        @JavascriptInterface
        public void TermSaleSetOrder(String order_detail){
            LogUtil.showLog("order_detail=="+order_detail);
            qtpayApplication.setValue("TermSaleSetOrder.Req");
            qtpayAttributeList.add(qtpayApplication);
            try {
                String orderdetail = DataUtil.encode(order_detail.trim().replace(" ", ""));
                qtpayParameterList.add(new Param("order_detail",orderdetail));
                httpsPost("TermSaleSetOrderTag", new XmlCallback() {
                    @Override
                    public void onTradeSuccess(RyxPayResult payResult) {
                        devpurchase_webview.loadUrl("javascript:TermSaleSetOrderback('"+payResult.getData()+"');");
                    }
                });
            } catch (UnsupportedEncodingException e) {
                LogUtil.showToast(DevPurchaseActivity.this,"订单数据有误");
            }
        }
        /**
         * 确认支付接口（支付宝，微信，快捷支付）
         * @param jsonStr
         */
        @JavascriptInterface
        public void PayOnline(String jsonStr){
//            {"md5sign":"xxx","orderId":"10000033000662837","payType":"ZFBZF"}
            try {
                JSONObject jsonObject=new JSONObject(jsonStr);
                String payType=JsonUtil.getValueFromJSONObject(jsonObject,"payType");
                String orderId=JsonUtil.getValueFromJSONObject(jsonObject,"orderId");
                String amount=JsonUtil.getValueFromJSONObject(jsonObject,"amount");
                if("WXZF".equals(payType)||"ZFBZF".equals(payType)){
                    qtpayApplication.setValue("PayOnline.Req");
                    qtpayAttributeList.add(qtpayApplication);
                    qtpayParameterList.add(new Param("payType",payType));
                    qtpayParameterList.add(new Param("orderId",orderId));
                    httpsPost("PayOnlineTag", new XmlCallback() {
                        @Override
                        public void onTradeSuccess(RyxPayResult payResult) {
                            String reaultData=payResult.getData();
                            try {
                                //{"result":"","code":"900002","msg":"其他错误"}
                                JSONObject resultDataObj=  new JSONObject(reaultData);
                                String code = JsonUtil.getValueFromJSONObject(resultDataObj,"code");
                                String msg = JsonUtil.getValueFromJSONObject(resultDataObj,"msg");
                               JSONObject payurlObj= JsonUtil.getJSONObjectFromJsonObject(resultDataObj,"result");
                                final String payurl = JsonUtil.getValueFromJSONObject(payurlObj,"payurl");
                                if(RyxAppconfig.QTNET_SUCCESS.equals(code)){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            toQrcodeActivity(payurl);
                                        }
                                    });
                                }else{
                                    LogUtil.showToast(DevPurchaseActivity.this,msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else if("KJZF".equals(payType)){
                    toQuickPayActivity(amount,orderId);
//                    final String payurl="http://www.ruiyinxin.com";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /**
         * 订单列表
         */
        @JavascriptInterface
        public void TermSaleOrderList(){
            qtpayApplication.setValue("TermSaleOrderList.Req");
            qtpayAttributeList.add(qtpayApplication);
            httpsPost("TermSaleOrderListTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
//                    String data=payResult.getData();
//                    try {
//                        JSONArray jsonArray=new JSONArray(data);
//                       LogUtil.showLog("jsonArraySize=="+jsonArray.length());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    devpurchase_webview.loadUrl("javascript:TermSaleOrderListback('"+payResult.getData()+"');");
                }
            });

        }


    }


}
