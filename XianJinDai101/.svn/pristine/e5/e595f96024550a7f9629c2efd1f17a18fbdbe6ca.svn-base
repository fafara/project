package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.BranchListAdapter;
import com.ryx.payment.ruishua.bean.AppListBean;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.AppIconUtil;
import com.ryx.payment.ruishua.utils.IDCardUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_reset_password)
public class ResetPassword extends BaseActivity {

    @ViewById(R.id.devtypearl)
    AutoRelativeLayout devtypearl;
    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.et_mac)
    EditText mMacEditText;
    @ViewById(R.id.tv_again_mac)
    TextView mAgainMacTextView;
    @ViewById(R.id.et_phonenumber)
    EditText mPhoneNumberEditText;
    @ViewById(R.id.btn_reset_next)
    Button mResetNextBtn;
    @ViewById(R.id.et_username)
    EditText mUserNameEdit;
    @ViewById(R.id.et_identification_card)
    EditText mIdentificationCardEdit;

    @ViewById(R.id.ll_phonenumber)
    LinearLayout mPhoneNumberLL;
    @ViewById(R.id.ll_username)
    LinearLayout mUserNameLL;
    @ViewById(R.id.ll_identification_card)
    LinearLayout mIdentificationCardLL;
    @ViewById(R.id.ll_mac)
    LinearLayout mMacLL;

    @ViewById(R.id.devtypeImg)
    ImageView devtypeImg;
    @ViewById(R.id.mBranchNametv)
    TextView mBranchNametv;
    private Param qtpayAppType;
    private Param qtpayOrderId;
    private Timer mTimer = new Timer();
    private String numberString;
    private String mUserName="";
    private String mIdCard="";
    private String macString;
    private ArrayList<AppListBean> app_list;
    @ViewById
    AutoLinearLayout devtypeallyout;
    @ViewById
    LinearLayout linelineyout;
    @AfterViews
    public void initViews() {
        RyxAppdata.getInstance(this).resetCurrentBranchConfig();
        setTitleLayout("找回密码");
        mBackImg.setVisibility(View.VISIBLE);
        mMsgImg.setVisibility(View.GONE);
        initQtPatParams();
        if(RyxAppdata.getInstance(this).getCurrentResetBranchType()==1){
            devtypeallyout.setVisibility(View.GONE);
            linelineyout.setVisibility(View.GONE);
        }else{
            devtypeallyout.setVisibility(View.VISIBLE);
            linelineyout.setVisibility(View.VISIBLE);
        }
        mBranchNametv.setText(RyxAppconfig.BRANCHID);
        devtypeImg.setImageResource(AppIconUtil.selectIcoid(RyxAppconfig.APPUSER));


//        mUserNameEdit.addTextChangedListener(new TextWatcher() {
//            String before = "";
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                before = s.toString();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String str = s.toString();
//                if (!TextUtils.isEmpty(str) && !str.matches(RegUtils.REGEX_CHINESE)) {
//                    mUserNameEdit.setText(before);
//                    mUserNameEdit.setSelection(mUserNameEdit.getText().toString().length());
//                }
//            }
//        });
    }

    @Click(R.id.devtypearl)
    public void selectdevtypelist(){
        //获取分支暂时屏蔽
        doGetBranchList();
    }

    /**
     * 获取贴牌列表数据
     */
    private void doGetBranchList() {
        qtpayApplication = new Param("application", "GetBranchList.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("doGetBranchListTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    JSONObject jsonObj = new JSONObject(payResult.getData());
                    app_list = new ArrayList<AppListBean>();
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
                    createApplist();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
    private void createApplist() {
        LayoutInflater inflater = LayoutInflater.from(this);
        AutoLinearLayout layout = (AutoLinearLayout) inflater.inflate(R.layout.bind_card_bank_list, null);
        TextView titlenametv=  (TextView)layout.findViewById(R.id.titlename);
        titlenametv.setText("请选择设备类型");
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        com.rey.material.app.Dialog.Builder builder =
                new com.rey.material.app.Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        final com.rey.material.app.Dialog dialog = builder.build(ResetPassword.this);
        ListView lv_bank = (ListView) layout.findViewById(R.id.lv_bank);
        ImageView imgview_close = (ImageView) layout.findViewById(R.id.imgview_close);
        imgview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        BranchListAdapter branchListAdapter = new BranchListAdapter(ResetPassword.this, app_list);
        lv_bank.setAdapter(branchListAdapter);
        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RyxAppconfig.APPUSER = app_list.get(position).getIconName();
                RyxAppconfig.API_SIGN_KEY = app_list.get(position).getKey();
                mBranchNametv.setText(app_list.get(position).getAppName());
                devtypeImg.setImageResource(AppIconUtil.selectIcoid(app_list.get(position)
                        .getIconName()));
                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        dialog.show();
    }

    public void allLineLostFocus() {
        mPhoneNumberLL.setBackgroundResource(R.color.login_edt_lostfocus);
        mUserNameLL.setBackgroundResource(R.color.login_edt_lostfocus);
        mIdentificationCardLL.setBackgroundResource(R.color.login_edt_lostfocus);
        mMacLL.setBackgroundResource(R.color.login_edt_lostfocus);
    }

    @FocusChange({R.id.et_phonenumber, R.id.et_username, R.id.et_identification_card, R.id.et_mac})
    public void focusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.et_phonenumber:
                allLineLostFocus();
                mPhoneNumberLL.setBackgroundResource(R.color.login_edt_getfocus);
                break;
            case R.id.et_username:
                allLineLostFocus();
                mUserNameLL.setBackgroundResource(R.color.login_edt_getfocus);
                break;
            case R.id.et_identification_card:
                allLineLostFocus();
                mIdentificationCardLL.setBackgroundResource(R.color.login_edt_getfocus);
                break;
            case R.id.et_mac:
                allLineLostFocus();
                mMacLL.setBackgroundResource(R.color.login_edt_getfocus);
                break;
        }
    }

    @Click(R.id.tileleftImg)
    public void setBackClick() {
        finish();
    }

    @Click(R.id.tv_again_mac)
    public void sendMac() {
        if (checkInput()) {
            mTimer = null;
            mTimer = new Timer();
            String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
            QtpayAppData.getInstance(ResetPassword.this).setPhone(phoneNumber);
            QtpayAppData.getInstance(ResetPassword.this).setMobileNo(phoneNumber);
            getMobileMac();
        }
    }

    private void getMobileMac() {
        qtpayApplication = new Param("application", "GetMobileMac.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayAppType);
        qtpayParameterList.add(qtpayOrderId);
        httpsPost("getMobileMac", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(ResetPassword.this,
                        getResources().getString(R.string.sms_has_been_issued_please_note_that_check));
                startCountdown();
            }
        });
    }

    @Click(R.id.btn_reset_next)
    public void resetNextClick() {
        if (checkInput()) {
            if (checkMobileMac()) {
                Intent intent = new Intent(ResetPassword.this, SetPassword_.class);
                intent.putExtra("phone", numberString);
                intent.putExtra("realName", mUserName);
                intent.putExtra("certType", "01");
                intent.putExtra("certPid", mIdCard.toLowerCase());
                intent.putExtra("mobileMac", macString);
                startActivity(intent);
                finish();
            }
        }
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

    private boolean checkInput() {
        numberString = mPhoneNumberEditText.getText().toString().trim();
        mUserName = mUserNameEdit.getText().toString().trim();
        mIdCard = mIdentificationCardEdit.getText().toString().trim();
        if (TextUtils.isEmpty(numberString)) {
            LogUtil.showToast(this,"请输入手机号码");
            return false;
        }
        if (numberString.length() != 11) {
            LogUtil.showToast(this,"手机号码为11位");
            return false;
        }
        if(!TextUtils.isEmpty(mIdCard)){
            if (mIdCard.length() != 18 || !IDCardUtil.isIDCard(mIdCard)) {
                LogUtil.showToast(this,"请输入正确的身份证号码");
                return false;
            }
        }

        return true;
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayAppType = new Param("appType", "RetrievePassword");
        qtpayOrderId = new Param("orderId", "");
    }

    Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 0) {
                mAgainMacTextView.setTextColor(ContextCompat.getColor(ResetPassword.this, R.color.text_a));
                mAgainMacTextView.setText(getResources().getString(R.string.resend) + "(" + msg.what + ")");
                mAgainMacTextView.setClickable(false);
            } else {
                mTimer.cancel();
                mAgainMacTextView.setText(getResources().getString(R.string.resend_verification_code));
                mAgainMacTextView.setClickable(true);
                mAgainMacTextView.setTextColor(ContextCompat.getColor(ResetPassword.this, R.color.colorPrimary));
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
}
