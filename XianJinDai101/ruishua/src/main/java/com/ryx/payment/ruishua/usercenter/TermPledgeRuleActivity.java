package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.CreateOrder_;
import com.ryx.payment.ruishua.convenience.Swiper_;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.RyxMoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

@EActivity(R.layout.activity_term_pledge_rule)
class TermPledgeRuleActivity extends BaseActivity {
    @ViewById
    TextView tv_name, tv_pledge_amount, tv_activitydesc, tv_url;

    @ViewById(R.id.placetheorder_btn)
    Button placetheorder_btn;
    String urlVal;
    String pledgeAmount="";

    @AfterViews
    public void initView() {
        setTitleLayout("激活设备", true, false);
        initDataView();
    }

    @Click(R.id.placetheorder_btn)
    public void placetheorderBtnClick() {
//        placetheorder();
        doOrderRequest();
    }

    private void initDataView() {
        String termPledgeRule = getIntent().getStringExtra("termRules");
        try {
            JSONObject jsonObject = new JSONObject(termPledgeRule);
            String pledge_amountVal = JsonUtil.getValueFromJSONObject(jsonObject, "pledge_amount");
            String nameVal = JsonUtil.getValueFromJSONObject(jsonObject, "name");
            String activitydescVal = JsonUtil.getValueFromJSONObject(jsonObject, "activitydesc");
            urlVal = JsonUtil.getValueFromJSONObject(jsonObject, "url");

            tv_name.setText(nameVal);
            pledgeAmount=RyxMoneyEncoder.EncodeFormat(pledge_amountVal);
            tv_pledge_amount.setText(pledgeAmount);
            tv_activitydesc.setText(activitydescVal);
            tv_url.setText(Html.fromHtml("<font color=\"#999999\">详情请戳这里</font>" + "<font color=\"#1db7f0\">" + urlVal + "</font>"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.tv_url)
    public void termPledRuleUrl() {
        Intent intent = new Intent(TermPledgeRuleActivity.this, HtmlMessageActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", "终端服务费");
        bundle.putString("ccurl", urlVal);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void placetheorder() {
        try {
            Intent intent = new Intent(TermPledgeRuleActivity.this, CreateOrder_.class);
            OrderInfo orderinfo = Order.IMPAY_CASHPLEDGE;
            orderinfo.setOrderRemark("闪付");
            orderinfo.setOrderAmt(pledgeAmount);
            orderinfo.setOrderDesc("any");
            orderinfo.setAccount2("");
            orderinfo.setDisPlayContent("");
//        orderinfo.setMustMpos(true);
            orderinfo.setPayee("");
            orderinfo.setCardIdx("");
            orderinfo.setMerchantId(RyxAppconfig.RYX_IMPAY_CASHPLEDGE_MERCHANT);
            orderinfo.setProductId(RyxAppconfig.RYX_IMPAY_CASHPLEDGE_PRODUCT);
            //是否进行储蓄卡交易拦截
            orderinfo.setIscashCardIntercept(true);
//    //交易类型存本地
//    PreferenceUtil.getInstance(this).saveString(
//            RyxAppdata.getInstance(this).getMobileNo() + "_impay", productId);
            orderinfo.setInterfaceTag("01");
            orderinfo.setCouponBindDisPaly("");
            orderinfo.setCouponBindId("");
            intent.putExtra("orderinfo", orderinfo);
            startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
        } catch (Exception e) {
            LogUtil.showToast(TermPledgeRuleActivity.this, "数据错误,请稍后再试");
        }
    }

    //add by laomao  不展示订单直接下单
    private void doOrderRequest() {
        super.initQtPatParams();
        qtpayApplication = new Param("application", "RequestOrder.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("merchantId", RyxAppconfig.RYX_IMPAY_CASHPLEDGE_MERCHANT));
        qtpayParameterList.add(new Param("productId", RyxAppconfig.RYX_IMPAY_CASHPLEDGE_PRODUCT));
        qtpayParameterList.add(new Param("orderAmt",RyxMoneyEncoder.encodeToPost(pledgeAmount) ));
        qtpayParameterList.add(new Param("orderDesc", "any"));
        qtpayParameterList.add(new Param("payTool", "01"));

        httpsPost("RequestOrderTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {

                Intent intent = new Intent(TermPledgeRuleActivity.this, Swiper_.class);
                OrderInfo orderinfo = Order.IMPAY_CASHPLEDGE;
                orderinfo.setOrderRemark("闪付");
                orderinfo.setOrderAmt(RyxMoneyEncoder.encodeToPost(pledgeAmount));
                orderinfo.setOrderDesc("any");
                orderinfo.setAccount2("");
                orderinfo.setDisPlayContent("");
//        orderinfo.setMustMpos(true);
                orderinfo.setPayee("");
                orderinfo.setCardIdx("");
                orderinfo.setMerchantId(RyxAppconfig.RYX_IMPAY_CASHPLEDGE_MERCHANT);
                orderinfo.setProductId(RyxAppconfig.RYX_IMPAY_CASHPLEDGE_PRODUCT);
                //是否进行储蓄卡交易拦截
                orderinfo.setIscashCardIntercept(false);
//    //交易类型存本地
//    PreferenceUtil.getInstance(this).saveString(
//            RyxAppdata.getInstance(this).getMobileNo() + "_impay", productId);
                orderinfo.setInterfaceTag("14");
                orderinfo.setCouponBindDisPaly("");
                orderinfo.setCouponBindId("");
                orderinfo.setRealAmt(RyxMoneyEncoder.encodeToPost(pledgeAmount));
                orderinfo.setTransLogNo(payResult.getTransLogNo());
                orderinfo.setOrderId(payResult.getOrderId());
                orderinfo.setOrderAmt(payResult.getOrderAmt());
                intent.putExtra("orderinfo", orderinfo);
                startActivity(intent);
                finish();
//                Log.e("test====",payResult.toString());
//                orderinfo.setRealAmt(payResult.getRealAmt());
//                orderinfo.setTransLogNo(payResult.getTransLogNo());
//                orderinfo.setOrderId(payResult.getOrderId());
//                orderinfo.setOrderAmt(payResult.getOrderAmt());
//
//                showOrderInfo();
//                createorder.setVisibility(View.VISIBLE);


            }

            @Override
            public void onLoginAnomaly() {
                finish();
            }

            @Override
            public void onOtherState() {
                finish();
            }

            @Override
            public void onTradeFailed() {
                finish();
            }
        });


    }

}
