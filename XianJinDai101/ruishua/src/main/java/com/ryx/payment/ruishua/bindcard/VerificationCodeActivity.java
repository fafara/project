package com.ryx.payment.ruishua.bindcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.PaymentSuccessful_;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 单纯公共验证码验证页面
 */
@EActivity(R.layout.activity_verification_code)
public class VerificationCodeActivity extends BaseActivity {
    @ViewById
    TextView tv_mac_tip,tv_again_code;
    @ViewById
    EditText et_mac_code,edt_cvn;
    @ViewById
    Button btn_verification_done;
    @ViewById
    AutoRelativeLayout lay_cvn;
    @ViewById
    RelativeLayout mac_codelayout;
    @ViewById
    View mac_code_layout_bootomview;
    private Timer mTimer = new Timer();
   private Bundle bundle;
    private String flag="";
    private String systemno,needcvn2,needsms;
    @AfterViews
    public void initView(){
        setTitleLayout(RyxAppdata.getInstance(VerificationCodeActivity.this).getCurrentBranchName(),true,false);
    bundle= getIntent().getBundleExtra("CodeData");
        initQtPatParams();
    if(bundle!=null){
        setTitleLayout(bundle.getString("title"),true,false);
        flag=  bundle.getString("flag");
        branch(flag,bundle);
    }else{
        LogUtil.showToast(VerificationCodeActivity.this,"数据有误!");
    }

    }

    /**
     * 根据标志来验证
     * @param flag
     * @param bundle
     */
    private void branch(String flag,Bundle bundle){
        if("BindedCardOpenQuickPay".equals(flag)){
            //
            tv_mac_tip.setVisibility(View.VISIBLE);
            mac_codelayout.setVisibility(View.VISIBLE);
            mac_code_layout_bootomview.setVisibility(View.GONE);
            lay_cvn.setVisibility(View.GONE);
            tv_again_code.setVisibility(View.INVISIBLE);

            String phoneNum=bundle.getString("toPhoneNum");
            tv_mac_tip.setText(getString(R.string.tv_verification_code, phoneNum));
            systemno=bundle.getString("systemno","");
        }else if("QuickPaymentSMS".equals(flag)){
                //快捷支付支付所需验证码和CV2码
            systemno=bundle.getString("systemno","");
            needsms=bundle.getString("needsms","");
            needcvn2=bundle.getString("needcvn2","");
            if("1".equals(needsms)){
                //需要短信
                tv_mac_tip.setVisibility(View.VISIBLE);
                mac_codelayout.setVisibility(View.VISIBLE);
                mac_code_layout_bootomview.setVisibility(View.VISIBLE);
                String phoneNum=bundle.getString("toPhoneNum");
                tv_mac_tip.setText(getString(R.string.tv_verification_code, phoneNum));
            }
            if("1".equals(needcvn2)){
                //需要CV2码
                lay_cvn.setVisibility(View.VISIBLE);
            }
        }


        else  if("DebitCardAdd".equals(flag)){
            //储蓄卡新增

        }else if("DebitCard".equals(flag)){
            //储蓄卡添加

        }else if("CreditCard".equals(flag)){
            //信用卡添加


        }

    }
    @Click(R.id.tv_again_code)
    public void againCode(){
        startCountdown();

    }
    /**
     * 开始倒计时60秒
     */
    public void startCountdown() {
        TimerTask task = new TimerTask() {
            int secondsRremaining = 60;

            public void run() {
                Message msg = new Message();
                msg.what = secondsRremaining--;
                timeHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(task, 1000, 1000);
    }
    Handler timeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what > 0) {
                tv_again_code.setTextColor(ContextCompat.getColor(VerificationCodeActivity.this, R.color.text_a));
                tv_again_code.setText(getResources().getString(R.string.resend) + "(" + msg.what + ")");
                tv_again_code.setClickable(false);
            } else {
                mTimer.cancel();
                tv_again_code.setText(getResources().getString(R.string.resend_verification_code));
                tv_again_code.setClickable(true);
                tv_again_code.setTextColor(ContextCompat.getColor(VerificationCodeActivity.this, R.color.colorPrimary));
            }
        }
    };
    /**
     * 验证码确认
     */
    @Click(R.id.btn_verification_done)
    public void verificationDone(){
        if("BindedCardOpenQuickPay".equals(flag)){
            if(checkCode()){
                String code= et_mac_code.getText().toString();
                qtpayApplication.setValue("QuickPaymentBindCardSMS.Req");
                qtpayAttributeList.add(qtpayApplication);
                qtpayParameterList.add(new Param("code",code));
                qtpayParameterList.add(new Param("systemno",systemno));
                httpsPost("QuickPaymentBindCardSMSTag", new XmlCallback() {
                    @Override
                    public void onTradeSuccess(RyxPayResult payResult) {
                        LogUtil.showToast(VerificationCodeActivity.this, "交易成功");
                        //请求验证
                        setResult(RyxAppconfig.CLOSE_ALL);
                        finish();
                    }

                    @Override
                    public void onOtherState() {
                        super.onOtherState();
                        finish();
                    }

                    @Override
                    public void onTradeFailed() {
                        super.onTradeFailed();
                        finish();
                    }

                    @Override
                    public void onLoginAnomaly() {
                        super.onLoginAnomaly();
                        finish();
                    }
                });
            }
        }else if("QuickPaymentSMS".equals(flag)){

            if("1".equals(needsms)&&!checkCode()){
                return;
            }
            if("1".equals(needcvn2)&&!checkCvn2Code()){
                return;
            }
            qtpayApplication.setValue("QuickPaymentSMS.Req");
            qtpayAttributeList.add(qtpayApplication);
            if ("1".equals(needsms)){
                String code= et_mac_code.getText().toString();
                qtpayParameterList.add(new Param("code",code));
            }
            if ("1".equals(needcvn2)){
                String cvn2code= edt_cvn.getText().toString();
                qtpayParameterList.add(new Param("cvn2",cvn2code));
            }
            qtpayParameterList.add(new Param("systemno",systemno));
            httpsPost("QuickPaymentSMSTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    LogUtil.showToast(VerificationCodeActivity.this, "交易成功");
                    Intent intent=new Intent(VerificationCodeActivity.this,PaymentSuccessful_.class);
                    intent.putExtra("flag","CLOSEALL");
                    startActivityForResult(intent,0x002);
                }

                @Override
                public void onLoginAnomaly() {
                    super.onLoginAnomaly();
                    setResult(RyxAppconfig.CLOSE_STACK_ALL);
                    finish();
                }

                @Override
                public void onTradeFailed() {
                    super.onTradeFailed();
                    setResult(RyxAppconfig.CLOSE_STACK_ALL);
                    finish();
                }

                @Override
                public void onOtherState(String code,String resDsc) {
                    if("9101".equals(code)){
                        Intent intent=new Intent(VerificationCodeActivity.this,PaymentSuccessful_.class);
                        intent.putExtra("flag","CLOSEALL");
                        startActivityForResult(intent,0x002);
                    }else{
                        setResult(RyxAppconfig.CLOSE_STACK_ALL);
                        finish();
                    }
                }
            });



        }
    }

    /**
     *验证码验证
     * @return
     */
    public boolean checkCode(){
       String code= et_mac_code.getText().toString();
        if(TextUtils.isEmpty(code)){
            LogUtil.showToast(VerificationCodeActivity.this,"请正确填写验证码!");
            return false;
        }
       return true;
    }

    /**
     * 校验CVN2
     * @return
     */
    public boolean checkCvn2Code(){
        String cvn2code= edt_cvn.getText().toString();
        if(TextUtils.isEmpty(cvn2code)){
            LogUtil.showToast(VerificationCodeActivity.this,"请正确填写安全码!");
            return false;
        }
        return true;
    }

    @Override
    protected void backUpImgOnclickListen() {
        if("QuickPaymentSMS".equals(flag))
        {
            setResult(RyxAppconfig.CLOSE_STACK_ALL);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if("QuickPaymentSMS".equals(flag))
            {
                setResult(RyxAppconfig.CLOSE_STACK_ALL);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
