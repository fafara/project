package com.ryx.payment.ruishua.bindcard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.ExpiryFilter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.VISIBLE;

/**
 * 已经绑定过的卡进行开通快捷支付操作界面
 */
@EActivity(R.layout.activity_binded_card_open_quick_pay)
public class BindedCardOpenQuickPay extends BaseActivity {
    @ViewById
    Button btn_next;
    @ViewById
    TextView tv_cardNo,tv_pay_agree;
    @ViewById
    EditText edt_validate,edt_cvn,edt_mobile;
    @ViewById
    AutoRelativeLayout lay_cvn,lay_mobile,lay_date;
    @ViewById
    CheckBox cb_agree;
    private String account;//银行卡号
    //第一次是1时完善cvn2码信息，第二位1完善手机号，第三位1完善信用卡有效期
    private String cardStatus="";
    private String mobileStr,cvnStr,validdateStr;
    InputFilter[] filters = {new ExpiryFilter()};
    private int month;
    private int year;
    private boolean fullLength = false;
    String clickToastMsg="暂不允许开通,请稍后再试!";
    @AfterViews
    public void afterView() {
        setTitleLayout("开通快捷支付", true, false);
        getIntentData();
        initData();
        initQtPatParams();
//        initRequestParams();
    }

    private void getIntentData() {
        Intent intent =getIntent();
        if(intent!=null)
        {
            if(intent.hasExtra("accountNo")){
                account=intent.getStringExtra("accountNo");
                tv_cardNo.setText(StringUnit.cardJiaMi(account));
            }
            if(intent.hasExtra("prepareresult")){
              String prepareresult=  intent.getStringExtra("prepareresult");
                analyzeResult(prepareresult);
            }
        }
    }
    @Click(R.id.btn_next)
    public void BtnNextClick() {
        if(checkInput()){
            if(TextUtils.isEmpty(cardStatus)){
                LogUtil.showToast(BindedCardOpenQuickPay.this,""+clickToastMsg);
                return;
            }
            qtpayApplication.setValue("QuickPaymentBindCard.Req");
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(new Param("account",account));
            if("1".equals(cardStatus.substring(0,1))){
                //第一位是1时完善cvn2码信息
                String edt_cvnText= edt_cvn.getText().toString();
                qtpayParameterList.add(new Param("cvn2",edt_cvnText));
            }
            if("1".equals(cardStatus.substring(1,2))){
                //第二位1完善手机号
                String  edt_mobileText=edt_mobile.getText().toString();
                qtpayParameterList.add(new Param("phoneNum",edt_mobileText));
            }
            if("1".equals(cardStatus.substring(2,3))){
                //第三位1完善信用卡有效期
                String  edt_validateText=edt_validate.getText().toString();
                qtpayParameterList.add(new Param("validDate",edt_validateText));
            }
            httpsPost("QuickPaymentBindCardTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {

                    try {
                        String result =  payResult.getData();
                        JSONObject jsonObj = new JSONObject(result);
                     String    needsms = JsonUtil.getValueFromJSONObject(jsonObj, "needsms");
                     String    systemno = JsonUtil.getValueFromJSONObject(jsonObj, "systemno");
                     String    phoneNum = JsonUtil.getValueFromJSONObject(jsonObj, "phonenum");
                        if("1".equals(needsms)){
                            Intent intent = new Intent(BindedCardOpenQuickPay.this, VerificationCodeActivity_.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("flag", "BindedCardOpenQuickPay");
                            bundle.putString("title", "开通快捷支付");
                            bundle.putString("systemno",systemno);
                            bundle.putString("toPhoneNum",phoneNum);

                            intent.putExtra("CodeData", bundle);
                            startActivityForResult(intent, 0x001);
                        }else{
                            //开通支付完毕
                            setResult(RyxAppconfig.CLOSE_ALL);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

//    private void initRequestParams() {
//        initQtPatParams();
//        qtpayApplication = new Param("application", "QuickPaymentBindCardPrepare.Req");
//        qtpayAttributeList.add(qtpayApplication);
//        qtpayParameterList.add(new Param("account", account));
//        httpsPost("QuickPaymentBindCardPrepareTag", new XmlCallback() {
//            @Override
//            public void onTradeSuccess(RyxPayResult payResult) {
//               String result =  payResult.getData();
//                analyzeResult(result);
//            }
//
//            @Override
//            public void onOtherState(String code,String resc) {
//                super.onOtherState(code,resc);
//                if(!TextUtils.isEmpty(resc)){
//                    clickToastMsg=resc;
//                }
//                btn_next.setEnabled(false);
//                finish();
//            }
//
//            @Override
//            public void onTradeFailed() {
//                super.onTradeFailed();
//                btn_next.setEnabled(false);
//                finish();
//            }
//        });
//    }

    private void analyzeResult(String result){
        try{
            JSONObject jsonObj = new JSONObject(result);
            cardStatus = JsonUtil.getValueFromJSONObject(jsonObj, "status");
            if("1".equals(cardStatus.substring(0,1))){
                //第一位是1时完善cvn2码信息
                lay_cvn.setVisibility(VISIBLE);
            }else{
                lay_cvn.setVisibility(View.GONE);
            }
            if("1".equals(cardStatus.substring(1,2))){
                //第二位1完善手机号
                lay_mobile.setVisibility(VISIBLE);
            }else{
                lay_mobile.setVisibility(View.GONE);
            }
            if("1".equals(cardStatus.substring(2,3))){
                //第三位1完善信用卡有效期
                lay_date.setVisibility(VISIBLE);
            }else{
                lay_date.setVisibility(View.GONE);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public boolean checkInput() {
        if(VISIBLE==lay_cvn.getVisibility()){
            String edt_cvnText= edt_cvn.getText().toString();
            if(TextUtils.isEmpty(edt_cvnText)){
                LogUtil.showToast(BindedCardOpenQuickPay.this,"请正确输入安全码!");
                return false;
            }
        }

        if(VISIBLE==lay_mobile.getVisibility()){
            String  edt_mobileText=edt_mobile.getText().toString();
            if(TextUtils.isEmpty(edt_mobileText)||edt_mobileText.length()!=11){
                LogUtil.showToast(BindedCardOpenQuickPay.this,"请正确输入手机号!");
                return false;
            }
        }
        if(VISIBLE==lay_date.getVisibility()){
            String  edt_validateText=edt_validate.getText().toString();
            if(TextUtils.isEmpty(edt_validateText)){
                LogUtil.showToast(BindedCardOpenQuickPay.this,"请正确输入有效期!");
                return false;
            }
        }
        if(!cb_agree.isChecked()){
            LogUtil.showToast(BindedCardOpenQuickPay.this,"请同意快捷支付开通协议!");
            return false;
        }
        return true;
    }

    private void initData() {
        edt_validate.setInputType(InputType.TYPE_CLASS_PHONE);
        edt_validate.setFilters(filters);
        String  expirydate=edt_validate.getText().toString();
        if (!TextUtils.isEmpty(expirydate)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMM");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMyy");
            simpleDateFormat.setLenient(false);
            try {
                Date expiry = simpleDateFormat.parse(expirydate);
                month = 0;
                year = 0;
                edt_validate.setText(simpleDateFormat2.format(expiry));
                fullLength = (expirydate.length() >= 4);
                edt_validate.setEnabled(false);
                edt_validate.setFocusable(false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        edt_validate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                month = 0;
                year = 0;
                fullLength = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fullLength = (s.length() >= 5);
//                changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String datestr = s.toString();
                if (datestr == null)
                    return;
                Date expiry = getDateForString(datestr);
                if (expiry == null)
                    return;
                month = expiry.getMonth() + 1;
                year = expiry.getYear();
                if (year < 1900)
                    year += 1900;

            }
        });
//        changeButtonState();
    }

    public static Date getDateForString(String dateString) {
        String digitsOnly = getDigitsOnlyString(dateString);
        SimpleDateFormat validDate = getDateFormatForLength(digitsOnly.length());
        if (validDate != null)
            try {
                validDate.setLenient(false);
                Date date = validDate.parse(digitsOnly);
                return date;
            } catch (ParseException pe) {
                return null;
            }
        return null;
    }
    public static String getDigitsOnlyString(String numString) {
        StringBuilder sb = new StringBuilder();
        for (char c : numString.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    public static SimpleDateFormat getDateFormatForLength(int len) {
        if (len == 4) {
            return new SimpleDateFormat("MMyy");
        } else if (len == 6) {
            return new SimpleDateFormat("MMyyyy");
        } else
            return null;
    }
    @Click(R.id.tv_pay_agree)
    public void payAgreeClick(){
        //快捷支付协议
        Intent intent = new Intent(BindedCardOpenQuickPay.this, HtmlMessageActivity_.class);
        intent.putExtra("title", "快捷支付开通协议");
        intent.putExtra("ccurl", "");
        intent.putExtra("urlkey", RyxAppconfig.Notes_FastAgreement);
        startActivity(intent);
    }
}
