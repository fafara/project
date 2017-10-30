package com.ryx.payment.ruishua.convenience;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.ContactHelper;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.swiper.utils.MoneyEncoder;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * 我要付款
 */
@EActivity(R.layout.activity_payment)
public class PaymentActivity extends BaseActivity {
    OrderInfo orderinfo;
    private String[] mPhoneString = null;
    @ViewById(R.id.et_accountid)
    EditText et_accountid;

    @ViewById(R.id.usernameid)
    TextView usernameid;
    Param qtpayTransType;
    String realName;// 付款账户名
    boolean isRegisted = false;// 用户是否注册
    @ViewById(R.id.default_accout_all)
    AutoLinearLayout default_accout_all;
    @ViewById(R.id.bankimgview)
    ImageView bankimgview;
    @ViewById(R.id.tv_bankCardnumber)
    TextView tv_bankCardnumber;
    @ViewById(R.id.payment_nextbtn)
    Button payment_nextbtn;
    @ViewById(R.id.et_paymoney)
    EditText et_paymoney;
    String cardNo = "", bankId = "", customerId = "", cardIdx = "";
    @ViewById(R.id.et_accountidline)
    View et_accountidline;
    @ViewById(R.id.et_paymoneyline)
    View et_paymoneyline;
    @ViewById(R.id.payment_contactImg)
    ImageView  payment_contactImg;
    @AfterViews
    public void initView() {
        setTitleLayout("我要付款", true, true);
        setRightImgMessage("用户须知", RyxAppconfig.Notes_Impay);
        initQtPatParams();
        initData();
        et_accountid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().replace(" ", "").length() == 11) {
                    getuserInfo();

                } else {
                    restViewData();
                }
            }
        });
    }

    private void initData() {
        Bundle bundle= getIntent().getExtras();
        if(bundle!=null&&!bundle.isEmpty()){
             String custoemrid=   bundle.getString("custoemrid");
             String username=   bundle.getString("username");
             String bankid=   bundle.getString("bankid");
             String cardno=   bundle.getString("cardno");
             String cardidx=   bundle.getString("cardidx");
             String mobileno=   bundle.getString("mobileno");
            realName = username;//等待加密
            usernameid.setText(username);
            default_accout_all.setVisibility(View.GONE);
            isRegisted = true;
            cardNo=cardno;
            bankId=bankid;
            customerId=custoemrid;
            et_accountid.setText(mobileno);
            et_accountid.setEnabled(false);
            et_accountid.setFocusable(false);
            payment_contactImg.setVisibility(View.GONE);
        }
    }

    /**
     * 重置数据和布局
     */
    private void restViewData() {
        usernameid.setText("——");
        default_accout_all.setVisibility(View.GONE);
        realName = "——";
        isRegisted = false;
        cardNo = "";
        bankId = "";
        customerId = "";
    }

    @FocusChange({R.id.et_accountid, R.id.et_paymoney})
    public void focusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.et_paymoney:
                et_paymoneyline.setBackgroundResource(R.color.login_edt_getfocus);
                et_accountidline.setBackgroundResource(R.color.login_edt_lostfocus);
                break;
            case R.id.et_accountid:
                et_accountidline.setBackgroundResource(R.color.login_edt_getfocus);
                et_paymoneyline.setBackgroundResource(R.color.login_edt_lostfocus);
                break;
        }
    }

    public void getuserInfo() {

        qtpayApplication.setValue("UserInfoQuery.Req");
        qtpayTransType = new Param("transType", "01");

        qtpayMobileNO.setValue(et_accountid.getText().toString());

        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayTransType);
        httpsPost("UserInfoQueryTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeJson(payResult.getData());
                usernameid.setText(realName);
                if (!isRegisted) {
                    LogUtil.showToast(PaymentActivity.this, "用户未注册");
                }
                String mobileNum = RyxAppdata.getInstance(PaymentActivity.this).getMobileNo();
                String account = et_accountid.getText().toString();
                if (!TextUtils.isEmpty(cardNo) && !TextUtils.isEmpty(bankId) && mobileNum.equals(account)) {
                    default_accout_all.setVisibility(View.VISIBLE);
                    BanksUtils.selectIcoidToImgView(PaymentActivity.this,bankId, bankimgview);
                    tv_bankCardnumber.setText(StringUnit.cardJiaMi(cardNo));
                } else {
                    default_accout_all.setVisibility(View.GONE);
                }
            }
        });
    }

    @Click(R.id.payment_contactImg)
    public void paymentContactImgClick() {
        //通讯录按钮点击事件
        checkContactPemission();
    }

    @Click(R.id.payment_nextbtn)
    public void payMentNextBtnClick() {
        //下一步按钮点击
        if (checkInput()) {
            //付款金额
            String moneyStr = MoneyEncoder.EncodeFormat(et_paymoney.getText().toString());
            //收款账户
            Intent intent = new Intent(PaymentActivity.this, CreateOrder_.class);
            orderinfo = Order.IMPAY_SHANFU;
            orderinfo.setOrderRemark("闪付支付");
            orderinfo.setOrderAmt(moneyStr);
            orderinfo.setOrderDesc(cardNo);
            orderinfo.setAccount2(realName);
            orderinfo.setPayee(customerId);
            orderinfo.setCardIdx(cardIdx);
            orderinfo.setDisPlayContent(et_accountid.getText().toString().trim() + "  " + realName);
            //小额付款
            orderinfo.setMerchantId(RyxAppdata.getInstance(PaymentActivity.this).getMerchantId());
            orderinfo.setProductId(RyxAppdata.getInstance(PaymentActivity.this).getProductId());
            intent.putExtra("orderinfo", orderinfo);
            startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
        }
    }

    /**
     * 手机访问权限
     */
    public void checkContactPemission() {
        String  waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg),RyxAppdata.getInstance(this).getCurrentBranchName());
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg), getResources().getString(R.string.app_name));
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg), getResources().getString(R.string.app_name_ryx));
//        }
        requesDevicePermission(waring, 0x0011, new PermissionResult() {

                    @Override
                    public void requestSuccess() {
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                ContactsContract.Contacts.CONTENT_URI), 0);
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE);

    }

    /**
     * 处理返回的手机号
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            setAccount(data);
        }
    }

    private void setAccount(Intent intent) {

        if (intent == null) {
            return;
        } else {
            ArrayList<String> tPhone = ContactHelper.getContactPhoneNo(intent.getData(), this);
            LogUtil.showLog("data===" + tPhone);
            if (tPhone.size() > 1) {
                mPhoneString = new String[tPhone.size()];
                for (int i = 0; i < tPhone.size(); i++) {
                    mPhoneString[i] = tPhone.get(i);
                }
                Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        SimpleDialog dialog = (SimpleDialog) fragment.getDialog();
                        if (dialog == null)
                            return;
                        final int index = dialog.getSelectedIndex();
                        fragment.dismiss();
                        String phoneNumber = mPhoneString[index];
                        String phonumberStr = phoneNumber.replace("+86", "").replace(" ", "").replace("-", "");
                        if (!TextUtils.isEmpty(phonumberStr) && phonumberStr.length() == 11) {
                            et_accountid.setText(phonumberStr);
                            et_accountid.setSelection(phonumberStr.length());
                        }
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        fragment.dismiss();
                    }
                };
                ((SimpleDialog.Builder) builder).items(mPhoneString, 0).title("请选择电话号码").positiveAction("确定").negativeAction("取消");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), null);
            } else if (tPhone.size() == 1) {
                String phonumberStr = tPhone.get(0).replace("+86", "").replace(" ", "").replace("-", "");
                if (!TextUtils.isEmpty(phonumberStr) && phonumberStr.length() == 11) {
                    et_accountid.setText(phonumberStr);
                    et_accountid.setSelection(phonumberStr.length());
                }
            } else {
                et_accountid.setText("");
            }
        }
    }

    /**
     * 解析返回的商户信息
     *
     * @author tianyingzhong <br/>
     * 修改日期 : 2014-06-18。
     */
    public void analyzeJson(String jsonstring) {
        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                        "result").getString("resultCode"))) {
                    JSONObject userinfo = jsonObj.getJSONObject("resultBean");
                    realName = userinfo.getString("realName");
                    if (userinfo.has("customerId")) {
                        customerId = userinfo.getString("customerId");
                    }
                    JSONObject bankCardDefault = null;
                    if (userinfo.has("bankCardDefault")) {
                        bankCardDefault = userinfo.getJSONObject("bankCardDefault");
                    }
                    if (bankCardDefault != null && bankCardDefault.has("cardNo") && bankCardDefault.has("bankId")) {
                        cardNo = bankCardDefault.getString("cardNo");
                        bankId = bankCardDefault.getString("bankId");
                        if (bankCardDefault.has("cardIdx")) {
                            cardIdx = bankCardDefault.getString("cardIdx");
                        }
                    } else {
                        cardNo = "";
                        bankId = "";
                    }
                    if ("".equals(realName)) {
                        realName = realName = MessageFormat.format(
                                    getResources().getString(R.string.phone_name),
                                RyxAppdata.getInstance(this).getCurrentBranchName());
//                        if (RyxAppconfig.BRANCH.equals("01")) {
//                            realName = realName = MessageFormat.format(
//                                    getResources().getString(R.string.phone_name),
//                                    getResources().getString(R.string.app_name));
//                        } else if (RyxAppconfig.BRANCH.equals("02")) {
//                            realName = realName = MessageFormat.format(
//                                    getResources().getString(R.string.phone_name),
//                                    getResources().getString(R.string.app_name_ryx));
//                        }
                    }
                    isRegisted = true;
                } else {
                    realName = "——";
                    isRegisted = false;
                    cardNo = "";
                    bankId = "";
                    customerId = "";
                }
            } else {
                realName = "——";
                isRegisted = false;
                cardNo = "";
                bankId = "";
                customerId = "";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 检查输入数据情况
     *
     * @return
     */
    private boolean checkInput() {
        if (!isRegisted) {
            LogUtil.showToast(PaymentActivity.this, "收款用户未注册,不能付款！");
            return false;
        }
        //收款账户
        String account = et_accountid.getText().toString();
        if (TextUtils.isEmpty(account) || account.length() != 11) {
            LogUtil.showToast(PaymentActivity.this, "请正确填写收款账号！");
            return false;
        }
        //付款金额
        String payMoney = et_paymoney.getText().toString();
        if (TextUtils.isEmpty(payMoney)) {
            LogUtil.showToast(PaymentActivity.this, "请正确填写付款金额");
            return false;
        }
        if ("￥0.00".equals(MoneyEncoder.EncodeFormat(payMoney))) {
            LogUtil.showToast(PaymentActivity.this, "交易金额必须大于零");
            return false;
        }
        if (TextUtils.isEmpty(cardNo)) {
            LogUtil.showToast(PaymentActivity.this, "收款账户未绑定默认结算卡,不允许付款!");
            return false;
        }
        if (TextUtils.isEmpty(customerId)) {
            LogUtil.showToast(PaymentActivity.this, "收款用户未注册,不能交易!");
            return false;
        }
        return true;
    }
}
