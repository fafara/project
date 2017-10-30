package com.ryx.ryxpay.activity;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qtpay.qtjni.QtPayEncode;
import com.ryx.ryxpay.R;
import com.ryx.ryxpay.RyxAppconfig;
import com.ryx.ryxpay.RyxAppdata;
import com.ryx.ryxpay.bean.Param;
import com.ryx.ryxpay.bean.RyxPayResult;
import com.ryx.ryxpay.net.XmlCallback;
import com.ryx.ryxpay.utils.DataUtil;
import com.ryx.ryxpay.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/5/4.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    Timer timer = new Timer();
    Handler  timehandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what>0){
                getcode_btn.setText("重新发送(" + msg.what+")");
                getcode_btn.setClickable(false);
            }else{
                timer.cancel();
                timer=null;
                getcode_btn.setText("重新发送验证码");
                getcode_btn.setClickable(true);
            }
        };
    };
    @ViewById
    Button register_btn;//注册按钮

    @ViewById
    TextView contract_txt;//服务协议

    @ViewById
    EditText reg_phoneet;//手机号
    @ViewById
    ImageView agree_checkbox;
    @ViewById
    Button getcode_btn;
    @ViewById
    ImageView show_pwd;
    @ViewById
    ImageView close_btn;
    @ViewById
    EditText pwd_edt,check_code_edttxt,pwd_edt_confirm;
    @ViewById
    ImageView show_pwd_confirm;

    @DrawableRes
    Drawable register_check_contract_img;
    @DrawableRes
    Drawable register_unchecked_contract;
    @DrawableRes
    Drawable register_hide_password, register_show_password;

    private boolean isAgree = false, showPwd = false,showConfirmPwd;

    @AfterViews
    public void initViews() {
        initQtPatParams();
    }

    //以下控件点击响应事件
    @Click(R.id.register_btn)
    public void setRegister_btn() {

        if(checkMessage()){
            String phonePwd=pwd_edt.getText().toString().trim();
            String phone=reg_phoneet.getText().toString().trim();
            qtpayApplication.setValue("UserRegister.Req");
            qtpayAttributeList.add(qtpayApplication);
            qtpayPhone.setValue(phone);
            qtpayMobileNO.setValue(phone);
            Param  qtpayMobileMac=    new Param("mobileMac",check_code_edttxt.getText().toString().trim());// 短信验证码
            Param   qtpayUserName = new Param("userName",phone);
            Param qtpayPassword = new Param("password", QtPayEncode.encryptUserPwd(phonePwd, phone, RyxAppconfig.DEBUG));
            Param qtpayReferrerMobileNo = new Param("referrerMobileNo","");
            qtpayParameterList.add(qtpayUserName);
            qtpayParameterList.add(qtpayPassword);
            qtpayParameterList.add(qtpayReferrerMobileNo);
            qtpayParameterList.add(qtpayMobileMac);

            httpsPost("setRegisterTag", new XmlCallback() {

                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    LogUtil.showToast(RegisterActivity.this,"注册成功");
                    finish();
                }

                @Override
                public void onLoginAnomaly() {

                }

                @Override
                public void onOtherState() {

                }

                @Override
                public void onTradeFailed() {

                }
            });
        }
    }

    @Click(R.id.contract_txt)
    public void setContract_txt() {
    }
    @Click(R.id.show_pwd_confirm)
    public void pwdConfirm(){
        if (showConfirmPwd) {
            showConfirmPwd = false;
            show_pwd_confirm.setBackgroundResource(R.drawable.register_hide_password);
            pwd_edt_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            showConfirmPwd = true;
            show_pwd_confirm.setBackgroundResource(R.drawable.register_show_password);
            pwd_edt_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }
    }


    @Click(R.id.agree_checkbox)
    public void setAgree_checkbox() {
        if (isAgree) {
            isAgree = false;
            agree_checkbox.setBackgroundResource(R.drawable.register_unchecked_contract);

        } else {
            isAgree = true;
            agree_checkbox.setBackgroundResource(R.drawable.register_check_contract_img);

        }
    }

    @Click(R.id.show_pwd)
    public void setShow_pwd() {
        if (showPwd) {
            showPwd = false;
            show_pwd.setBackgroundResource(R.drawable.register_hide_password);
            pwd_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            showPwd = true;
            show_pwd.setBackgroundResource(R.drawable.register_show_password);
            pwd_edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }
    }

    @Click(R.id.getcode_btn)
    public void setGetcode_btn() {
        String phoneNumber = reg_phoneet.getText().toString().trim();
        if (!DataUtil.isMobileNO(phoneNumber)) {
            Toast.makeText(RegisterActivity.this, "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        RyxAppdata.getInstance(RegisterActivity.this).setPhone(phoneNumber);
        RyxAppdata.getInstance(RegisterActivity.this).setMobileNo(phoneNumber);
        qtpayApplication.setValue("GetMobileMac.Req");
        Param qtpayAppType = new Param("appType", "UserRegister");
        Param qtpayOrderId = new Param("orderId", "");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayAppType);
        qtpayParameterList.add(qtpayOrderId);
        startCountdown();
        httpsPost("GetMobileMacTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(RegisterActivity.this,"验证码发送成功");
            }

            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public void onOtherState() {

            }

            @Override
            public void onTradeFailed() {

            }
        });
    }
    @Click(R.id.close_btn)
    public void setClose_btn() {
        finish();
    }
    /**
     * 开始倒计时60秒
     */
    public  void  startCountdown(){
        TimerTask task = new TimerTask() {
            int  secondsRremaining=60;
            public void run() {
                Message msg = new Message();
                msg.what = secondsRremaining--;
                timehandler.sendMessage(msg);
            }
        };
        if(timer==null){
            timer=new Timer();
        }
        timer.schedule(task, 1000,1000);
    }

    /**
     * 输入信息检查
     */
    private boolean checkMessage(){
        String regPhoneStr=reg_phoneet.getText().toString().trim();
        if(!DataUtil.isMobileNO(regPhoneStr)){
            LogUtil.showToast(RegisterActivity.this,"请输入有效的手机号码");
            return false;
        }
        String phoneCode=check_code_edttxt.getText().toString().trim();
        if (TextUtils.isEmpty(phoneCode)||phoneCode.length()!=4){
            LogUtil.showToast(RegisterActivity.this,"请正确输入验证码");
            return false;
        }
        String pwd_edtStr= pwd_edt.getText().toString().trim();
        String pwd_edtConfirmStr = pwd_edt_confirm.getText().toString().trim();
        if(TextUtils.isEmpty(pwd_edtStr)){
            LogUtil.showToast(RegisterActivity.this,"请正确输入密码");
            return false;
        }
        if(TextUtils.isEmpty(pwd_edtConfirmStr)||!pwd_edtConfirmStr.equals(pwd_edtStr)){
            LogUtil.showToast(RegisterActivity.this,"两次输入的密码不一致");
            return false;
        }
        if(!isAgree){
            LogUtil.showToast(RegisterActivity.this,"请先同意服务协议");
            return false;
        }
        if(TextUtils.isEmpty(pwd_edtStr)){
            LogUtil.showToast(RegisterActivity.this,"请正确输入密码");
            return false;
        }
        return true;
    }
}
