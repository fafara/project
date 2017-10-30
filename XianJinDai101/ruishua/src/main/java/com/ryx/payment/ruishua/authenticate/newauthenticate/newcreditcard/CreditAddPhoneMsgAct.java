package com.ryx.payment.ruishua.authenticate.newauthenticate.newcreditcard;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.UserAuthResultDialog;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 高级认证中添加手机等信息
 */
@EActivity(R.layout.activity_credit_add_phone_msg)
public class CreditAddPhoneMsgAct extends BaseActivity {


    @ViewById
    TextView tv_userName;
    @ViewById
    EditText et_card_type;

    @ViewById(R.id.tv_again_mac)
    TextView tv_again_mac;
    @ViewById
    EditText edt_phoneNum, edt_checkNum;
    @ViewById(R.id.btn_next)
    Button btn_next;

    private String cardno = "";
    private boolean fullLength = false;
    private String bankcardid = "";
    private String phoneStr = "";
    private String checkNum = "";
    private Timer mTimer = new Timer();

    private String realName;
    private BankCardInfo bankCardInfo;
    private Param qtpayCardNo;
    private String bankId;
    private String cardType;
    private String bankName;
    private String shortBankName;//银行名称缩写
    private String cardTypeTxt;//卡类型
    private String cardNo;//卡号
    private UserAuthResultDialog userAuthSuccessDialog;
    private static final int CREDITFINISHED=0x1000;

    @AfterViews
    public void initViews() {
        setTitleLayout("信用卡认证", true, false);
        getIntentData();
        edt_phoneNum.requestFocus();
        tv_userName.setText(QtpayAppData.getInstance(this).getRealName());
        initQtPatParams();
        userAuthSuccessDialog=new UserAuthResultDialog(CreditAddPhoneMsgAct.this);

    }

    private void getIntentData() {
        bankcardid = getIntent().getStringExtra("bankcardId");
        cardNo = getIntent().getStringExtra("cardNo");
        bankCardInfo = (BankCardInfo) getIntent().getSerializableExtra("bankCardInfo");
        bankName = getIntent().getStringExtra("bankname");
        cardType = getIntent().getStringExtra("cardtype");
        et_card_type.setText(bankName + cardType);
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
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
        phoneStr = edt_phoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11) {
            LogUtil.showToast(CreditAddPhoneMsgAct.this, "请输入正确的银行预留电话！");
            return;
        }
        getMobileMac();
        // 开始倒计时
        startCountdown();
    }

    private void getMobileMac() {
        qtpayApplication.setValue("SendAdvancedVipSMS.Req");
        Param phoneParam = new Param("bankTel");
        phoneParam.setValue(phoneStr);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(phoneParam);
        httpsPost("SendAdvancedVipSMS", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(CreditAddPhoneMsgAct.this,
                        getResources().getString(R.string.sms_has_been_issued_please_note_that_check));
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
        phoneStr = edt_phoneNum.getText().toString().trim();
        checkNum = edt_checkNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneStr) || phoneStr.length() != 11) {
            LogUtil.showToast(CreditAddPhoneMsgAct.this, "请输入正确的银行预留电话！");
            return;
        }
        if (TextUtils.isEmpty(checkNum) || checkNum.length() != 4) {
            LogUtil.showToast(CreditAddPhoneMsgAct.this, "请输入正确的验证码！");
            return;
        }
        uploadcardno();
    }

    //卡号校验
    private void uploadcardno() {
        initQtPatParams();
        qtpayApplication.setValue("CustomerVIPAuthByLive.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("verifiCode", checkNum));//验证码
        qtpayParameterList.add(new Param("cardNo", cardNo));//卡号
        qtpayParameterList.add(new Param("userMobileNo", phoneStr));//银行预留手机号
        httpsPost("CustomerVIPAuthByLive", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    JSONObject jsonObject = new JSONObject(payResult.getData());
                    String code  = jsonObject.getString("code");
                    if("0000".equals(code)){
                        userAuthSuccessDialog.showDefinedDialog(R.drawable.dialog_userauth_success_img,"恭喜你！认证通过！","随心支付享受移动生活，赶快体验吧！","我知道了",new UserAuthResultDialog.UserAuthResultDialogBtnListen(){
                            @Override
                            public void btnOkClick(View view) {
                                userAuthSuccessDialog.dismiss();
                                setResult(CREDITFINISHED);
                                finish();
                            }

                            @Override
                            public void btnReturnClick(View view) {

                            }
                        },null);
                    }else if("9999".equals(code)){
                        String msg = jsonObject.getString("msg");
                        if(TextUtils.isEmpty(msg)){
                            msg =  "等待人工审核！";
                        }
                        userAuthSuccessDialog.showDefinedDialog(R.drawable.dialog_authresult_processing,"等待人工审核！",msg,"我知道了",new UserAuthResultDialog.UserAuthResultDialogBtnListen(){
                            @Override
                            public void btnOkClick(View view) {
                                userAuthSuccessDialog.dismiss();
                                setResult(CREDITFINISHED);
                                finish();
                            }

                            @Override
                            public void btnReturnClick(View view) {

                            }
                        },null);
                    }else if("1000".equals(code)){
                        String msg = jsonObject.getString("msg");
                        if(TextUtils.isEmpty(msg)){
                            msg =  "请仔细核对认证信息准确无误后再次认证！";
                        }
                        userAuthSuccessDialog.showDefinedDialog(R.drawable.dialog_authresult_fail,"对不起！认证失败！",msg,"重新认证",new UserAuthResultDialog.UserAuthResultDialogBtnListen(){
                            @Override
                            public void btnOkClick(View view) {
                                userAuthSuccessDialog.dismiss();
                            }
                            @Override
                            public void btnReturnClick(View view) {
                                userAuthSuccessDialog.dismiss();
                                setResult(RyxAppconfig.CLOSE_ALL);
                                finish();
                            }
                        },"返回首页");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.showToast(CreditAddPhoneMsgAct.this, "数据解析异常！");
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
}
