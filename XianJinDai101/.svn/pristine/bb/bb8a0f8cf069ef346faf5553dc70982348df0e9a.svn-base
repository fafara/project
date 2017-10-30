package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.bean.MsgInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
@EActivity(R.layout.act_login_verification_code)
public class LoginVerificationCodeAct extends LoginBaseAct {
    @ViewById
    TextView tv_content;
    @ViewById
    EditText verificationCode_edit1,verificationCode_edit2,verificationCode_edit3,verificationCode_edit4;
    List<EditText> verificationCodeEditList=new ArrayList<>();
    @ViewById
    Button bt_send,bt_countdown;
    public static int FINISHSELF=0x002;
    private String phone,paswd,phoneModel,phoneMsg;
    private ArrayList<MsgInfo> noticeTempOldList = new ArrayList<MsgInfo>();
    private ArrayList<MsgInfo> noticeTempNewList = new ArrayList<MsgInfo>();
    @AfterViews
    public void initView(){
    setTitleLayout("验证码验证",true,false);
        initQtPatParams();
        initIntentData();
        tv_content.setText("已发送至"+phone);
        initVerificationView();
        startCountdown();
    }
private void initVerificationView(){
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
            } else {
                focus();
                checkAndCommit();
            }

        }

    };
    verificationCodeEditList.add(verificationCode_edit1);
    verificationCodeEditList.add(verificationCode_edit2);
    verificationCodeEditList.add(verificationCode_edit3);
    verificationCodeEditList.add(verificationCode_edit4);

    for(int i=0;i<verificationCodeEditList.size();i++){
        EditText editText= verificationCodeEditList.get(i);
        editText.addTextChangedListener(textWatcher);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        editText.setOnKeyListener(onKeyListener);
    }
}

    View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public synchronized boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                backFocus();
            }
            return false;
        }
    };
    private void focus() {
        for(int i=0;i<verificationCodeEditList.size();i++){
            EditText editText= verificationCodeEditList.get(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    private void backFocus() {
        int count = verificationCodeEditList.size();
        EditText editText ;
        for (int i = count-1; i>= 0; i--) {
            editText = verificationCodeEditList.get(i);
            if (editText.getText().length() == 1) {
                editText.requestFocus();
                editText.setSelection(1);
                return;
            }
        }
    }


    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0 ;i < verificationCodeEditList.size(); i++){
            EditText editText = verificationCodeEditList.get(i);
            String content = editText.getText().toString();
            if ( content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }

        }
        if (full){
            doLogin(stringBuilder.toString());
        }
    }
    private void initIntentData() {
    Intent intent=getIntent();
        if(intent.hasExtra("phone")){
            phone= intent.getStringExtra("phone");
        }
        if(intent.hasExtra("paswd")){
            paswd= intent.getStringExtra("paswd");
        }
        if(intent.hasExtra("phoneModel")){
            phoneModel= intent.getStringExtra("phoneModel");
        }
        if(intent.hasExtra("phoneMsg")){
            phoneMsg= intent.getStringExtra("phoneMsg");
        }
    }
    private void doLogin(String content) {

        qtpayApplication.setValue("UserLogin.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("password",paswd));
        qtpayParameterList.add(new Param("verifiCode",content));
            qtpayParameterList.add(new Param("phoneModel", phoneModel));
        qtpayParameterList
                .add(new Param("phoneMsg", phoneMsg));

        httpsPost("UserLoginTag", new XmlCallback() {
            @Override
            public void onLoginAnomaly() {

            }
            @Override
            public void onTradeSuccess(RyxPayResult qtpayResult) {
                LogUtil.showToast(LoginVerificationCodeAct.this, "登录成功！");
                doRSAnalyze(phone,qtpayResult);
            }

            @Override
            public void onOtherState(String code,String msg) {
                RyxAppdata.getInstance(LoginVerificationCodeAct.this).resetCurrentBranchConfig();
            }

            @Override
            public void onTradeFailed() {
                RyxAppdata.getInstance(LoginVerificationCodeAct.this).resetCurrentBranchConfig();
            }
        });

    }

    @Click(R.id.bt_send)
    public void BtnSendMsg(){
        qtpayApplication.setValue("SendAdvancedVipSMS.Req");
        Param phoneParam=new Param("bankTel");
        phoneParam.setValue(phone);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(phoneParam);
        qtpayParameterList.add(new Param("type","deviceToken"));
        httpsPost("SendAdvancedVipSMSTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                //获取验证码成功
                Toast.makeText(LoginVerificationCodeAct.this, getResources().getString(R.string.sms_has_been_issued_please_note_that_check), Toast.LENGTH_SHORT).show();
            }
        });
        startCountdown();
    }
    @Override
    protected void backUpImgOnclickListen() {
        if(mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }

        super.backUpImgOnclickListen();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
//            setResult(RyxAppconfig.CLOSE_AT_SWIPER);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Timer mTimer = new Timer();
    Handler timeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what > 0) {
                bt_countdown.setText(getResources().getString(R.string.resend)
                        + "(" + msg.what + "s)");
                bt_send.setVisibility(View.GONE);
                bt_countdown.setVisibility(View.VISIBLE);
            } else {
                if(mTimer!=null){
                    mTimer.cancel();
                    mTimer=null;
                }
                bt_send.setVisibility(View.VISIBLE);
                bt_countdown.setVisibility(View.GONE);
            }
        }
    };
    /**
     * 开始倒计时60秒
     */

    public void startCountdown() {
        if(mTimer==null){
            mTimer=new Timer();
        }
        TimerTask task = new TimerTask() {
            int secondsRremaining = 59;
            public void run() {
                Message msg = Message.obtain();
                msg.what = secondsRremaining--;
                timeHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(task, 1000, 1000);
    }



}
