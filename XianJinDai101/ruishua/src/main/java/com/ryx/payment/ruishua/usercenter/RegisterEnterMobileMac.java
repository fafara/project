package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtpay.qtjni.QtPayEncode;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_register_enter_mobile_mac)
public class RegisterEnterMobileMac extends BaseActivity {

    private String mPhoneNumber;
    private String code;
    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.tv_register_mac_tip)
    TextView mMacTip;
    @ViewById(R.id.register_et_mac_code)
    EditText mMacNumber;
    @ViewById(R.id.tv_again_mac)
    TextView mAgainSendMac;
    @ViewById(R.id.btn_register_done)
    Button mRegisterDone;
    private Timer mTimer = new Timer();
    private Param qtpayAppType;
    private Param qtpayOrderId;
    private Param qtpayNewPassword;
    private Param qtpayMobileMac;
    private String mPassword;

    @AfterViews
    public void initViews() {
        setTitleLayout("注册");
        mBackImg.setVisibility(View.VISIBLE);
        mMsgImg.setVisibility(View.GONE);
        initQtPatParams();
        startCountdown();//开始倒计时
        mPhoneNumber = getIntent().getStringExtra("phonenumber");
        code = getIntent().getStringExtra("code");
        mPassword = getIntent().getStringExtra("password");
        mMacTip.setText(getString(R.string.tv_register_mac_tip, mPhoneNumber));
        String string = mMacTip.getText().toString().trim();
        ColorStateList redColors = ColorStateList.valueOf(Color.parseColor("#1db7f0"));
        SpannableStringBuilder spannable = new SpannableStringBuilder(string);
        spannable.setSpan(new TextAppearanceSpan(null, 0, 0, redColors, null), 6, 17, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mMacTip.setText(spannable);
        mMacNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                mMacNumber.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayOrderId = new Param("orderId", "");
        qtpayNewPassword = new Param("newPassword");
        qtpayMobileMac = new Param("mobileMac");// 短信验证码
    }

    @Click(R.id.tileleftImg)
    public void setBackClick() {
        finish();
    }

    @Click(R.id.tv_again_mac)
    public void sendMacAgain() {
        mTimer = null;
        mTimer = new Timer();
        //重新建立网络请求
        QtpayAppData.getInstance(RegisterEnterMobileMac.this).setPhone(
                mPhoneNumber);
        QtpayAppData.getInstance(RegisterEnterMobileMac.this).setMobileNo(
                mPhoneNumber);
        getMobileMac();
        startCountdown(); // 开始倒计时
    }

    private void getMobileMac() {
        qtpayApplication.setValue("GetMobileMac.Req");
        qtpayAppType = new Param("appType", "UserRegister");
        qtpayOrderId = new Param("orderId", "");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayAppType);
        qtpayParameterList.add(qtpayOrderId);
        httpsPost("getMobileMac", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                if (qtpayApplication.getValue().equals("GetMobileMac.Req")) {
                    LogUtil.showToast(RegisterEnterMobileMac.this,
                            getResources().getString(R.string.sms_has_been_issued_please_note_that_check));
                }
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

    @Click(R.id.btn_register_done)
    public void registerDone() {
        String macNumber = mMacNumber.getText().toString().trim();
        if (TextUtils.isEmpty(macNumber)) {
            LogUtil.showToast(RegisterEnterMobileMac.this,"请输入验证码");
//            mMacNumber.setError("请输入验证码");
            return;
        }
        if (macNumber.length() != 4) {
            LogUtil.showToast(RegisterEnterMobileMac.this,"验证码为4位数字");
//            mMacNumber.setError("验证码为4位数字");
            return;
        }
        doRegister(mPhoneNumber, macNumber);
    }

    /**
     * 注册
     */
    public void doRegister(final String mPhoneNumber, String macNumber) {
        qtpayApplication.setValue("UserRegister.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayPhone.setValue(mPhoneNumber);
        qtpayMobileNO.setValue(mPhoneNumber);
        qtpayMobileMac.setValue(macNumber);// 短信验证码

        Param qtpayUserName = new Param("userName", mPhoneNumber);
        Param qtpayPassword = new Param("password", QtPayEncode.encryptUserPwd(mPassword, mPhoneNumber,
                RyxAppconfig.DEBUG));
        if(RyxAppdata.getInstance(this).getCurrentBranchRegIsNeedCode()){
            Param codeStr = new Param("invitationCode", code);
            qtpayParameterList.add(codeStr);
        }
        Param qtpayReferrerMobileNo = new Param("referrerMobileNo", "");

        qtpayParameterList.add(qtpayUserName);
        qtpayParameterList.add(qtpayPassword);
        qtpayParameterList.add(qtpayReferrerMobileNo);
        qtpayParameterList.add(qtpayMobileMac);

        httpsPost("doUserRegister", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(RegisterEnterMobileMac.this,"恭喜您注册成功.");
                setResult(RyxAppconfig.CLOSE_ALL);
                try {
                    String jsonData=  payResult.getData();
                    JSONObject jsonObject=new JSONObject(jsonData);
                   String recommendStatus= JsonUtil.getValueFromJSONObject(jsonObject,"recommendStatus");
                    if("1".equals(recommendStatus)){
                        Intent intent = new Intent(RegisterEnterMobileMac.this, RegReferrerActivity_.class);
                        intent.putExtra("userName",mPhoneNumber);
                        startActivity(intent);
                    }
                }catch (Exception e){

                }
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

    Handler timeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what > 0) {
                mAgainSendMac.setTextColor(getResources().getColor(R.color.text_a));
                mAgainSendMac.setText(getResources().getString(R.string.resend)
                        + "(" + msg.what + ")");
                mAgainSendMac.setClickable(false);
            } else {
                mTimer.cancel();
                mAgainSendMac.setText(getResources().getString(
                        R.string.resend_verification_code));
                mAgainSendMac.setClickable(true);
                mAgainSendMac.setTextColor(Color.parseColor("#1db7f0"));
            }
        }
    };

    /**
     * 开始倒计时60秒
     */

    public void startCountdown() {
        TimerTask task = new TimerTask() {
            int secondsRremaining = 60;

            public void run() {
                Message msg = Message.obtain();
                msg.what = secondsRremaining--;
                timeHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(task, 1000, 1000);
    }
}
