package com.ryx.payment.ruishua.recharge;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.QcoinInfo;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.CreateOrder_;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.recharge.adapter.MobileAdapter;
import com.ryx.payment.ruishua.recharge.adapter.MobileAdapter_;
import com.ryx.payment.ruishua.utils.ContactHelper;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

@EActivity(R.layout.activity_suningrecharge)
public class SuNingRechargeActivity extends BaseActivity {

    @ViewById(R.id.et_mobile)
    EditText mMobileEt;
    @ViewById(R.id.layout_mobile)
    LinearLayout mMobileLine;
    @ViewById(R.id.gv_suning)
    GridView mSuningGridView;
    private String mMoney;
    private Param qtpayGameId;
    private String gameid = "CARD60280";
    private String jsonStr;
    private QcoinInfo qcoinInfo;
    private ArrayList<QcoinInfo> coinlist = new ArrayList<>();
    private MobileAdapter adapter;
    private int selectid = 0;
    private String[] mPhoneString;
    private String mobile;
    private OrderInfo orderinfo;

    @AfterViews
    public void initViews() {
        setTitleLayout("苏宁易购卡", true);
        setRightImgMessage("用户须知", RyxAppconfig.Notes_SUNINGRecharge);
        initQtPatParams();
        doOrderRequest();
    }

    private void doOrderRequest() {
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayGameId);
        httpsPost("doAliPayReq", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                jsonStr = payResult.getData();
                LogUtil.showLog("muxin", "suning json:" + jsonStr);
                initListData();
                initData();
            }
        });
    }

    private void initListData() {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray banks = jsonObj.getJSONArray("resultBean");
            for (int i = 0; i < banks.length(); i++) {
                qcoinInfo = new QcoinInfo();
                qcoinInfo.setGameid(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "gameid"));
                qcoinInfo.setGamename(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "gamename"));
                qcoinInfo.setOnlineid(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "onlineid"));
                qcoinInfo.setOnlinename(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "onlinename"));
                qcoinInfo.setParvalue(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "parvalue"));
                qcoinInfo.setSaleprice(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "saleprice"));
                qcoinInfo.setBigDidsName("￥"+JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "onlinename"));
                coinlist.add(qcoinInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application", "GetGameMoney.Req");
        // 商品编码
        qtpayGameId = new Param("gameId", gameid);
    }

    private void initData() {
        mMobileEt.setText(QtpayAppData.getInstance(SuNingRechargeActivity.this).getMobileNo());
        mMobileEt.setSelection(mMobileEt.getText().length());
        adapter = MobileAdapter_.getInstance_(SuNingRechargeActivity.this);
        adapter.setList(coinlist);
        adapter.setSelection(0);
        mMoney = MoneyEncoder.EncodeFormat(coinlist.get(0).getParvalue());
        mSuningGridView.setAdapter(adapter);
    }

    @ItemClick(R.id.gv_suning)
    public void mobileClick(int position) {
        mMoney = MoneyEncoder.EncodeFormat(coinlist.get(position).getParvalue());
        selectid = position;
        adapter.setSelection(position);
        adapter.notifyDataSetChanged();
    }

    @Click(R.id.iv_select_contact)
    public void selectContact() {
        checkContactPermission();
    }

    private boolean checkInput() {
        mobile = mMobileEt.getText().toString().trim();
        if (TextUtils.isEmpty(mobile) || mobile.length() != 11) {
            LogUtil.showToast(SuNingRechargeActivity.this, getResources().getString(R.string.please_enter_the_11_phone_numbers));
            return false;
        } else if (mobile.substring(0, 1).equals("0")) {
            LogUtil.showToast(SuNingRechargeActivity.this, getResources().getString(R.string.not_a_phone_number));
            return false;
        } else if (TextUtils.isEmpty(mMoney)) {
            LogUtil.showToast(
                    SuNingRechargeActivity.this, "请选择充值金额!!!");
            return false;
        }
        return true;
    }

    @Click(R.id.btn_recharge)
    public void rechargeClick(){
        if (checkInput()) {
            Intent intent = new Intent(SuNingRechargeActivity.this, CreateOrder_.class);
            orderinfo = Order.SUNING_RECHARGE;
            orderinfo.setOrderDesc(gameid
                    + "|"
                    + MoneyEncoder
                    .EncodeFormat(
                            coinlist.get(selectid).getParvalue())
                    .replace("￥", "").replace(".", "").replace(",", "") + "|"
                    +mobile+"|" + PhoneinfoUtils.getPsdnIp());
            orderinfo.setOrderAmt(mMoney);
            orderinfo.setOrderRemark(mobile);
            intent.putExtra("orderinfo", orderinfo);
            startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
        }
    }

    /**
     * 手机访问权限
     */
    public void checkContactPermission() {
        String waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
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
            if (tPhone.size() > 1) {
                mPhoneString = new String[tPhone.size()];
                for (int i = 0; i < tPhone.size(); i++) {
                    mPhoneString[i] = tPhone.get(i);
                }
                SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        SimpleDialog dialog = (SimpleDialog) fragment.getDialog();
                        if (dialog == null)
                            return;
                        final int index = dialog.getSelectedIndex();
                        fragment.dismiss();
                        String phoneNumber = mPhoneString[index];
                        mMobileEt.setText(phoneNumber.replace("+86", "").replace(" ", "").replace("-", ""));
                    }

                    @Override
                    public void onNeutralActionClicked(DialogFragment fragment) {
                        fragment.dismiss();
                    }
                };
                builder.items(mPhoneString, 0).title("请选择电话号码").positiveAction("确定").negativeAction("取消");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), null);
            } else if (tPhone.size() == 1) {
                mMobileEt.setText(tPhone.get(0).replace("+86", "").replace(" ", "").replace("-", ""));
            } else {
                mMobileEt.setText("");
            }
        }
    }

//
//    @TextChange(R.id.et_mobile)
//    public void mobileTextChanged(CharSequence str, TextView hello, int before, int start, int count) {
//        String contents = str.toString();
//        int length = contents.length();
//        if (length == 4) {
//            if (contents.substring(3).equals(new String(" "))) { // -
//                contents = contents.substring(0, 3);
//                mMobileEt.setText(contents);
//                mMobileEt.setSelection(contents.length());
//            } else { // +
//                contents = contents.substring(0, 3) + " " + contents.substring(3);
//                mMobileEt.setText(contents);
//                mMobileEt.setSelection(contents.length());
//            }
//        } else if (length == 9) {
//            if (contents.substring(8).equals(new String(" "))) { // -
//                contents = contents.substring(0, 8);
//                mMobileEt.setText(contents);
//                mMobileEt.setSelection(contents.length());
//            } else {// +
//                contents = contents.substring(0, 8) + " " + contents.substring(8);
//                mMobileEt.setText(contents);
//                mMobileEt.setSelection(contents.length());
//            }
//        }
//    }
}
