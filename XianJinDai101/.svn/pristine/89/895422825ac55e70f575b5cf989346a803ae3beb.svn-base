package com.ryx.payment.ruishua.bindcard;

import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.CityInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.ProvinceInfo;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.adapter.BranchAdapter;
import com.ryx.payment.ruishua.bindcard.adapter.CityAdapter;
import com.ryx.payment.ruishua.bindcard.adapter.ProvinceAdapter;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 填写储蓄卡信息
 *
 * @author muxin
 * @time 2016-09-14 10:44
 */
@EActivity(R.layout.activity_debit_card_info)
public class DebitCardInfoAddActivity extends BaseActivity {

    @ViewById
    AutoRelativeLayout province_id,city_id,lay_id;


    @ViewById(R.id.tv_userName)
    TextView mUserNameTv;//姓名
    @ViewById(R.id.tv_cardtype)
    TextView mCardTypeTv;//卡类型
    @ViewById(R.id.edt_province)
    EditText mProvinceEt;//省份
    @ViewById(R.id.edt_city)
    EditText mCityEt;//城市
    @ViewById(R.id.edt_branch)
    EditText mBranchEt;//支行
    @ViewById(R.id.edt_mobile)
    EditText mMobileEt;//手机号
    @ViewById(R.id.edt_mac)
    EditText mMacEt;//验证码
    @ViewById(R.id.tv_again_mac)
    TextView mSendMacTv;//发送验证码
    @ViewById(R.id.btn_back)
    ImageView mBackIv;//后退
    @ViewById(R.id.cb_agree)
    CheckBox mAgreeCb;//同意单选框
    @ViewById(R.id.tv_pay_agree)
    TextView mPayAgreeTv;//同意协议
    @ViewById(R.id.btn_next)
    Button mNextBtn;//完成按钮
    @Bean
    ProvinceAdapter provinceAdapter;
    @Bean
    CityAdapter cityAdapter;
    @Bean
    BranchAdapter branchAdapter;
    private com.rey.material.app.Dialog branchDialog;
    private String numberString;
    private Timer mTimer = new Timer();
    private Param qtpayAppType;
    private Param qtpayOrderId;
    private String macString;
    private int pPosition;
    private ArrayList<ProvinceInfo> allcitys = new ArrayList<ProvinceInfo>();
    public static final int SHOW_PROVINCE = 1;
    public static final int SHOW_CITY = 2;
    private String bankProvinceId = "";
    private String bankCityId = "";
    private boolean isProvinceSelected = false,
            isCitySelected = false, isBranchSelected = false;
    private String islast = "0";
    private String condition = "";
    private int offset = 0;
    private boolean isRefresh;
    private boolean isBranchDialogshow;
    private LinkedList<BankCardInfo> bankCardInfoList = new LinkedList<BankCardInfo>();
    private MaterialRefreshLayout materialRefreshLayout;
    private Param qtpayBankId;
    private Param qtpayBankCityId;
    private Param qtpayBankProvinceId;
    private Param qtpayCondition;
    private Param qtpayOffset;
    private String bankId, bankName, branchId = "",needbranch="1",branchid2="",hissuers="";
    private BankCardInfo cardinfo;
    private BankCardInfo bankCardInfo;
    private String usertype;
    private String cardType;

    @AfterViews
    public void initView() {
        bankCardInfo = (BankCardInfo) getIntent().getExtras().get("bankCardInfo");
        usertype = getIntent().getExtras().getString("usertype");
        bankName = getIntent().getExtras().getString("bankname");
        cardType = getIntent().getExtras().getString("cardtype");
        bankId = getIntent().getExtras().getString("bankid");
        branchid2 = getIntent().getExtras().getString("branchid2");
        //0不选1是选
        needbranch = getIntent().getExtras().getString("needbranch");
        hissuers = getIntent().getExtras().getString("hissuers");
        mUserNameTv.setText(bankCardInfo.getName());
        mCardTypeTv.setText(bankName + cardType);
        initQtPatParams();
        pPosition = 0;
        if("0".equals(needbranch)){
            viewGone();
        }else{
            viewVisible();
        }
        initListData();
        mNextBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                btnClick();
            }
        });
        mSendMacTv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                setmSendMacTv();
            }
        });
    }


    /**
     * 支行选择隐藏
     */
    private void viewGone(){
        province_id.setVisibility(View.GONE);
        city_id.setVisibility(View.GONE);
        lay_id.setVisibility(View.GONE);
    }
    /**
     * 支行选择展示
     */
    private void viewVisible(){
        province_id.setVisibility(View.VISIBLE);
        city_id.setVisibility(View.VISIBLE);
        lay_id.setVisibility(View.VISIBLE);
    }
    public void initListData() {
        // 获取中国省市区信息
        String jsonString = getRawCitys().toString();
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            // 获得一个省的数组
            JSONArray citysArray = jsonObj.getJSONArray("resultBean");
            for (int i = 0; i < citysArray.length(); i++) {
                // 创建一个新的省份
                ProvinceInfo provinceinfo = new ProvinceInfo();
                JSONObject province = citysArray.getJSONObject(i);

                JSONArray citys = province.getJSONArray("citys");
                // 创建该省的城市列表list
                ArrayList<CityInfo> cityslist = new ArrayList<CityInfo>();
                for (int j = 0; j < citys.length(); j++) {
                    JSONObject cityjson = citys.getJSONObject(j);
                    CityInfo city = new CityInfo(
                            cityjson.getString("cityCode"),
                            cityjson.getString("cityName"));
                    cityslist.add(city);
                }
                provinceinfo
                        .setProvinceCode(province.getString("provinceCode"));
                provinceinfo
                        .setProvinceName(province.getString("provinceName"));
                provinceinfo.setCityslist(cityslist);
                allcitys.add(provinceinfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取城市列表数据
     */
    public String getRawCitys() {
        InputStream in = getResources().openRawResource(R.raw.cities);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            br.close();
            isr.close();
            in.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return sb.toString();
    }

    @Click(R.id.edt_province)
    public void searchProvince() {
        creatDialog(SHOW_PROVINCE);
    }

    @Click(R.id.edt_city)
    public void searchCity() {
        if (isProvinceSelected) {
            creatDialog(SHOW_CITY);
        } else {
            LogUtil.showToast(DebitCardInfoAddActivity.this, getResources().getString(R.string.please_select_province_bank));
        }
    }

    @Click(R.id.edt_branch)
    public void searchBranch() {
        islast = "0";
        condition = "";
        offset = 0;
        isRefresh = false;
        isBranchDialogshow = false;
        bankCardInfoList.clear();
        if (isParamsOK()) {
            initBranchList();
        }
    }

    private void initBranchList() {
        if (!"0".equals(islast)) {
            LogUtil.showToast(DebitCardInfoAddActivity.this, "无记录！");
            if (isRefresh) {
                materialRefreshLayout.finishRefreshLoadMore();
            }
            return;
        }
        initQtPatParams();
        if (isRefresh) {
            isNeedThread = false;
        }
        qtpayApplication = new Param("application", "GetBankBranch.Req");
        qtpayBankId = new Param("bankId");
        qtpayBankCityId = new Param("bankCityId");
        qtpayBankProvinceId = new Param("bankProvinceId");
        qtpayCondition = new Param("condition");
        qtpayOffset = new Param("offset");
        qtpayCondition.setValue(condition);
        qtpayBankId.setValue(bankId.length()>3?bankId.substring(1,4):bankId);
        qtpayBankCityId.setValue(bankCityId);
        qtpayBankProvinceId.setValue(bankProvinceId);
        qtpayOffset.setValue(offset + "");
        offset = offset + 20;
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBankId);
        qtpayParameterList.add(qtpayBankCityId);
        qtpayParameterList.add(qtpayBankProvinceId);
        qtpayParameterList.add(qtpayCondition);
        qtpayParameterList.add(qtpayOffset);
        httpsPost("GetBankBranch", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                if (isRefresh) {
                    materialRefreshLayout.finishRefreshLoadMore();
                    isRefresh = false;
                }
                if (payResult.getData() != null) {
                    LinkedList<BankCardInfo> list = getSubBranchList(payResult.getData());
                    if (list != null && list.size() > 0) {
                        bankCardInfoList.addAll(list);
                        list = null;
                        branchAdapter.notifyDataSetChanged();
                        if (!isBranchDialogshow) {
                            showBranchDialog();
                        }
                    }
                }
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                if (isRefresh) {
                    materialRefreshLayout.finishRefreshLoadMore();
                    isRefresh = false;
                }
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                if (isRefresh) {
                    materialRefreshLayout.finishRefreshLoadMore();
                    isRefresh = false;
                }
            }
        });
    }

    //显示分行
    private void showBranchDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        AutoLinearLayout layout = (AutoLinearLayout) inflater.inflate(R.layout.bind_card_branch_bank_list, null);
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        com.rey.material.app.Dialog.Builder builder =
                new com.rey.material.app.Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        branchDialog = builder.build(DebitCardInfoAddActivity.this);
        materialRefreshLayout = (MaterialRefreshLayout) layout.findViewById(R.id.materialRefreshLayout);
        final EditText edt_condition = (EditText) layout.findViewById(R.id.edt_condition);
        ImageView btn_search = (ImageView) layout.findViewById(R.id.btn_search);
        ListView lv_bank = (ListView) layout.findViewById(R.id.lv_bank);
        ImageView imgview_close = (ImageView) layout.findViewById(R.id.imgview_close);
        imgview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchDialog.dismiss();
            }
        });
        branchAdapter.setList(bankCardInfoList);
        lv_bank.setAdapter(branchAdapter);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankCardInfoList != null) {
                    bankCardInfoList.clear();
                    branchAdapter.notifyDataSetChanged();
                    offset = 0;
                    condition = edt_condition.getText().toString();
                    islast = "0";
                    initBranchList();
                }
            }
        });
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                isRefresh = true;
                initBranchList();
            }

        });
        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                branchDialog.dismiss();
                isBranchDialogshow = false;
                isBranchSelected = true;
                branchId = bankCardInfoList.get(position).getBranchBankId();
                mBranchEt.setText(bankCardInfoList.get(position).getBranchBankName());
            }
        });
        branchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isBranchDialogshow = false;
            }
        });
        branchDialog.setContentView(layout);
        branchDialog.show();
        isBranchDialogshow = true;
    }

    public LinkedList<BankCardInfo> getSubBranchList(String jsonstring) {
        LinkedList<BankCardInfo> list = null;
        String toastmsg = "";
        try {
            if (!islast.equals("1")) {    // 如果不是最后一页，才可以进一步加载更多
                JSONObject jsonObj = new JSONObject(jsonstring);
                islast = (String) jsonObj.getJSONObject("summary").getString("isLast");

                toastmsg = (String) jsonObj.getJSONObject("result").getString("message");
                if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject("result").getString("resultCode"))) {
                    // 解析银行信息
                    JSONArray banks = jsonObj.getJSONArray("resultBean");
                    list = new LinkedList<BankCardInfo>();
                    for (int i = 0; i < banks.length(); i++) {
                        cardinfo = new BankCardInfo();
                        cardinfo.setBranchBankName(banks.getJSONObject(i).getString("bankName"));
                        cardinfo.setBranchBankId(banks.getJSONObject(i).getString("bankId"));
                        list.add(cardinfo);
                        cardinfo = null;
                    }
                    return list;
                } else {
                    LogUtil.showToast(DebitCardInfoAddActivity.this, toastmsg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查省份城市
     */
    public boolean isParamsOK() {
        if (!isProvinceSelected && bankProvinceId.length() == 0) {
            LogUtil.showToast(DebitCardInfoAddActivity.this,
                    getResources().getString(R.string.please_select_province_bank));
            return false;
        }
        if (!isCitySelected && bankCityId.length() == 0) {
            LogUtil.showToast(DebitCardInfoAddActivity.this,
                    getResources().getString(R.string.please_select_bank_city));
            return false;
        }
        return true;
    }

    private void creatDialog(final int dialogType) {
        LayoutInflater inflater = LayoutInflater.from(this);
        AutoLinearLayout layout = (AutoLinearLayout) inflater.inflate(R.layout.bind_debit_card_select_povince, null);
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        com.rey.material.app.Dialog.Builder builder =
                new com.rey.material.app.Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        final com.rey.material.app.Dialog dialog = builder.build(DebitCardInfoAddActivity.this);
        ImageView imgview_close = (ImageView) layout.findViewById(R.id.imgview_close);
        imgview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ListView lv_bank = (ListView) layout.findViewById(R.id.lv_location);
        TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
        if (dialogType == SHOW_PROVINCE) {
            tv_title.setText(getResources().getString(R.string.please_select_a_provinces));
            provinceAdapter.setList(allcitys);
            lv_bank.setAdapter(provinceAdapter);
        } else {
            tv_title.setText(getResources().getString(R.string.please_select_a_city));
            cityAdapter.setList(allcitys.get(pPosition).getCityslist());
            lv_bank.setAdapter(cityAdapter);
        }

        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (dialogType == SHOW_PROVINCE) {
                    pPosition = position;
                    bankProvinceId = allcitys.get(pPosition).getProvinceCode();
                    mProvinceEt.setText(allcitys.get(pPosition).getProvinceName());
                    // 判断该省下是否有市级
                    if (allcitys.get(pPosition).getCityslist().size() < 1) {
                        mCityEt.setText("");

                    } else {
                        mCityEt.setText(allcitys.get(pPosition).getCityslist()
                                .get(0).getCityName());
                        bankCityId = allcitys.get(pPosition).getCityslist()
                                .get(0).getCityCode();
                    }
                    isProvinceSelected = true;
                    isCitySelected = false;
                    isBranchSelected = false;
                    bankCityId = "";
                    mBranchEt.setText("");
                    creatDialog(SHOW_CITY);
                } else {
                    bankCityId = allcitys.get(pPosition).getCityslist()
                            .get(position).getCityCode();
                    mCityEt.setText(allcitys.get(pPosition).getCityslist()
                            .get(position).getCityName());
                    isCitySelected = true;
                    isBranchSelected = false;
                    mBranchEt.setText("");
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        dialog.show();
    }

    private boolean checkInput() {
        numberString = mMobileEt.getText().toString().trim();
        macString = mMacEt.getText().toString().trim();
        if (TextUtils.isEmpty(numberString)) {
            LogUtil.showToast(this, "请输入手机号码");
            return false;
        }
        if (numberString.length() != 11) {
            LogUtil.showToast(this, "手机号码为11位");
            return false;
        }
        if (TextUtils.isEmpty(macString)) {
            LogUtil.showToast(this, "请输入验证码");
            return false;
        }
        if (macString.length() != 4) {
            LogUtil.showToast(this, "验证码为4位数字");
            return false;
        }
//        if (!mAgreeCb.isChecked()) {
//            LogUtil.showToast(this, "请同意快捷支付协议");
//            return false;
//        }
        return true;
    }

    private void btnClick() {
        //需要选择支行
        if("1".equals(needbranch)&&!isParamsOK()){
               return;
        }
        if (!mAgreeCb.isChecked()) {
            LogUtil.showToast(this, "请同意快捷支付协议");
            return ;
        }
        if (isBranchSelected||"0".equals(needbranch)) {
            Bindcard();
        } else {
            LogUtil.showToast(DebitCardInfoAddActivity.this,
                    getResources().getString(R.string.select_the_account_where_the_branch));
        }
        //手机号和验证码字段取消
//        if (checkInput()) {
//            final String mac = mMacEt.getText().toString().trim();
//            String mobileNo = QtpayAppData.getInstance(this).getMobileNo();
//            String phoneNo = mMobileEt.getText().toString().trim();
//            if (mobileNo.equals(phoneNo)) {
//                RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(DebitCardInfoAddActivity.this, new ConFirmDialogListener() {
//
//                    @Override
//                    public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
//                        ryxSimpleConfirmDialog.dismiss();
//                        checkSmsMac(mac);//校验验证码
//                    }
//
//                    @Override
//                    public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
//                        ryxSimpleConfirmDialog.dismiss();
//                    }
//                });
//                ryxSimpleConfirmDialog.show();
//                ryxSimpleConfirmDialog.setContent("手机号为银行卡预留手机号，是否确认？");
//            } else {
//                //校验验证码
//                checkSmsMac(mac);
//            }
//        }
    }

//    private void checkSmsMac(String smsMac) {
//        qtpayApplication.setValue("CheckSMSCode.Req");
//        Param phoneParam = new Param("bankTel");
//        String phoneNumber = mMobileEt.getText().toString();
//        phoneParam.setValue(phoneNumber);
//        qtpayAttributeList.add(qtpayApplication);
//        qtpayParameterList.add(phoneParam);
//        qtpayParameterList.add(new Param("smsCode", smsMac));
//        httpsPost("checkSmsMac", new XmlCallback() {
//            @Override
//            public void onTradeSuccess(RyxPayResult payResult) {
//                if (isBranchSelected||"0".equals(needbranch)) {
//                    Bindcard();
//                } else {
//                    LogUtil.showToast(DebitCardInfoAddActivity.this,
//                            getResources().getString(R.string.select_the_account_where_the_branch));
//                }
//            }
//        });
//    }

    private void Bindcard() {
        initQtPatParams();
        qtpayApplication = new Param("application", "BankCardBind.Req");
        Param qtpayBindType = new Param("bindType", "01");
        Param qtpayhissuers = new Param("hissuers", hissuers);
        Param qtpayAccountNo = new Param("accountNo");
        Param qtpayUsertype = new Param("userType", usertype);
//        Param qtPayMobileNumber = new Param("phoneNum", numberString);
        qtpayBankId = new Param("bankId");
        if("0".equals(needbranch)){
            qtpayBankId.setValue(branchid2);
        }else{
            qtpayBankId.setValue(branchId);
        }
        qtpayAccountNo.setValue(bankCardInfo.getAccountNo().trim().replace(" ", ""));
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBindType);
//        qtpayParameterList.add(qtPayMobileNumber);
        qtpayParameterList.add(qtpayBankId);
        qtpayParameterList.add(qtpayhissuers);
        qtpayParameterList.add(qtpayAccountNo);
        qtpayParameterList.add(qtpayUsertype);
        qtpayParameterList.add(new Param("cardType", "01"));
        httpsPost("BankCardBind", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                setResult(RyxAppconfig.CLOSE_ALL);
                finish();
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

    @Click(R.id.tv_pay_agree)
    public void agreePay() {
        //跳转快捷支付开通协议
        Intent intent = new Intent(DebitCardInfoAddActivity.this, HtmlMessageActivity_.class);
        intent.putExtra("title", "快捷支付开通协议");
        intent.putExtra("ccurl", "");
        intent.putExtra("urlkey", RyxAppconfig.Notes_FastAgreement);
        startActivity(intent);
    }

    @Click(R.id.btn_back)
    public void closeWindow() {
        finish();
    }


    public void setmSendMacTv() {
        numberString = mMobileEt.getText().toString().trim();
        if (TextUtils.isEmpty(numberString)) {
            LogUtil.showToast(this, "请输入手机号码");
            return;
        }
        if (numberString.length() != 11) {
            LogUtil.showToast(this, "手机号码为11位");
            return;
        }
        //发送验证码
        mTimer = null;
        mTimer = new Timer();
        getMobileMac();
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
    }

    private void getMobileMac() {
        qtpayApplication.setValue("SendAdvancedVipSMS.Req");
        Param phoneParam = new Param("bankTel");
        phoneParam.setValue(mMobileEt.getText().toString().trim());
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(phoneParam);
        httpsPost("getMobileMac", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(DebitCardInfoAddActivity.this,
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

    Handler timeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what > 0) {
                mSendMacTv.setTextColor(ContextCompat.getColor(DebitCardInfoAddActivity.this, R.color.text_a));
                mSendMacTv.setText(getResources().getString(R.string.resend) + "(" + msg.what + ")");
                mSendMacTv.setClickable(false);
            } else {
                mTimer.cancel();
                mSendMacTv.setText(getResources().getString(R.string.resend_verification_code));
                mSendMacTv.setClickable(true);
                mSendMacTv.setTextColor(ContextCompat.getColor(DebitCardInfoAddActivity.this, R.color.colorPrimary));
            }
        }
    };
}
