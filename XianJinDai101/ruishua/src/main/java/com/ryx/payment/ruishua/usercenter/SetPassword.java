package com.ryx.payment.ruishua.usercenter;

import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.qtpay.qtjni.QtPayEncode;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.ryxkeylib.listener.EditPwdListener;
import com.ryx.ryxkeylib.service.CustomKeyBoardService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_set_password)
public class SetPassword extends BaseActivity {

    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.et_password_again)
    EditText mSetAgainPwd;
    @ViewById(R.id.btn_setpwd_done)
    Button mBtnSetDone;
    @ViewById(R.id.iv_show_pwd_status)
    ImageView mShowPwdStatusImgView;
    private String mPhoneNumber;
    private String mUserName;
    private String mCertType;
    private String mCertPid;
    private String mMobileMac;
    private Param qtpayOrderId;
    private Param qtpayRealName;
    private Param qtpayNewPassword;
    private Param qtpayCertType;
    private Param qtpayCertPid;
    private Param qtpayMobileMac;

    @AfterViews
    public void initViews() {
        setTitleLayout("设置密码");
        mBackImg.setVisibility(View.VISIBLE);
        mMsgImg.setVisibility(View.GONE);
        mPhoneNumber = getIntent().getStringExtra("phone");
        mUserName = getIntent().getStringExtra("realName");
        mCertType = getIntent().getStringExtra("certType");
        mCertPid = getIntent().getStringExtra("certPid");
        mMobileMac = getIntent().getStringExtra("mobileMac");
        initQtPatParams();
        iniRyxKeyWord();
    }

    /**
     * 采用正则表达式检查密码强度
     */
    public String checkPassword(String passwordStr) {
        // 输入单数字或英文或符号 密码强度显示 弱
        // 输入数字、英文、符号中的任意2种 密码强度显示 中
        // 输入数字、英文、符号 三种 密码强度显示 强
        final String str1 = "[0-9]{1,20}$"; // 不超过20位的数字组合
        final String str2 = "^[a-zA-Z]{1,20}$"; // 由字母不超过20位
        final String str3 = "^[0-9|a-z|A-Z]{1,20}$"; // 由字母、数字组成，不超过20位
        final String str4 = "^[0-9|a-z|A-Z|[^0-9|^a-z|^A-Z]]{1,20}$"; // 由字母、数字、符号
        // 三种组成，不超过20位

        if (passwordStr == null || passwordStr.length() == 0) {
            return "0";
        }
        if (passwordStr.matches(str1) || passwordStr.matches(str2)) {
            return "1";
        }
        if (passwordStr.matches(str3)) {
            return "2";
        }
        if (passwordStr.matches(str4)) {
            return "3";
        }
        return "3";
    }

    @Click(R.id.iv_show_pwd_status)
    public void setPwdStatus() {
        if (mSetAgainPwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            //当前是密文切换成明文
            mSetAgainPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mShowPwdStatusImgView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_user_eye_close));
        } else {
            mSetAgainPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mShowPwdStatusImgView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_user_eye_open));
        }
        mSetAgainPwd.setSelection(mSetAgainPwd.getText().length());
    }
    private void iniRyxKeyWord() {
        CustomKeyBoardService customKeyBoardService = CustomKeyBoardService.registerKeyBoardForEdit(SetPassword.this, true, mSetAgainPwd, new EditPwdListener() {
            @Override
            public void getPwdVal(String realVal, String disVal) {
//            textView1.setText("密码:"+realVal);
                mSetAgainPwd.setText(realVal);
            }

            @Override
            public void getPwdDisVal(String disVal, int count) {
                mSetAgainPwd.setText(disVal);
            }

            @Override
            public void pwdViewOkbtnLisener() {
                setDoneClick();
            }

        });
        customKeyBoardService.setEditMaxLenth(20);
    }
    @Click(R.id.btn_setpwd_done)
    public void setDoneClick() {
        String pwdString = mSetAgainPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwdString)) {
            LogUtil.showToast(this,"请输入密码");
            return;
        }
        if (pwdString.length() < 6) {
            LogUtil.showToast(this,"密码至少6位");
            return;
        }
        String flag = checkPassword(pwdString);
        if (flag.equals("0") || flag.equals("1")) {
            LogUtil.showToast(this,"您的密码太过于简单,请使用复杂密码");
            return;
        }
        doResetPassWord(pwdString);
    }

    private void doResetPassWord(String password) {
        qtpayApplication.setValue("RetrievePassword.Req");
        qtpayAttributeList.add(qtpayApplication);
        QtpayAppData.getInstance(SetPassword.this).setPhone(mPhoneNumber);
        QtpayAppData.getInstance(SetPassword.this).setMobileNo(mPhoneNumber);
        qtpayRealName.setValue(mUserName);
        qtpayNewPassword.setValue(QtPayEncode.encryptUserPwd(password, mPhoneNumber, RyxAppconfig.DEBUG));// 新密码
        // 加密后的密码，使用前置平台公钥(UKEY1)加密(HEX)
        qtpayCertType.setValue(mCertType);// 证件类型
        qtpayCertPid.setValue(mCertPid);// 证件号码
        qtpayMobileMac.setValue(mMobileMac);// 短信验证码
        qtpayParameterList.add(qtpayRealName);
        qtpayParameterList.add(qtpayNewPassword);
        qtpayParameterList.add(qtpayCertType);
        qtpayParameterList.add(qtpayCertPid);
        qtpayParameterList.add(qtpayMobileMac);

        httpsPost("doResetPwd", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                if (qtpayApplication.getValue().equals("RetrievePassword.Req")) {
                    LogUtil.showToast(SetPassword.this,
                            getResources().getString(R.string.password_has_been_set_successfully));
                    finish();
                }
            }
        });
    }

    @Click(R.id.tileleftImg)
    public void setBackClick() {
        finish();
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayOrderId = new Param("orderId", "");
        qtpayRealName = new Param("realName");
        qtpayNewPassword = new Param("newPassword");
        // 证件类型
        qtpayCertType = new Param("certType");
        // 证件号码
        qtpayCertPid = new Param("certPid");
        // 短信验证码
        qtpayMobileMac = new Param("mobileMac");
    }
}
