package com.ryx.payment.ruishua.setting;

import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_change_pass_word)
public class ChangePassWord extends BaseActivity {

    @ViewById(R.id.et_old_password)
    EditText mOldPwd;
    @ViewById(R.id.et_new_password)
    EditText mNewPwd;
    @ViewById(R.id.iv_show_old_pwd_status)
    ImageView mShowOldPwdStatus;
    @ViewById(R.id.iv_show_new_pwd_status)
    ImageView mShowNewPwdStatus;
    @ViewById(R.id.et_mac)
    EditText mMacEditText;
    @ViewById(R.id.tv_again_mac)
    TextView mMacAgainText;
    @ViewById(R.id.btn_save_pwd)
    Button mSavePwdBtn;
    @ViewById(R.id.ll_old_pwd_line)
    LinearLayout mOldPwdLL;
    @ViewById(R.id.ll_new_pwd_line)
    LinearLayout mNewPwdLL;
    @ViewById(R.id.ll_mac_line)
    LinearLayout mMacLineLL;


    private String macString;
    private Timer mTimer = new Timer();
    private Param qtpayAppType;
    private Param qtpayOrderId;
    private Param qtpayPassword;
    private String oldPwd;
    private String newPwd;
    private Param qtpayNewPassword;
    private Param qtpayCertPid;
    private Param qtpayMobileMac;

    @AfterViews
    public void initViews() {
        setTitleLayout("修改密码",true,false);
        initQtPatParams();
        iniRyxOldPwdWord();
        iniRyxNewPwdWord();
    }

    public void allLineLostFocus() {
        mOldPwdLL.setBackgroundResource(R.color.login_edt_lostfocus);
        mNewPwdLL.setBackgroundResource(R.color.login_edt_lostfocus);
        mMacLineLL.setBackgroundResource(R.color.login_edt_lostfocus);
    }

    @FocusChange({R.id.et_old_password, R.id.et_new_password, R.id.et_mac})
    public void focusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.et_old_password:
                allLineLostFocus();
                mOldPwdLL.setBackgroundResource(R.color.login_edt_getfocus);
                break;
            case R.id.et_new_password:
                allLineLostFocus();
                mNewPwdLL.setBackgroundResource(R.color.login_edt_getfocus);
                break;
            case R.id.et_mac:
                allLineLostFocus();
                mMacLineLL.setBackgroundResource(R.color.login_edt_getfocus);
                break;
        }
    }
    private void iniRyxOldPwdWord() {
        CustomKeyBoardService customKeyBoardService = CustomKeyBoardService.registerKeyBoardForEdit(ChangePassWord.this, true, mOldPwd, new EditPwdListener() {
            @Override
            public void getPwdVal(String realVal, String disVal) {
//            textView1.setText("密码:"+realVal);
                mOldPwd.setText(realVal);
            }

            @Override
            public void getPwdDisVal(String disVal, int count) {
                mOldPwd.setText(disVal);
            }

            @Override
            public void pwdViewOkbtnLisener() {
            }

        });
        customKeyBoardService.setEditMaxLenth(20);
    }

    private void iniRyxNewPwdWord() {
        CustomKeyBoardService customKeyBoardService = CustomKeyBoardService.registerKeyBoardForEdit(ChangePassWord.this, true, mNewPwd, new EditPwdListener() {
            @Override
            public void getPwdVal(String realVal, String disVal) {
//            textView1.setText("密码:"+realVal);
                mNewPwd.setText(realVal);
            }

            @Override
            public void getPwdDisVal(String disVal, int count) {
                mNewPwd.setText(disVal);
            }

            @Override
            public void pwdViewOkbtnLisener() {
            }

        });
        customKeyBoardService.setEditMaxLenth(20);
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

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayAppType = new Param("appType", "UserUpdatePwd");
        qtpayOrderId = new Param("orderId", "");
    }

    @Click(R.id.iv_show_old_pwd_status)
    public void showOldPwd() {
        if (mOldPwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            //当前是密文切换成明文
            mOldPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mShowOldPwdStatus.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_user_eye_close));
        } else {
            mOldPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mShowOldPwdStatus.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_user_eye_open));
        }
        mOldPwd.setSelection(mOldPwd.getText().length());
    }

    @Click(R.id.iv_show_new_pwd_status)
    public void showNewPwd() {
        if (mNewPwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            //当前是密文切换成明文
            mNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mShowNewPwdStatus.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_user_eye_close));
        } else {
            mNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mShowNewPwdStatus.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_user_eye_open));
        }
        mNewPwd.setSelection(mNewPwd.getText().length());
    }

    @Click(R.id.btn_save_pwd)
    public void savePwdBtn() {
        if (checkInput()) {
            if (checkMobileMac()) {
                doSavePwd();
            }
        }
    }

    /**
     * 保存密码
     */
    private void doSavePwd() {
        qtpayApplication = new Param("application", "UserUpdatePwd.Req");
        // 原密码
        qtpayPassword = new Param("password", QtPayEncode.encryptUserPwd(oldPwd, QtpayAppData
                .getInstance(ChangePassWord.this).getMobileNo(), RyxAppconfig.DEBUG));
        // 新密码
        qtpayNewPassword = new Param("newPassword", QtPayEncode.encryptUserPwd(newPwd, QtpayAppData
                .getInstance(ChangePassWord.this).getMobileNo(), RyxAppconfig.DEBUG));
        // 加密后的密码，使用前置平台公钥(UKEY1)加密(HEX)
        // 证件号码
        qtpayCertPid = new Param("certPid", QtpayAppData.getInstance(ChangePassWord.this)
                .getCertPid());
        // 短信验证码
        qtpayMobileMac = new Param("mobileMac", macString);
        qtpayAttributeList.clear();
        qtpayParameterList.clear();
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayPassword);
        qtpayParameterList.add(qtpayNewPassword);
        qtpayParameterList.add(qtpayCertPid);
        qtpayParameterList.add(qtpayMobileMac);
        httpsPost("doChangePwd", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(ChangePassWord.this,
                        getResources().getString(R.string.password_has_been_changed_successfully));

                QtpayAppData.getInstance(ChangePassWord.this).setLogin(false);
                QtpayAppData.getInstance(ChangePassWord.this).setRealName("");
                QtpayAppData.getInstance(ChangePassWord.this).setMobileNo("");
                QtpayAppData.getInstance(ChangePassWord.this).setPhone("");
                QtpayAppData.getInstance(ChangePassWord.this).setCustomerId("");
                QtpayAppData.getInstance(ChangePassWord.this).setAuthenFlag(0);
                QtpayAppData.getInstance(ChangePassWord.this).setCustomerName("");
                QtpayAppData.getInstance(ChangePassWord.this).setToken("");
                toAgainLogin(ChangePassWord.this,RyxAppconfig.TOLOGINACT);
//                startActivityForResult(new Intent(ChangePassWord.this, LoginActivity_.class),RyxAppconfig.TOLOGINACT);
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

    @Click(R.id.tv_again_mac)
    public void sendMacText() {
        if (checkInput()) {
            mTimer = null;
            mTimer = new Timer();
            getMobileMac();
        }
    }

    /**
     * 获得验证码
     */
    private void getMobileMac() {
        qtpayApplication = new Param("application", "GetMobileMac.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayAppType);
        qtpayParameterList.add(qtpayOrderId);
        httpsPost("getMobileMac", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(ChangePassWord.this,
                        getResources().getString(R.string.sms_has_been_issued_please_note_that_check));
                startCountdown();
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

    public boolean checkInput() {
        oldPwd = mOldPwd.getText().toString().trim();
        newPwd = mNewPwd.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)) {
            LogUtil.showToast(this,"请输入原密码");
            return false;
        }
        if (TextUtils.isEmpty(newPwd)) {
            LogUtil.showToast(this,"请输入新密码");
            return false;
        }
        if (oldPwd.length() < 6) {
            LogUtil.showToast(this,"密码至少6位");
            return false;
        }
        if (newPwd.length() < 6) {
            LogUtil.showToast(this,"密码至少6位");
            return false;
        }
        String flag = checkPassword(newPwd);
        if (flag.equals("0") || flag.equals("1")) {
            LogUtil.showToast(this,"您的密码太过于简单,请使用复杂密码");
            return false;
        }
        return true;
    }

    private boolean checkMobileMac() {
        macString = mMacEditText.getText().toString().trim();
        if (TextUtils.isEmpty(macString)) {
            LogUtil.showToast(this,"请输入验证码");
            return false;
        }
        if (macString.length() != 4) {
            LogUtil.showToast(this,"验证码为4位数字");
            return false;
        }
        return true;
    }

    Handler timeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what > 0) {
                mMacAgainText.setTextColor(ContextCompat.getColor(ChangePassWord.this, R.color.text_a));
                mMacAgainText.setText(getResources().getString(R.string.resend) + "(" + msg.what + ")");
                mMacAgainText.setClickable(false);
            } else {
                mTimer.cancel();
                mMacAgainText.setText(getResources().getString(R.string.resend_verification_code));
                mMacAgainText.setClickable(true);
                mMacAgainText.setTextColor(ContextCompat.getColor(ChangePassWord.this, R.color.colorPrimary));
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
                Message msg = new Message();
                msg.what = secondsRremaining--;
                timeHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(task, 1000, 1000);
    }

    @Click(R.id.tileleftImg)
    public void backBtn(){
        finish();
    }
}
