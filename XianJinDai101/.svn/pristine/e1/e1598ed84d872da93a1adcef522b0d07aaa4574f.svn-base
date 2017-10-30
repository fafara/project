package com.ryx.payment.ruishua.bindcard;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.ExpiryFilter;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.ryx.payment.ruishua.R.id.edt_mac;

@EActivity(R.layout.activity_my_credit_card)
public class MyCreditCardActivity extends BaseActivity {

    @ViewById
    EditText edt_userName;
    @ViewById
    EditText edt_cardType;
    @ViewById
    EditText edt_date;
    @ViewById
    EditText edt_securityCode;
    @ViewById
    EditText edt_phoneNum;
    @ViewById
    CheckBox cb_agree;
    @ViewById
    TextView tv_payProtocols;
    @ViewById
    Button btn_next;
    @ViewById(edt_mac)
    EditText mMacEt;//验证码
    @ViewById(R.id.tv_again_mac)
    TextView mSendMacTv;//发送验证码

    @ViewById(R.id.lay_contract)
    AutoRelativeLayout lay_contract;
    @ViewById(R.id.edt_date_linerlayout)
    AutoLinearLayout edt_date_linerlayout;
    @ViewById(R.id.securityCode_linerlayout)
    AutoLinearLayout securityCode_linerlayout;


    private BankCardInfo bankCardInfo;
    private String usertype;
    private String cardType;
    private String branchid2, bankName,securCode="",hissuers,kjzf_status,needvaliddate,needcvn;
    private Timer myTimer;
    private Param qtpayAppType;
    private Param qtpayOrderId;
    private int month;
    private String phoneStr="";
    private int year;
    private String date = "";
    private boolean fullLength = false;
    private InputFilter[] filters = {new ExpiryFilter()};
    private boolean isAgredded = true;//判断有没有同意协议
    Param qtpayflag;
    Param qtpayAccountNo;
    private Param qtpayBankId;
    private Timer mTimer = new Timer();
    @AfterViews
    public void initViews(){
        setTitleLayout("填写银行卡信息",true,false);
        bankCardInfo = (BankCardInfo) getIntent().getExtras().get("bankCardInfo");
        usertype = getIntent().getExtras().getString("usertype");
        bankName = getIntent().getExtras().getString("bankname");
        cardType = getIntent().getExtras().getString("cardtype");
        branchid2 = getIntent().getExtras().getString("branchid2");
        hissuers = getIntent().getExtras().getString("hissuers");

        kjzf_status = getIntent().getExtras().getString("kjzf_status");
        needvaliddate = getIntent().getExtras().getString("needvaliddate");
        needcvn = getIntent().getExtras().getString("needcvn");
        initInfo();
        initQtPatParams();
    }

    private void initInfo(){
        if("1".equals(kjzf_status)){
            lay_contract.setVisibility(View.VISIBLE);

            cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isAgredded = isChecked;
                }
            });
        }else{
            lay_contract.setVisibility(View.GONE);
        }

        if("1".equals(needvaliddate)){
            edt_date_linerlayout.setVisibility(View.VISIBLE);

            edt_date.setText(bankCardInfo.getCreditCardTime());
            edt_date.setInputType(InputType.TYPE_CLASS_PHONE);
            edt_date.setFilters(filters);
            edt_date.addTextChangedListener(new TextWatcher() {
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


        }else{
            edt_date_linerlayout.setVisibility(View.GONE);
        }

        if("1".equals(needcvn)){
            securityCode_linerlayout.setVisibility(View.VISIBLE);
        }else{
            securityCode_linerlayout.setVisibility(View.GONE);
        }


        edt_userName.setText(bankCardInfo.getName());
        edt_cardType.setText(bankName + cardType);



        btn_next.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                uploadInfo();
            }
        });
        mSendMacTv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                setmSendMacTv();
            }
        });
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

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayflag = new Param("flag", "01");
        qtpayAccountNo = new Param("accountNo");
    }

    private void uploadInfo(){
        phoneStr = edt_phoneNum.getText().toString();

        String edt_macVal=mMacEt.getText().toString();

        if("1".equals(needvaliddate)){
            date = edt_date.getText().toString();
            if (TextUtils.isEmpty(date)) {
                LogUtil.showToast(MyCreditCardActivity.this, "请输入卡片有截止效期！");
                return;
            }
        }
        if("1".equals(needcvn)){
            securCode =edt_securityCode.getText().toString();
            if (TextUtils.isEmpty(securCode)) {
                LogUtil.showToast(MyCreditCardActivity.this, "请输入正确的安全码！");
                return;
            }
        }
        if (TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11) {
            LogUtil.showToast(MyCreditCardActivity.this, "请输入正确的银行预留电话！");
            return;
        }

        if (TextUtils.isEmpty(edt_macVal)) {
            LogUtil.showToast(this, "请输入验证码");
            return ;
        }
        if (edt_macVal.length() != 4) {
            LogUtil.showToast(this, "验证码为4位数字");
            return ;
        }
        if ("1".equals(kjzf_status)&&!isAgredded) {
            LogUtil.showToast(this, "请同意快捷支付协议");
            return ;
        }
        final String mac = mMacEt.getText().toString().trim();
        String mobileNo = QtpayAppData.getInstance(this).getMobileNo();
        String phoneNo = edt_phoneNum.getText().toString().trim();
        if (mobileNo.equals(phoneNo)) {
            RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(MyCreditCardActivity.this, new ConFirmDialogListener() {

                @Override
                public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                    ryxSimpleConfirmDialog.dismiss();
                    checkSmsMac(mac);//校验验证码
                }

                @Override
                public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                    ryxSimpleConfirmDialog.dismiss();
                }
            });
            ryxSimpleConfirmDialog.show();
            ryxSimpleConfirmDialog.setContent("手机号为银行卡预留手机号，是否确认？");
        } else {
            //校验验证码
            checkSmsMac(mac);
        }

    }

    private void bindCreditCard(){
        super.initQtPatParams();
        qtpayApplication.setValue("BankCardBind.Req");
        Param qtpayBindType = new Param("bindType", "01");
        Param qtpayAccountNo = new Param("accountNo");
        Param qtpayUsertype = new Param("userType", usertype);
        Param phoneNumParam = new Param("phoneNum", phoneStr);
        qtpayAccountNo.setValue(bankCardInfo.getAccountNo().trim().replace(" ", ""));
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBindType);
        qtpayParameterList.add(phoneNumParam);
        qtpayParameterList.add(qtpayAccountNo);
        qtpayParameterList.add(qtpayUsertype);
        qtpayParameterList.add(new Param("bankId", branchid2));
        qtpayParameterList.add(new Param("hissuers", hissuers));
        if("1".equals(needvaliddate)){
            qtpayParameterList.add(new Param("validDate", date));
        }
        if("1".equals(needcvn)){
            qtpayParameterList.add(new Param("cardCvn", securCode));
        }

        qtpayParameterList.add(new Param("cardType", "03"));
        qtpayParameterList.add(new Param("pinyin", ""));
        httpsPost("BankCardBind", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
               LogUtil.showToast(MyCreditCardActivity.this,payResult.getRespDesc()+"");
                setResult(RyxAppconfig.CLOSE_ALL);
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

    @Click(R.id.tv_payProtocols)
    public void showProtocols(){
        Intent intent = new Intent(MyCreditCardActivity.this, HtmlMessageActivity_.class);
        intent.putExtra("title", "快捷支付开通协议");
        intent.putExtra("ccurl", "");
        intent.putExtra("urlkey", RyxAppconfig.Notes_FastAgreement);
        startActivity(intent);

    }
    private void setmSendMacTv() {
        phoneStr = edt_phoneNum.getText().toString();
        if (TextUtils.isEmpty(phoneStr)||phoneStr.length() != 11) {
            LogUtil.showToast(this, "请输入正确的银行预留电话");
            return;
        }
        //发送验证码
        mTimer = null;
        mTimer = new Timer();
        getMobileMac();
    }
    private void getMobileMac() {
        qtpayApplication.setValue("SendAdvancedVipSMS.Req");
        Param phoneParam = new Param("bankTel");
        phoneParam.setValue(edt_phoneNum.getText().toString().trim());
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(phoneParam);
        httpsPost("getMobileMac", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(MyCreditCardActivity.this,
                        getResources().getString(R.string.sms_has_been_issued_please_note_that_check));
                startCountdown();
            }
        });
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
                mSendMacTv.setTextColor(ContextCompat.getColor(MyCreditCardActivity.this, R.color.text_a));
                mSendMacTv.setText(getResources().getString(R.string.resend) + "(" + msg.what + ")");
                mSendMacTv.setClickable(false);
            } else {
                mTimer.cancel();
                mSendMacTv.setText(getResources().getString(R.string.resend_verification_code));
                mSendMacTv.setClickable(true);
                mSendMacTv.setTextColor(ContextCompat.getColor(MyCreditCardActivity.this, R.color.colorPrimary));
            }
        }
    };
    private void checkSmsMac(String smsMac) {
        qtpayApplication.setValue("CheckSMSCode.Req");
        Param phoneParam = new Param("bankTel");
        String phoneNumber = edt_phoneNum.getText().toString();
        phoneParam.setValue(phoneNumber);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(phoneParam);
        qtpayParameterList.add(new Param("smsCode", smsMac));
        httpsPost("checkSmsMac", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                bindCreditCard();
            }
        });
    }
}
