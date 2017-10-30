package com.ryx.payment.ruishua.convenience;

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

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.VerificationCodeActivity_;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.ExpiryFilter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 * 扫码快捷支付资料补充
 */
@EActivity(R.layout.activity_sweep_quick_pay_data_replenish)
public class SweepQuickPayDataReplenishActivity extends PayStateCheckBaseActivity {
String needcvn2,needphone,needexpiredate,needsms,orderId;
    @ViewById
    TextView tv_orderid,tv_money,tv_account,tv_fukuanAccount;
    @ViewById
    View lay_cvn,lay_mobile,lay_date,smscode_layou,btn_next;
    @ViewById
    EditText edt_cvn,edt_mobile,edt_validate,et_smscode;
    InputFilter[] filters = {new ExpiryFilter()};
    private int month;
    private int year;
    private boolean fullLength = false;
    @AfterViews
    public void afterView(){
        setTitleLayout("资料补充",true,false);
        initQtPatParams();
        initView();
        initData();
    }

    private  void initView(){
        orderId= getIntent().getStringExtra("orderId");
       String amount=  getIntent().getStringExtra("amount");
        String toPayuserName=  getIntent().getStringExtra("toPayuserName");
        String  account=  getIntent().getStringExtra("account");///选中的支付卡号即付款卡号
        String  mobileno= getIntent().getStringExtra("mobileno");////收款人手机号
        String  skaccount= getIntent().getStringExtra("skaccount");
//        String fkphone= getIntent().getStringExtra("fkphone");

        tv_money.setText(amount);
        tv_orderid.setText(orderId);
        tv_account.setText(mobileno+"  "+toPayuserName);
        tv_fukuanAccount.setText(StringUnit.cardJiaMi(account));


        needcvn2= getIntent().getStringExtra("needcvn2");
        needphone= getIntent().getStringExtra("needphone");
        needexpiredate= getIntent().getStringExtra("needexpiredate");
        needsms= getIntent().getStringExtra("needsms");
        lay_cvn.setVisibility(View.GONE);
        lay_mobile.setVisibility(View.GONE);
        lay_date.setVisibility(View.GONE);
        smscode_layou.setVisibility(View.GONE);

        if("1".equals(needcvn2)){
            lay_cvn.setVisibility(View.VISIBLE);
        }
        if("1".equals(needphone)){
            lay_mobile.setVisibility(View.VISIBLE);

        }
        if("1".equals(needexpiredate)){
            lay_date.setVisibility(View.VISIBLE);

        }
        if("1".equals(needsms)){
            smscode_layou.setVisibility(View.VISIBLE);

        }

    }
    @Click(R.id.btn_next)
    public void btnNextClick(View view){
        disabledTimerView(view);
        String edt_cvnVal="",edt_mobileVal="",edt_validateVal="",et_smscodeVal="";
        if("1".equals(needcvn2)){
            edt_cvnVal=  edt_cvn.getText().toString();
            if(TextUtils.isEmpty(edt_cvnVal)){
                LogUtil.showToast(SweepQuickPayDataReplenishActivity.this,"请正确填写安全码!");
                return;
            }
        }
        if("1".equals(needphone)){
            edt_mobileVal=  edt_mobile.getText().toString();
            if(TextUtils.isEmpty(edt_mobileVal)||edt_mobileVal.length()!=11){
                LogUtil.showToast(SweepQuickPayDataReplenishActivity.this,"请正确填写银行预留手机号!");
                return;
            }
        }
        if("1".equals(needexpiredate)){
            edt_validateVal=  edt_validate.getText().toString();
            if(TextUtils.isEmpty(edt_validateVal)){
                LogUtil.showToast(SweepQuickPayDataReplenishActivity.this,"请正确填写有效期!");
                return;
            }
        }
        if("1".equals(needsms)){
            et_smscodeVal=  et_smscode.getText().toString();
            if(TextUtils.isEmpty(et_smscodeVal)){
                LogUtil.showToast(SweepQuickPayDataReplenishActivity.this,"请正确填写短信验证码!");
                return;
            }
        }
        qtpayApplication.setValue("QuickPaymentPay.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("orderId",orderId));
        if(!TextUtils.isEmpty(edt_cvnVal)){
            qtpayParameterList.add(new Param("cvn2",edt_cvnVal));
        }
        if(!TextUtils.isEmpty(edt_validateVal)){
            qtpayParameterList.add(new Param("validDate",edt_validateVal));
        }
        if(!TextUtils.isEmpty(et_smscodeVal)){
            qtpayParameterList.add(new Param("smsCode",et_smscodeVal));
        }
        if(!TextUtils.isEmpty(edt_mobileVal)){
            qtpayParameterList.add(new Param("bankPhone",edt_mobileVal));
        }
        httpsPost("QuickPaymentPaytag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                String resultdata=payResult.getData();
//                if(!TextUtils.isEmpty(resultdata)){
                    try {
                        JSONObject resultDataObj=new JSONObject(resultdata);
                        String rscode=  JsonUtil.getValueFromJSONObject(resultDataObj,"code");
                        if("0000".equals(rscode)){
                            //支付成功
                            LogUtil.showToast(SweepQuickPayDataReplenishActivity.this, "交易成功");
                            Intent intent=new Intent(SweepQuickPayDataReplenishActivity.this,PaymentSuccessful_.class);
                            intent.putExtra("flag","CLOSEALL");
                            startActivityForResult(intent,0x002);
                        }else if("9101".equals(rscode)){
                            //支付结果未知轮询
                            JSONObject resultObj=   JsonUtil.getJSONObjectFromJsonObject(resultDataObj,"result");
                            String orderId=  JsonUtil.getValueFromJSONObject(resultObj,"systemno");
                            showLoadingStateDialog(orderId);
                        }else if("9001".equals(rscode)){
                         //同步返回结果明确失败
                            String msg=  JsonUtil.getValueFromJSONObject(resultDataObj,"msg");
                            LogUtil.showToast(SweepQuickPayDataReplenishActivity.this, msg);
                            setResult(RyxAppconfig.CLOSE_STACK_ALL);
                            finish();
                        } else{
                            String needsms=  JsonUtil.getValueFromJSONObject(resultDataObj,"needsms");
                            String needcvn2=  JsonUtil.getValueFromJSONObject(resultDataObj,"needcvn2");
                            String phonenumber=  JsonUtil.getValueFromJSONObject(resultDataObj,"phonenum");
                            String systemno=  JsonUtil.getValueFromJSONObject(resultDataObj,"systemno");
                            if("1".equals(needsms)||"1".equals(needcvn2)){
                                Intent intent = new Intent(SweepQuickPayDataReplenishActivity.this, VerificationCodeActivity_.class);
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
                                String msg=  JsonUtil.getValueFromJSONObject(resultDataObj,"msg");
//                                if(("0".equals(needsms)&&"0".equals(needcvn2))||((TextUtils.isEmpty(needsms)&&TextUtils.isEmpty(needcvn2)))) {
//                                    LogUtil.showToast(SweepQuickPayDataReplenishActivity.this, "交易成功");
//                                    Intent intent=new Intent(SweepQuickPayDataReplenishActivity.this,PaymentSuccessful_.class);
//                                    intent.putExtra("flag","CLOSEALL");
//                                    startActivityForResult(intent,0x002);
                                LogUtil.showToast(SweepQuickPayDataReplenishActivity.this, msg);
                                setResult(RyxAppconfig.CLOSE_STACK_ALL);
                                finish();

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.showToast(SweepQuickPayDataReplenishActivity.this, "确认订单返回信息有误!");
                        setResult(RyxAppconfig.CLOSE_STACK_ALL);
                        finish();
                    }
//                }else{
//                    LogUtil.showToast(SweepQuickPayDataReplenishActivity.this, "交易成功!");
//                    Intent intent=new Intent(SweepQuickPayDataReplenishActivity.this,PaymentSuccessful_.class);
//                    intent.putExtra("flag","CLOSEALL");
//                    startActivityForResult(intent,0x002);
//                }
            }

//            @Override
//            public void onOtherState(String resCode,String resDesc) {
//                if("9101".equals(resCode)){
//                    Intent intent=new Intent(SweepQuickPayDataReplenishActivity.this,PaymentSuccessful_.class);
//                    intent.putExtra("flag","CLOSEALL");
//                    startActivityForResult(intent,0x002);
//                }else{
//                    finish();
//                }
//            }
        });
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
    private void initData() {
        edt_validate.setInputType(InputType.TYPE_CLASS_PHONE);
        edt_validate.setFilters(filters);

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

    /**
     * 执行轮询查询结果
     * @param orderId
     * @param number
     */
    @Override
    public void initiativeSaoMaPayStateCheckTag(String orderId, final int number ){
        qtpayApplication.setValue("QuickPayStateCheck.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("orderId",orderId));
        httpsPost(false, false, "QuickPayStateCheckTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {

                try {
                    JSONObject jsonObject=new JSONObject(payResult.getData());
                    String code=  JsonUtil.getValueFromJSONObject(jsonObject,"code");
                    String msg=  JsonUtil.getValueFromJSONObject(jsonObject,"msg");
                    if("9001".equals(code)){
                        //明确失败
                        if(loadingStateDialog!=null){
                            loadingStateDialog.setImgState(3);
                            loadingStateDialog.setStatusContent("查询结果失败,请稍后再试");
                        }

                    }else if(number==1&&"9101".equals(code)&&loadingStatetimer!=null){

                        loadingStatetimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(2);
                            }
                        }, 10000);
                    }else if(number==2&&"9101".equals(code)){
                        //第二次查询无结果
                        if(loadingStateDialog!=null){
                            loadingStateDialog.setImgState(3);
                            loadingStateDialog.setStatusContent(msg);
                        }

                    }else if("0000".equals(code)){
                        LogUtil.showToast(SweepQuickPayDataReplenishActivity.this,msg);
                        if(loadingStateDialog!=null){
                            loadingStateDialog.setImgState(2);
                            loadingStateDialog.setStatusContent(msg);
                        }
                    }
                    else{
                        LogUtil.showToast(SweepQuickPayDataReplenishActivity.this,msg);
                        setResult(RyxAppconfig.CLOSE_STACK_ALL);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                if(loadingStatetimer!=null){
                    loadingStatetimer.cancel();
                    loadingStatetimer=null;
                }
                if(loadingStateDialog!=null){
                    loadingStateDialog.setImgState(3);
                    loadingStateDialog.setStatusContent("查询结果失败,请稍后再试");
                }
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                if(loadingStatetimer!=null){
                    loadingStatetimer.cancel();
                    loadingStatetimer=null;
                }
                if(loadingStateDialog!=null){
                    loadingStateDialog.setImgState(3);
                    loadingStateDialog.setStatusContent("查询结果失败,请稍后再试");
                }
            }

            @Override
            public void onLoginAnomaly() {
                super.onLoginAnomaly();
                if(loadingStatetimer!=null){
                    loadingStatetimer.cancel();
                    loadingStatetimer=null;
                }
                if(loadingStateDialog!=null){
                    loadingStateDialog.setImgState(3);
                    loadingStateDialog.setStatusContent("查询结果失败,请稍后再试");
                }
            }

        });
    }


    /**
     * 交易成功进行信息提示，此处直接进行页面跳转即可
     * @param message
     */
    @Override
    public void showMsgDialog(String message,boolean isOnlyOk){
        LogUtil.showToast(SweepQuickPayDataReplenishActivity.this, "交易成功");
        Intent intent=new Intent(SweepQuickPayDataReplenishActivity.this,PaymentSuccessful_.class);
        intent.putExtra("flag","CLOSEALL");
        startActivityForResult(intent,0x002);
//        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(SweepQuickPayDataReplenishActivity.this, new ConFirmDialogListener() {
//
//            @Override
//            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
//                ryxSimpleConfirmDialog.dismiss();
//                finish();
//            }
//
//            @Override
//            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
//
//
//                ryxSimpleConfirmDialog.dismiss();
//            }
//        });
//        ryxSimpleConfirmDialog.show();
//        ryxSimpleConfirmDialog.setContent(message);
//        ryxSimpleConfirmDialog.setCancelable(false);
//        ryxSimpleConfirmDialog.setOkbtnText("退出");
//        ryxSimpleConfirmDialog.setCancelbtnText("重扫");
//        ryxSimpleConfirmDialog.setContentgravity(Gravity.CENTER);
//        if(isOnlyOk){
//            ryxSimpleConfirmDialog.setOnlyokLinearlayout();
//        }
    }
    @Override
    public void loadingStatefinishSelf(){
        setResult(RyxAppconfig.CLOSE_STACK_ALL);
        finish();
    }
}
