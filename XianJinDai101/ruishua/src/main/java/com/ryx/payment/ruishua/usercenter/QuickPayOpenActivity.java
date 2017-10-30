package com.ryx.payment.ruishua.usercenter;


import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.adapter.QuickPayBankListAdapter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 开通快捷支付
 * Created by xiepingping
 */
@EActivity(R.layout.activity_quick_pay_open)
public class QuickPayOpenActivity extends BaseActivity {

    @ViewById(R.id.btn_open)
    Button btn_open;
    @ViewById(R.id.rv_banklist)
    RecyclerView rv_bank;
    @ViewById(R.id.tv_quickpay_contract)
    TextView tv_quickpay_contract;//快捷支付开通协议
    @ViewById(R.id.tv_instruction)
    TextView tv_instruction;//快捷支付开通说明
    @ViewById
    AutoLinearLayout lay_out;
    private QuickPayBankListAdapter quickPayBankListAdapter;//需要开通快捷支付的银行卡列表
    private ArrayList<BankCardInfo> cardInfoList = new ArrayList<BankCardInfo>();
    private int QUICKPAY_BACK=995;//开通快捷支付跳转code

    @AfterViews
    public void initViews() {
        setTitleLayout("开通快捷支付", true, false);
        initQtPatParams();
        Intent intent = getIntent();
        if (intent.hasExtra("resultData")) {
            analyzeResult(intent.getStringExtra("resultData"));
        }
    }

    @Click(R.id.tv_quickpay_contract)
    public void showQuickContract(){
        Intent intent = new Intent(QuickPayOpenActivity.this,HtmlMessageActivity_.class);
        intent.putExtra("title","快捷支付开通协议");
        intent.putExtra("urlkey", RyxAppconfig.Notes_FastAgreement);
        startActivity(intent);
    }

    //解析开通快捷支付返回的内容:
    // "resultcode":"是否展示（0:不展示，1:展示）
    private void analyzeResult(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject resultData = new JSONObject(data);
                if(resultData.has("resultmsg")){
                    tv_instruction.setText(resultData.getString("resultmsg"));
                }
                if (resultData.has("resultbean")) {
                    String resultbean = resultData.getString("resultbean");
                    LogUtil.showLog("resultbean---", resultbean + "---");
                    if (!TextUtils.isEmpty(resultbean)) {
                        JSONArray bankInfoList = new JSONArray(resultbean);
                        int len = bankInfoList.length();
                        for (int i = 0; i < len; i++) {
                            BankCardInfo bankCardInfo = new BankCardInfo();
                            bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(
                                    bankInfoList.getJSONObject(i), "cardno"));
                            bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(
                                    bankInfoList.getJSONObject(i), "bankid"));
                            cardInfoList.add(bankCardInfo);
                        }
                        initRecycleView();
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void initRecycleView() {
        //设置阴影效果
        ViewCompat.setElevation(lay_out, 10.0f);
        RecyclerViewHelper.init().setRVGridLayout(QuickPayOpenActivity.this, rv_bank, 1);//3列
        quickPayBankListAdapter = new QuickPayBankListAdapter(cardInfoList, QuickPayOpenActivity.this, R.layout.adapter_quickpay_bank_list_item);
        rv_bank.setAdapter(quickPayBankListAdapter);
    }

    @Click(R.id.btn_open)
    public void openQuickPay() {
        qtpayApplication.setValue("QuickPaymentCardBind.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost(false, true, "QuickPaymentCardBind", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showLog("QuickPaymentCardBind----", payResult.getData() + "------"+payResult.toString());
                if(!TextUtils.isEmpty(payResult.getData())){
                    try{
                    JSONObject jsonObject = new JSONObject(payResult.getData());
                        if(jsonObject.has("code")){
                            if("0000".equals(jsonObject.getString("code"))){
                                setResult(QUICKPAY_BACK);
                                finish();
                            }
                        }
                    }catch(JSONException ex){
                        ex.printStackTrace();
                    }
                }

            }

            @Override
            public void onOtherState() {

            }

            @Override
            public void onTradeFailed() {

            }
        });
    }

    @Override
    protected void backUpImgOnclickListen() {
        setResult(QUICKPAY_BACK);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(QUICKPAY_BACK);
        finish();
    }
}
