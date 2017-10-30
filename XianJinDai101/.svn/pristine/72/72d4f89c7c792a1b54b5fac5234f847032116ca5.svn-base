package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.VerificationCodeActivity_;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.payment.ruishua.view.SweepCreateOrderView;
import com.ryx.quickadapter.inter.NoDoubleClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 扫一扫功能订单确认页面
 */
@EActivity(R.layout.activity_sweep_quick_pay_order)
public class SweepQuickPayOrderActivity extends BaseActivity {
    @ViewById
    SweepCreateOrderView createorder;
    private  String orderId,amount,toPayuserName,account,mobileno,skaccount,fkphone;
    @AfterViews
    public void iniview(){
        setTitleLayout("确认订单",true,false);
        initQtPatParams();
        getIntentdata();
        showOrderInfo();
        createorder.setVisibility(View.VISIBLE);

    }
    private void getIntentdata(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId= bundle.getString("orderId");
            amount= bundle.getString("amount");
            toPayuserName= bundle.getString("toPayuserName");
            account= bundle.getString("account");///选中的支付卡号即付款卡号
            mobileno= bundle.getString("mobileno");////收款人手机号
            skaccount= bundle.getString("skaccount");
            fkphone= bundle.getString("fkphone");
        } else {
            LogUtil.showToast(SweepQuickPayOrderActivity.this, "订单信息数据有误!");
        }
    }
    private void showOrderInfo() {
        createorder.setExplanation("订单号", "收款人姓名", "收款账号","付款卡号","付款手机号","");
        createorder.setValue(amount,orderId,toPayuserName,skaccount, StringUnit.cardJiaMi(account),fkphone,"");
            createorder.setNextBtnText("确认");
            createorder.setNextListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    qtpayApplication.setValue("QuickPaymentPay.Req");
                    qtpayAttributeList.add(qtpayApplication);
                    qtpayParameterList.add(new Param("orderId",orderId));
                    httpsPost("QuickPaymentPaytag", new XmlCallback() {
                        @Override
                        public void onTradeSuccess(RyxPayResult payResult) {
                            String resultdata=payResult.getData();
                            if(!TextUtils.isEmpty(resultdata)){
                                try {
                                    JSONObject resultDataObj=new JSONObject(resultdata);
                                    String needsms=  JsonUtil.getValueFromJSONObject(resultDataObj,"needsms");
                                    String needcvn2=  JsonUtil.getValueFromJSONObject(resultDataObj,"needcvn2");
                                    String phonenumber=  JsonUtil.getValueFromJSONObject(resultDataObj,"phonenum");
                                    String systemno=  JsonUtil.getValueFromJSONObject(resultDataObj,"systemno");
                                    if(("0".equals(needsms)&&"0".equals(needcvn2))||((TextUtils.isEmpty(needsms)&&TextUtils.isEmpty(needcvn2)))) {
                                        LogUtil.showToast(SweepQuickPayOrderActivity.this, "交易成功");
                                        Intent intent=new Intent(SweepQuickPayOrderActivity.this,PaymentSuccessful_.class);
                                        intent.putExtra("flag","CLOSEALL");
                                        startActivityForResult(intent,0x002);
                                    }else if("1".equals(needsms)||"1".equals(needcvn2)){
                                        Intent intent = new Intent(SweepQuickPayOrderActivity.this, VerificationCodeActivity_.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("flag","QuickPaymentSMS");
                                        bundle.putString("toPhoneNum",phonenumber);
                                        bundle.putString("title","交易资料补充");
                                        bundle.putString("needsms",needsms);
                                        bundle.putString("needcvn2",needcvn2);
                                        bundle.putString("systemno",systemno);
                                        intent.putExtra("CodeData",bundle);
                                        startActivityForResult(intent,0x002);
                                    }else{
                                        LogUtil.showToast(SweepQuickPayOrderActivity.this, "交易失败请稍后再试!");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    LogUtil.showToast(SweepQuickPayOrderActivity.this, "确认订单返回信息有误!");
                                }
                            }else{
                                LogUtil.showToast(SweepQuickPayOrderActivity.this, "交易成功!");
                                Intent intent=new Intent(SweepQuickPayOrderActivity.this,PaymentSuccessful_.class);
                                intent.putExtra("flag","CLOSEALL");
                                startActivityForResult(intent,0x002);
                            }

                        }

                        @Override
                        public void onOtherState(String resCode,String resDesc) {
                            if("9101".equals(resCode)){
                                Intent intent=new Intent(SweepQuickPayOrderActivity.this,PaymentSuccessful_.class);
                                intent.putExtra("flag","CLOSEALL");
                                startActivityForResult(intent,0x002);
                            }else{
                                finish();
                            }
                        }
                    });
                }
            });
    }
    @Override
    public void  initQtPatParams() {
        super.initQtPatParams();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.showLog("resultCode=="+resultCode);
        if (RyxAppconfig.CLOSE_STACK_ALL == resultCode) {
            setResult(RyxAppconfig.CLOSE_STACK_ALL);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
