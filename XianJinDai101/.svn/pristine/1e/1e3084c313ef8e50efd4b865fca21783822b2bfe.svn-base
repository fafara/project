package com.ryx.payment.ruishua.authenticate.creditcard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.CheckBox;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.ExpiryFilter;
import com.ryx.payment.ruishua.utils.LogUtil;
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

/**
 * Created by xiepingping on 2016/5/27.
 */
@EActivity(R.layout.activity_credit_card_auth_step2)
public class CreditCardAuthStep2Activity extends BaseActivity  {

    @ViewById
    TextView tv_cardno;

    @ViewById
    View line3,line6,line2;
    @ViewById
    TextView tv_whereenddate;
    @ViewById(R.id.edt_enddate)
    EditText edt_enddate;
    @ViewById(R.id.tv_again_mac)
    TextView tv_again_mac;
    @ViewById
    EditText edt_phoneNum,edt_checkNum;
    @ViewById
    EditText edt_securityCode;
    @ViewById(R.id.btn_next)
    Button btn_next;
    @ViewById(R.id.credit_card_swiperimg)
    ImageView credit_card_swiperimg;

    @ViewById
    CheckBox cb_agree;
    @ViewById
    AutoRelativeLayout lay_contract,lay_secury;

    @ViewById
    AutoLinearLayout lay_enddate;

    private String cardno = "", expirydate = "";
    InputFilter[] filters = {new ExpiryFilter()};
    private int month;
    private int year;
    private boolean fullLength = false;
    private String bankcardid = "";
    private String phoneStr = "";
    private String checkNum = "",securCode="";
    private String cardNumber,kjzf_status,needcvn;
    private Timer mTimer = new Timer();
    Param qtpayflag;
    Param qtpayAccountNo;
    private Param qtpayAppType;
    private Param qtpayOrderId;


    @AfterViews
    public void initViews() {
        setTitleLayout("信用卡认证", true, false);
        bankcardid = getIntent().getStringExtra("BankcardId");

        cardNumber = getIntent().getStringExtra("cardNumber");
        expirydate = getIntent().getStringExtra("expirydate");
        kjzf_status = getIntent().getStringExtra("kjzf_status");
        needcvn = getIntent().getStringExtra("needcvn");

        initQtPatParams();
        initData();
    }


    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayflag = new Param("flag", "01");
        qtpayAccountNo = new Param("accountNo");
    }

    /**
     * 展示有效期在哪popwindow提示框
     */
    @Click(R.id.tv_whereenddate)
    public void showHelpPopWindow() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(CreditCardAuthStep2Activity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(CreditCardAuthStep2Activity.this).inflate(R.layout.expiryhelppopview, null);
        mBottomSheetDialog.contentView(boottomView).show();
    }

    private void initData() {

        if(!TextUtils.isEmpty(cardNumber)){
            tv_cardno.setText(cardNumber);
        }
        if("1".equals(kjzf_status)){
            lay_contract.setVisibility(View.VISIBLE);
        }else{
            lay_contract.setVisibility(View.GONE);
        }
            edt_enddate.setInputType(InputType.TYPE_CLASS_PHONE);
            edt_enddate.setFilters(filters);
            if (!TextUtils.isEmpty(expirydate)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMM");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMyy");
                simpleDateFormat.setLenient(false);
                try {
                    Date expiry = simpleDateFormat.parse(expirydate);
                    month = 0;
                    year = 0;
                    edt_enddate.setText(simpleDateFormat2.format(expiry));
                    fullLength = (expirydate.length() >= 4);
//                changeButtonState();
//                    edt_enddate.setEnabled(false);
//                    edt_enddate.setFocusable(false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            edt_enddate.addTextChangedListener(new TextWatcher() {
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

        if("1".equals(needcvn)){
            lay_secury.setVisibility(View.VISIBLE);
        }else{
            lay_secury.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
        }
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

    /**
     * 根据输入判断按钮的显示颜色和提示文字的显示
     */
    public void changeButtonState() {
        if (fullLength) {
            btn_next.setBackgroundResource(R.drawable.blue_button_bg);
            btn_next.setClickable(true);
        } else {
            btn_next.setBackgroundResource(R.drawable.grey_button_bg);
            btn_next.setClickable(false);
        }
    }

    Handler timeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what > 0) {
                tv_again_mac.setTextColor(getResources().getColor(R.color.text_a));
                tv_again_mac.setText(getResources().getString(R.string.resend)
                        + "(" + msg.what + ")");
                tv_again_mac.setClickable(false);
            } else {
                mTimer.cancel();
                tv_again_mac.setText(getResources().getString(
                        R.string.resend_verification_code));
                tv_again_mac.setClickable(true);
                tv_again_mac.setTextColor(Color.parseColor("#1db7f0"));
            }
        }
    };

    @Click(R.id.tv_again_mac)
    public void getCheckNo() {
        mTimer = null;
        mTimer = new Timer();
        phoneStr = edt_phoneNum.getText().toString();
        if (TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11) {
            LogUtil.showToast(CreditCardAuthStep2Activity.this, "请输入正确的银行预留电话！");
            return;
        }
        getMobileMac();
        // 开始倒计时
        startCountdown();
    }

    private void getMobileMac() {
        qtpayApplication.setValue("SendAdvancedVipSMS.Req");
        Param phoneParam=new Param("bankTel");
        phoneParam.setValue(phoneStr);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(phoneParam);
        httpsPost("SendAdvancedVipSMS", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(CreditCardAuthStep2Activity.this,
                        getResources().getString(R.string.sms_has_been_issued_please_note_that_check));
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
                Message msg = Message.obtain();
                msg.what = secondsRremaining--;
                timeHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(task, 1000, 1000);
    }
    @Click(R.id.btn_next)
    public void nextStep() {
        phoneStr = edt_phoneNum.getText().toString();
        checkNum = edt_checkNum.getText().toString();
//        if(TextUtils.isEmpty(cardmnumber)||cardmnumber.length()<14){
//            LogUtil.showToast(CreditCardAuthStep2Activity.this, "请正确输入卡号或者刷卡获取！");
//            return;
//        }
        expirydate = edt_enddate.getText().toString();
            if (TextUtils.isEmpty(expirydate)) {
                LogUtil.showToast(CreditCardAuthStep2Activity.this, "请输入卡片有效截止效期！");
                return;
            }
        if("1".equals(needcvn)){
            securCode = edt_securityCode.getText().toString();
            if ( TextUtils.isEmpty(securCode) ) {
                LogUtil.showToast(CreditCardAuthStep2Activity.this, "请输入正确的安全码！");
                return;
            }
        }
        if (TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11) {
            LogUtil.showToast(CreditCardAuthStep2Activity.this, "请输入正确的银行预留电话！");
            return;
        }
        if (TextUtils.isEmpty(checkNum) || checkNum.length() != 4) {
            LogUtil.showToast(CreditCardAuthStep2Activity.this, "请输入正确的验证码！");
            return;
        }

        if("1".equals(kjzf_status)){
            if(!cb_agree.isChecked()){
                LogUtil.showToast(CreditCardAuthStep2Activity.this, "请同意快捷支付协议");
                return;
            }
        }
        uploadcardno();
    }
    @Click(R.id.tv_payProtocols)
    public void showProtocols(){
        Intent intent = new Intent(CreditCardAuthStep2Activity.this, HtmlMessageActivity_.class);
        intent.putExtra("title", "快捷支付开通协议");
        intent.putExtra("ccurl", "");
        intent.putExtra("urlkey", RyxAppconfig.Notes_FastAgreement);
        startActivity(intent);

    }
    //卡号校验
    private void uploadcardno() {
        qtpayApplication.setValue("CheckAdvancedVip.Req");
        qtpayAccountNo.setValue(cardNumber);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("bankTel", phoneStr));
        qtpayParameterList.add(qtpayflag);
        qtpayParameterList.add(qtpayAccountNo);
        if("1".equals(needcvn)){
            qtpayParameterList.add(new Param("cardCvn", securCode));
        }
        qtpayParameterList.add(new Param("validDate", expirydate));
        qtpayParameterList.add(new Param("verifiCode", checkNum));
        httpsPost("CheckAdvancedVip", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                if ("0".equals(payResult.getPayStatus())) {
                    startActivityForResult(
                            new Intent(CreditCardAuthStep2Activity.this,
                                    CreditCardAuthStep3Activity_.class)
                                    .putExtra("CARDNO",cardNumber)
                                    .putExtra("CARDDATE",
                                            expirydate)
                                    .putExtra("BankcardId", bankcardid)
                                    .putExtra("bankTel", phoneStr),
                            RyxAppconfig.WILL_BE_CLOSED);
                } else {
                    startActivityForResult(new Intent(CreditCardAuthStep2Activity.this,
                                    CreditCardCheckFailActivity_.class).putExtra("RespDesc",
                            payResult.getOldText()),
                            RyxAppconfig.WILL_BE_CLOSED);

                }
            }
        });
    }
}
