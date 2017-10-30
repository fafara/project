package com.ryx.payment.ruishua.authenticate;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.utils.IDCardUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * xiepingping
 */
@EActivity(R.layout.activity_user_info_add)
public class UserInfoAddActivity extends BaseActivity {

    @ViewById(R.id.edt_userName)
    EditText edt_userName;
    @ViewById(R.id.edt_userCardNo)
    EditText edt_userCardNo;
    @ViewById(R.id.cb_agree)
    CheckBox check_box;
    @ViewById(R.id.tv_authcontract)
    TextView tv_authcontract;
    @ViewById(R.id.btn_next)
    Button next;

    private boolean isAgredded;//判断有没有同意协议
    private String userRealName, userIdNo;

    @AfterViews
    public void iniViews() {
        check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgredded = isChecked;
            }
        });
        edt_userCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 18) {
                    if (!IDCardUtil.isIDCard(s.toString())) {
                        LogUtil.showToast(UserInfoAddActivity.this, getResources().getString(R.string.please_enter_the_correct_id_number));
                    }
                }
            }
        });
    }

    @Click(R.id.btn_next)
    public void goNextStep() {
        if (checkInput()) {
            Intent intent = new Intent(UserInfoAddActivity.this, UserAuthenticateSignatureActivity_.class);
            PreferenceUtil.getInstance(getApplicationContext()).saveString("realname", userRealName);
            PreferenceUtil.getInstance(getApplicationContext()).saveString("idcardnum", userIdNo.toLowerCase());
            startActivity(intent);
            finish();
        }
    }

    @Click(R.id.tv_authcontract)
    public void goAuthContract() {
        Intent intent = new Intent(UserInfoAddActivity.this,HtmlMessageActivity_.class);
        intent.putExtra("title","实名认证协议");
        intent.putExtra("urlkey", RyxAppconfig.Notes_SignPerson);
        startActivity(intent);
    }
    @Click(R.id.tv_privacy)
    public void goPrivacyContent() {
        Intent intent = new Intent(UserInfoAddActivity.this,HtmlMessageActivity_.class);
        intent.putExtra("title","隐私权保护政策");
        intent.putExtra("urlkey", RyxAppconfig.Notes_privacy);
        startActivity(intent);
    }

    private boolean checkInput() {
        userRealName = edt_userName.getText().toString() + "";  // 登录的手机号
        userIdNo = edt_userCardNo.getText().toString() + "";    // 登录密码；
        //判断用户是否填写了姓名
        if (TextUtils.isEmpty(userRealName)) {
            LogUtil.showToast(UserInfoAddActivity.this, getResources().getString(R.string.please_enter_correct_name));
            return false;
        }
        //判断用户是否填写了身份证号码
        if (!IDCardUtil.isIDCard(userIdNo)) {
            LogUtil.showToast(UserInfoAddActivity.this, getResources().getString(R.string.please_enter_the_correct_id_number));
            return false;
        }
        if (!isAgredded) {
            LogUtil.showToast(UserInfoAddActivity.this, getResources().getString(R.string.please_checkd_the_agree));
            return false;
        }
        return true;
    }

    @Click(R.id.tileleftImg)
    public void closeWindow() {
        finish();
    }
}
