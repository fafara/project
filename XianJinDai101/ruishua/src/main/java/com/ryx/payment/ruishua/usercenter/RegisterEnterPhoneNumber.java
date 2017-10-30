package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.BranchListAdapter;
import com.ryx.payment.ruishua.bean.AppListBean;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.AppIconUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.ryxkeylib.listener.EditPwdListener;
import com.ryx.ryxkeylib.service.CustomKeyBoardService;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


@EActivity(R.layout.activity_register_enter_phone_number)
public class RegisterEnterPhoneNumber extends BaseActivity {
    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.et_phonenumber)
    EditText mPhoneNumber;
    @ViewById(R.id.iv_show_pwd_status)
    ImageView mShowPwdView;
    @ViewById(R.id.et_password)
    EditText mPwdEditText;
    @ViewById(R.id.btn_register_next)
    Button mRegisterNext;
    @ViewById(R.id.tv_ruishua_agreement)
    TextView mRuiShuaAgreement;
    @ViewById(R.id.tv_ruishua_fenRunAgreement)
    TextView mRuiShuafenRunAgreement;
    @ViewById(R.id.layout_mobile)
    LinearLayout mMobileLine;
    @ViewById(R.id.layout_pwd)
    LinearLayout mPwdLine;
    @ViewById(R.id.rl_branch)
    RelativeLayout mBranchRl;
    @ViewById(R.id.tv_branch_name)
    TextView mBranchName;
    @ViewById(R.id.iv_branch_icon)
    ImageView mBranchIcon;

    private Param qtpayAppType;
    private Param qtpayOrderId;
    private ArrayList<AppListBean> app_list;
    @ViewById
    RelativeLayout rl_branch;
    @ViewById
    LinearLayout layout_branch;
    @ViewById(R.id.code_layout)
    LinearLayout code_layout;
    @ViewById(R.id.et_code)
    EditText et_code;
    @ViewById(R.id.et_code_line)
    LinearLayout et_code_line;
    @ViewById
    TextView and_tv;
    @AfterViews
    public void initViews() {
        RyxAppdata.getInstance(this).resetCurrentBranchConfig();
        //重置APP信息
        if(RyxAppdata.getInstance(this).getCurrentRegBranchType()==1){
            rl_branch.setVisibility(View.GONE);
            layout_branch.setVisibility(View.GONE);
        }else{
            rl_branch.setVisibility(View.VISIBLE);
            layout_branch.setVisibility(View.VISIBLE);
        }
        if(RyxAppdata.getInstance(this).getCurrentBranchRegIsNeedCode()){
            code_layout.setVisibility(View.VISIBLE);
            et_code_line.setVisibility(View.VISIBLE);
        }else{
            code_layout.setVisibility(View.GONE);
            et_code_line.setVisibility(View.GONE);
        }
        setTitleLayout("注册");
        mBackImg.setVisibility(View.VISIBLE);
        mMsgImg.setVisibility(View.GONE);
        initQtPatParams();
        iniRyxKeyWord();
        mBranchName.setText(RyxAppconfig.BRANCHID);
        mBranchIcon.setImageResource(AppIconUtil.selectIcoid(RyxAppconfig.APPUSER));
        mRuiShuaAgreement.setText("《"+RyxAppdata.getInstance(this).getCurrentBranchName()+"服务协议》");
        if (RyxAppconfig.BRANCH.equals("05")) {
         mRuiShuafenRunAgreement.setText("《"+RyxAppdata.getInstance(this).getCurrentBranchName()+"收益协议》");
            and_tv.setVisibility(View.VISIBLE);
            mRuiShuafenRunAgreement.setVisibility(View.VISIBLE);
        }else{
            and_tv.setVisibility(View.GONE);
            mRuiShuafenRunAgreement.setVisibility(View.GONE);
        }
    }

    @FocusChange({R.id.et_phonenumber, R.id.et_password})
    public void focusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.et_phonenumber:
                mMobileLine.setBackgroundResource(R.color.login_edt_getfocus);
                mPwdLine.setBackgroundResource(R.color.login_edt_lostfocus);
                break;
            case R.id.et_password:
                mMobileLine.setBackgroundResource(R.color.login_edt_lostfocus);
                mPwdLine.setBackgroundResource(R.color.login_edt_getfocus);
                break;
        }
    }

    @Click(R.id.rl_branch)
    public void showBranchList() {
        doGetBranchList();
    }

    /**
     * 获取branch列表
     */
    private void doGetBranchList() {
        qtpayApplication = new Param("application", "GetBranchList.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("getBranchList", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(payResult.getData());
                    app_list = new ArrayList<>();
                    JSONArray tps = jsonObj.getJSONArray("resultBean");
                    for (int i = 0; i < tps.length(); i++) {
                        AppListBean applist = new AppListBean();
                        applist.setAppName(tps.getJSONObject(i).getString(
                                "branchid"));
                        applist.setIconName(tps.getJSONObject(i).getString(
                                "appuser"));
                        applist.setKey(tps.getJSONObject(i).getString(
                                "key"));
                        app_list.add(applist);
                    }
                    createAppList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createAppList() {
        LayoutInflater inflater = LayoutInflater.from(this);
        AutoLinearLayout layout = (AutoLinearLayout) inflater.inflate(R.layout.bind_card_bank_list, null);
        TextView titlenametv = (TextView) layout.findViewById(R.id.titlename);
        titlenametv.setText("请选择设备类型");
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        Dialog.Builder builder = new Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        final Dialog dialog = builder.build(RegisterEnterPhoneNumber.this);
        ListView lv_bank = (ListView) layout.findViewById(R.id.lv_bank);
        ImageView imgview_close = (ImageView) layout.findViewById(R.id.imgview_close);
        imgview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        BranchListAdapter branchListAdapter = new BranchListAdapter(RegisterEnterPhoneNumber.this, app_list);
        lv_bank.setAdapter(branchListAdapter);
        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RyxAppconfig.APPUSER = app_list.get(position).getIconName();
                RyxAppconfig.API_SIGN_KEY = app_list.get(position).getKey();
                mBranchName.setText(app_list.get(position).getAppName());
                mBranchIcon.setImageResource(AppIconUtil.selectIcoid(app_list.get(position)
                        .getIconName()));
                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        dialog.show();
    }

    private void iniRyxKeyWord() {
        CustomKeyBoardService customKeyBoardService = CustomKeyBoardService.registerKeyBoardForEdit(RegisterEnterPhoneNumber.this, true, mPwdEditText, new EditPwdListener() {
            @Override
            public void getPwdVal(String realVal, String disVal) {
//            textView1.setText("密码:"+realVal);
                mPwdEditText.setText(realVal);
            }

            @Override
            public void getPwdDisVal(String disVal, int count) {
                mPwdEditText.setText(disVal);
            }

            @Override
            public void pwdViewOkbtnLisener() {
//                onNextClick();
            }

        });
        customKeyBoardService.setEditMaxLenth(20);
    }

    @Click(R.id.tv_ruishua_agreement)
    public void showAgreement() {
        Intent intent = new Intent(this, RegisterAgreement_.class);
        startActivity(intent);
    }
    @Click(R.id.tv_ruishua_fenRunAgreement)
    public void showfenRunAgreement() {
        Intent intent = new Intent(RegisterEnterPhoneNumber.this, HtmlMessageActivity_.class);
        intent.putExtra("title", "收益协议");
        intent.putExtra("urlkey", RyxAppconfig.Notes_FenRunAgreement);
        startActivity(intent);
    }

    @Click(R.id.iv_show_pwd_status)
    public void setPwdStatus() {
        if (mPwdEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            mPwdEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mShowPwdView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_user_eye_close));
        } else {
            mPwdEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mShowPwdView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_user_eye_open));
        }
        mPwdEditText.setSelection(mPwdEditText.getText().length());
    }

    @Click(R.id.tileleftImg)
    public void setBackClick() {
        finish();
    }

    /**
     * 采用正则表达式检查密码
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

    @Click(R.id.btn_register_next)
    public void onNextClick() {
        String numberString = mPhoneNumber.getText().toString().trim();
        String pwdString = mPwdEditText.getText().toString().trim();
        String et_codeStr = "";
        if(RyxAppdata.getInstance(this).getCurrentBranchRegIsNeedCode()){
            et_codeStr = et_code.getText().toString().trim();
            if (TextUtils.isEmpty(et_codeStr)) {
                LogUtil.showToast(this, "请输入邀请码");
                return;
            }
        }
        if (TextUtils.isEmpty(numberString)) {
            LogUtil.showToast(this, "请输入手机号码");
            return;
        }
        if (numberString.length() != 11) {
            LogUtil.showToast(this, "手机号码为11位");
            return;
        }
        if (TextUtils.isEmpty(pwdString)) {
            LogUtil.showToast(this, "请输入密码");
            return;
        }
        if (pwdString.length() < 6) {
            LogUtil.showToast(this, "密码至少6位");
            return;
        }
        String flag = checkPassword(pwdString);
        if (flag.equals("0") || flag.equals("1")) {
            LogUtil.showToast(this, "您的密码太过于简单,请使用复杂密码");
            return;
        }
        //弹出对话框确认发送验证码
        showAuthDialog(numberString, pwdString,et_codeStr);
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayAppType = new Param("appType", "UserRegister");
        qtpayOrderId = new Param("orderId", "");
    }

    private void showAuthDialog(final String number, final String password, final String et_codeStr) {

        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                QtpayAppData.getInstance(RegisterEnterPhoneNumber.this).setPhone(
                        number);
                QtpayAppData.getInstance(RegisterEnterPhoneNumber.this).setMobileNo(
                        number);
                getMobileMac(number, password,et_codeStr);
                ryxSimpleConfirmDialog.dismiss();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("我们将发送短信到此手机号码\n" + number);
    }
    /**
     * 发送网络请求
     */
    private void getMobileMac(final String number, final String password, final String et_codeStr) {
        qtpayApplication.setValue("GetMobileMac.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayAppType);
        qtpayParameterList.add(qtpayOrderId);
        httpsPost("getMobileMac", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                if (qtpayApplication.getValue().equals("GetMobileMac.Req")) {
                    LogUtil.showToast(RegisterEnterPhoneNumber.this,
                            getResources().getString(R.string.sms_has_been_issued_please_note_that_check));
                    Intent intent = new Intent(RegisterEnterPhoneNumber.this, RegisterEnterMobileMac_.class);
                    intent.putExtra("phonenumber", number);
                    intent.putExtra("password", password);
                    intent.putExtra("code", et_codeStr);
                    startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
                }
            }
        });
    }
}
