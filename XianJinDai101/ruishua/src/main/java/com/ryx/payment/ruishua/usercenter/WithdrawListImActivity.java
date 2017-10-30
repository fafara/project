package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.qtpay.qtjni.QtPayEncode;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.BankLisAdapter;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.BankCardAddActivity_;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.adapter.WithDrawCardListAdapter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.quickadapter.inter.NoDoubleItemClickListener;
import com.ryx.ryxkeylib.listener.EditPwdListener;
import com.ryx.ryxkeylib.service.CustomKeyBoardService;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
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

import static android.media.CamcorderProfile.get;

/**
 * 资金结算
 *
 * @author muxin
 * @time 2016-08-03 13:08
 */
@EActivity(R.layout.activity_withdraw_list_im)
public class WithdrawListImActivity extends BaseActivity {

    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tilerightImg)
    ImageView mAddCardImg;
    @ViewById(R.id.lv_show_bank)
    SwipeMenuListView mShowBankList;
    @ViewById(R.id.et_withdraw_balance)
    EditText mWithDrawMoney;
    @ViewById(R.id.et_withdraw_pwd)
    EditText mWithDrawPwd;
    @ViewById(R.id.ll_withdraw_balance)
    LinearLayout mWithDrawMoneyLine;
    @ViewById(R.id.ll_withdraw_pwd)
    LinearLayout mWithDrawPwdLine;
    //快速到账和普通操作
    @ViewById(R.id.ll_action_normal)
    LinearLayout mNormalActionLL;
    @ViewById(R.id.ll_action_immediate)
    LinearLayout mImmediateActionLL;
    @ViewById(R.id.tv_normal)
    TextView mNormalActionTv;
    @ViewById(R.id.tv_normal_tip)
    TextView mNormalActionTipTv;
    @ViewById(R.id.tv_immediate)
    TextView mImmediateActionTv;
    @ViewById(R.id.tv_immediate_tip)
    TextView mImmediateActionTipTv;
    @ViewById(R.id.btn_true)
    Button mCommitBtn;
    @ViewById(R.id.tv_tip_balance)
    TextView mWithDrawTips;
    @ViewById(R.id.et_mac)
    EditText mMobileMac;
    @ViewById(R.id.ll_withdraw_mac)
    LinearLayout mWithDrawMacLine;
    @ViewById(R.id.tv_again_mac)
    TextView mMacSendTv;
    @ViewById(R.id.tv_withdraw_all)
    TextView mWithDrawAll;
    @ViewById(R.id.warmprompt_tv)
    TextView warmprompt_tv;

    @Bean
    WithDrawCardListAdapter cardListAdapter;
    private ArrayList<BankCardInfo> selectBankList = new ArrayList<>();
    private ArrayList<BankCardInfo> showBankList = new ArrayList<>();
    private String flag = "0";//默认0普通到账
    private String accountNo;
    private int selectBankIndex=0;
    private int ADDCARDINFO = 1;
//    private BankCardInfo bankCardInfo;
    private String availableAmt;
    private String accountday;
    private Param qtpayAcctType;
    private Timer timer = null;
    private Param qtpayCardIdx;
    private BankCardInfo mSelectBankCardInfo;
    private Param qtpayCardTag;
    private Param qtpayCashAmt;
    private Param qtpayPassword;
    private Param qtpayMobileMac;
    private Param qtpayCashType;
    Handler timeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what > 0) {
                mMacSendTv.setTextColor(ContextCompat.getColor(WithdrawListImActivity.this, R.color.text_a));
                mMacSendTv.setText(getResources().getString(R.string.resend)
                        + "(" + msg.what + ")");
                mMacSendTv.setClickable(false);
            } else {
                timer.cancel();
                mMacSendTv.setText(getResources().getString(
                        R.string.resend_verification_code));
                mMacSendTv.setClickable(true);
                mMacSendTv.setTextColor(ContextCompat.getColor(WithdrawListImActivity.this, R.color.colorPrimary));
            }
        }
    };

    @AfterViews
    public void initViews() {
        setTitleLayout("资金结算", true, true);
        mAddCardImg.setImageResource(R.drawable.ic_add_icon_bg);
        initQtPatParams();
        initBankView();
        iniRyxKeyWord();
        initData();
        doQueryAccountBalance();
    }

    private void initBankView() {
        mShowBankList.setOnItemClickListener(new NoDoubleItemClickListener() {
            @Override
            protected void onNoDoubleItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                showBankList();
            }
        });
    }

    private void initData() {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("温馨提示:提款成功后第二个工作日到结算银行卡中,请及时留意资金动态。");
        if(RyxAppdata.getInstance(WithdrawListImActivity.this).withdrawListImActivityfeeMessageIsshow()){
           String shareFreeMsg= PreferenceUtil.getInstance(WithdrawListImActivity.this).getString(RyxAppconfig.Notes_WithdrawFeeMsg, "");
           String feeMsg="<font color=\"#ed9f2b\">"+shareFreeMsg+"</font>";
            stringBuffer.append(feeMsg);
            LogUtil.showLog("stringBuffer=="+stringBuffer.toString());
            warmprompt_tv.setText(Html.fromHtml(stringBuffer.toString()));
        }else{
            warmprompt_tv.setText(stringBuffer.toString());
        }
        ArrayList<BankCardInfo> bankList = (ArrayList<BankCardInfo>) getIntent().getExtras().getSerializable("bankcardlist");
        if (bankList != null && bankList.size() > 0) {
            selectBankList.addAll(bankList);
            for (int i=0;i<selectBankList.size();i++) {
                BankCardInfo   bankCardInfo=  selectBankList.get(i);
                String cardstatus=bankCardInfo.getCardstatus();
                if (bankCardInfo.getFlagInfo().equals("1")&&!"1".equals(cardstatus)) {
                    showBankList.add(bankCardInfo);//显示默认卡并且当前银行未处于维护状态
                    accountNo = bankCardInfo.getAccountNo();
                    selectBankIndex=i;
                }
            }
            if (showBankList.isEmpty()) {

                for(int j = 0; j < selectBankList.size(); j++){
                    String cardstatusj=selectBankList.get(j).getCardstatus();
                    if(!"1".equals(cardstatusj)){
                        showBankList.add(selectBankList.get(j));//显示没处于维护的支持的结算卡就行
                        accountNo = selectBankList.get(j).getAccountNo();
                        selectBankIndex=j;
                        break;
                    }
                }
                if(showBankList.isEmpty()){
                    //如果当前还是空的则进行选中第一个即可
                    showBankList.add(selectBankList.get(0));
                    accountNo = selectBankList.get(0).getAccountNo();
                    selectBankIndex=0;
                }
            }
            cardListAdapter.setList(showBankList);
            mShowBankList.setAdapter(cardListAdapter);
        }
        mWithDrawMoney.requestFocus();
        initImmediateChecked();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void resetBankListView(int i) {
        selectBankIndex=i;
        accountNo = selectBankList.get(i).getAccountNo();
        RyxAppdata.getInstance(WithdrawListImActivity.this).saveRuishuaparam(RyxAppdata.getInstance(
                WithdrawListImActivity.this).getMobileNo(), accountNo, selectBankList.get(i).getCardIdx());
        showBankList.clear();
        showBankList.add(selectBankList.get(i));
        cardListAdapter.setList(showBankList);
        cardListAdapter.notifyDataSetChanged();
    }

    private void initImmediateChecked() {
        mImmediateActionLL.setBackgroundResource(com.ryx.ryxcredit.R.drawable.c_repayment_type_press);
        mNormalActionLL.setBackgroundResource(com.ryx.ryxcredit.R.drawable.c_repayment_type_unpress);
        mImmediateActionTv.setTextColor(ContextCompat.getColor(this, R.color.white));
        mImmediateActionTipTv.setTextColor(ContextCompat.getColor(this, R.color.white));
        mNormalActionTv.setTextColor(ContextCompat.getColor(this, R.color.threeblack));
        mNormalActionTipTv.setTextColor(ContextCompat.getColor(this, R.color.threeblack));
    }

    private void initNormalChecked() {
        mImmediateActionLL.setBackgroundResource(com.ryx.ryxcredit.R.drawable.c_repayment_type_unpress);
        mNormalActionLL.setBackgroundResource(com.ryx.ryxcredit.R.drawable.c_repayment_type_press);
        mImmediateActionTv.setTextColor(ContextCompat.getColor(this, R.color.threeblack));
        mImmediateActionTipTv.setTextColor(ContextCompat.getColor(this, R.color.threeblack));
        mNormalActionTv.setTextColor(ContextCompat.getColor(this, R.color.white));
        mNormalActionTipTv.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayAcctType = new Param("acctType", "00");
    }

    @Click(R.id.tileleftImg)
    public void closeWindow() {
        finish();
    }

    public void showBankList() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(WithdrawListImActivity.this, R.style.Material_App_BottomSheetDialog);
        View bottomView = LayoutInflater.from(WithdrawListImActivity.this).inflate(R.layout.impay_bottomsheet, null);
        final ListView bottomListView = (ListView) bottomView.findViewById(R.id.im_pay_bottomListViewid);
        final BankLisAdapter bankLisAdapter = new BankLisAdapter(WithdrawListImActivity.this, selectBankList, accountNo);
        bottomListView.setAdapter(bankLisAdapter);
        ImageView imageView = (ImageView) bottomView.findViewById(R.id.imgview_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        bottomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cardstatus=selectBankList.get(position).getCardstatus();
                String cardnote=selectBankList.get(position).getCardnote();
                if("1".equals(cardstatus)){
                    LogUtil.showToast(WithdrawListImActivity.this,cardnote+"");
                    return;
                }
                resetBankListView(position);
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.contentView(bottomView).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        getBankCardList2();
        getNewBankCardList();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Click(R.id.tv_again_mac)
    public void sendMac() {
        if (QtpayAppData.getInstance(WithdrawListImActivity.this).isLogin()) {
            getMobileMac();
        } else {
            LogUtil.showToast(this, "请先登录");
        }
    }

    @Click(R.id.ll_action_immediate)
    public void immediateChecked() {
        initImmediateChecked();
        flag = "1";
        String s = String.format(getResources().getString(R.string.tv_withdraw_tips), availableAmt,
                MoneyEncoder.decodeFormat(accountday));
        mWithDrawTips.setText(Html.fromHtml(s));
    }

    @Click(R.id.ll_action_normal)
    public void normalChecked() {
        initNormalChecked();
        flag = "0";
        String s = String.format(getResources().getString(R.string.tv_withdraw_tips2), availableAmt);
        mWithDrawTips.setText(Html.fromHtml(s));
    }

    @Click(R.id.btn_true)
    public void withDrawCommit() {
        if (checkValue()) {
            doWithDraw();
        }
    }

    @Click(R.id.tv_withdraw_all)
    public void setWithDrawAll() {
        mWithDrawMoney.setText(availableAmt);
        mWithDrawPwd.requestFocus();
    }

    private void iniRyxKeyWord() {
        CustomKeyBoardService customKeyBoardService = CustomKeyBoardService.registerKeyBoardForEdit(WithdrawListImActivity.this, true, mWithDrawPwd, new EditPwdListener() {
            @Override
            public void getPwdVal(String realVal, String disVal) {
//            textView1.setText("密码:"+realVal);
                mWithDrawPwd.setText(realVal);
            }

            @Override
            public void getPwdDisVal(String disVal, int count) {
                mWithDrawPwd.setText(disVal);
            }

            @Override
            public void pwdViewOkbtnLisener() {

            }

        });
        customKeyBoardService.setEditMaxLenth(20);
    }

    @Click(R.id.tilerightImg)
    public void addCard() {
        Intent addIntent;
        LogUtil.showLog("UserType = " + QtpayAppData.getInstance(WithdrawListImActivity.this).getUserType());
           //改为旧的界面
//            addIntent = new Intent(WithdrawListImActivity.this, BindDebitCardActivity_.class);
           //新的卡管理界面
            addIntent = new Intent(WithdrawListImActivity.this, BankCardAddActivity_.class);
            startActivityForResult(addIntent, ADDCARDINFO);
    }

    @FocusChange({R.id.et_withdraw_balance, R.id.et_withdraw_pwd, R.id.et_mac})
    public void focusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.et_withdraw_balance:
                mWithDrawMoneyLine.setBackgroundResource(R.color.login_edt_getfocus);
                mWithDrawPwdLine.setBackgroundResource(R.color.login_edt_lostfocus);
                mWithDrawMacLine.setBackgroundResource(R.color.login_edt_lostfocus);
                break;
            case R.id.et_withdraw_pwd:
                mWithDrawMoneyLine.setBackgroundResource(R.color.login_edt_lostfocus);
                mWithDrawPwdLine.setBackgroundResource(R.color.login_edt_getfocus);
                mWithDrawMacLine.setBackgroundResource(R.color.login_edt_lostfocus);
                break;
            case R.id.et_mac:
                mWithDrawMoneyLine.setBackgroundResource(R.color.login_edt_lostfocus);
                mWithDrawPwdLine.setBackgroundResource(R.color.login_edt_lostfocus);
                mWithDrawMacLine.setBackgroundResource(R.color.login_edt_getfocus);
                break;
        }
    }

    private void startCountdown() {
        TimerTask task = new TimerTask() {
            int shyusecond = 60;

            public void run() {
                Message msg = new Message();
                msg.what = shyusecond--;
                timeHandler.sendMessage(msg);
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    /**
     * 查询账户余额
     */
    public void doQueryAccountBalance() {
        qtpayApplication = new Param("application", "JFPalAcctEnquiry.Req");
        qtpayAcctType = new Param("acctType", "00");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayAcctType);
        httpsPost("doQueryBalance", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                availableAmt = MoneyEncoder.decodeFormat(payResult
                        .getAvailableAmt());
                LogUtil.showLog("muxin", "avaliableAmt:" + availableAmt);
                doQueryUserInfo();
            }
        });
    }

    /**
     * 查询用户信息
     */
    public void doQueryUserInfo() {
        qtpayApplication.setValue("UserInfoQuery.Req");
        Param qtpayTransType = new Param("transType", "00");
        qtpayMobileNO.setValue(QtpayAppData.getInstance(WithdrawListImActivity.this).getMobileNo());
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayTransType);
        httpsPost("doQueryUserInfo", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeJson(payResult.getData());
                updateUI();
            }
        });
    }

    private void updateUI() {
        String s = String.format(getResources().getString(R.string.tv_withdraw_tips), availableAmt);
        mWithDrawTips.setVisibility(View.VISIBLE);
        mWithDrawTips.setText(Html.fromHtml(s));
    }

    public void analyzeJson(String jsonstring) {
        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                        "result").getString("resultCode"))) {
                    JSONObject userinfo = jsonObj.getJSONObject("resultBean");
                    accountday = JsonUtil.getValueFromJSONObject(userinfo,
                            "accountday");
                    LogUtil.showLog("muxin", "accountday:" + accountday);
                }
            } else {
                accountday = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用卡管理中获取卡列表(new)
     */
    private void getNewBankCardList(){
        qtpayApplication.setValue("BindCardList.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("cardType","10"));
        httpsPost("BindCardListTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                newinitBankListData(payResult.getData());
            }
        });
    }
//    /**
//     * 获取银行卡列表(old)
//     */
//    private void getBankCardList2() {
//        Param qtpayCardIdx = new Param("cardIdx");
//        Param qtpayBindType = new Param("bindType", "01");
//        Param qtpayCardNum = new Param("cardNum", "05");
//        qtpayCardIdx.setValue("00");
//        qtpayApplication.setValue("GetBankCardList2.Req");
//        qtpayAttributeList.add(qtpayApplication);
//        qtpayParameterList.add(qtpayBindType);
//        qtpayParameterList.add(qtpayCardIdx);
//        qtpayParameterList.add(qtpayCardNum);
//        httpsPost("getBankCardList2Tag", new XmlCallback() {
//            @Override
//            public void onTradeSuccess(RyxPayResult payResult) {
//                initBankListData(payResult.getData());
//            }
//        });
//    }

    /**
     * 初始化银行卡列表(new)
     * @param banklistJson
     */
    private void newinitBankListData(String banklistJson){
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            if ("0000".equals(jsonObj.getString("code"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONObject("result").getJSONArray("cardlist");
                selectBankList.clear();
                for (int i = 0; i < banks.length(); i++) {
                    BankCardInfo   bankCardInfo = new BankCardInfo();
                    bankCardInfo.setCardIdx(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardidx"));
                    bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankid"));
                    bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardno"));
                    bankCardInfo.setBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankname"));
                    bankCardInfo.setQuick(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "quick"));
                    bankCardInfo.setDaikou(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "msdk"));
                    bankCardInfo.setDaifustatus(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "daifustatus"));
                    bankCardInfo.setFlagInfo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "flaginfo"));//1为默认结算卡
                    bankCardInfo.setCardtype(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardtype"));
                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankName"));
                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "customername"));
                    bankCardInfo.setCardstatus(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardstatus"));
                    bankCardInfo.setCardnote(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardnote"));
                    selectBankList.add(bankCardInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        boolean ishaveDefault = false;
        if(selectBankList.size()==1){
            resetBankListView(0);
        }else{
            for (int i = 0; i < selectBankList.size(); i++) {
                String flagInfo = selectBankList.get(i).getFlagInfo();
                String cardstatus=selectBankList.get(i).getCardstatus();
                if ("1".equals(flagInfo)&&!"1".equals(cardstatus)) {
                    resetBankListView(i);
                    ishaveDefault = true;
                    break;
                }
            }
            if (!ishaveDefault) {
                boolean isHaveStatusOk = false;
                for (int j = 0; j < selectBankList.size(); j++) {
                    String cardstatusj = selectBankList.get(j).getCardstatus();
                    if (!"1".equals(cardstatusj)) {
                        isHaveStatusOk = true;
                        resetBankListView(j);
                        break;
                    }
                }
                if (!isHaveStatusOk) {
                    //当前用户所有绑定卡中没有一个是正常可以交易的都处于维护状态
                    resetBankListView(0);
                }
            }}
    }
//    /**
//     * 初始化银行卡列表(old)
//     */
//    public void initBankListData(String banklistJson) {
//        try {
//            JSONObject jsonObj = new JSONObject(banklistJson);
//            if ("0000".equals(jsonObj.getJSONObject("result").getString("resultCode"))) {
//                // 解析银行卡信息
//                JSONArray banks = jsonObj.getJSONArray("resultBean");
//                selectBankList.clear();
//                for (int i = 0; i < banks.length(); i++) {
//                    bankCardInfo = new BankCardInfo();
//                    bankCardInfo.setBankCity(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "bankCity"));
//                    bankCardInfo.setRemark(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i),
//                            "remark"));
//                    bankCardInfo.setBankProvinceId(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "bankProvinceId"));
//                    bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "accountNo"));
//                    bankCardInfo.setBankName(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "bankName"));
//                    bankCardInfo.setBankProvince(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "bankProvince"));
//                    bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i),
//                            "bankId"));
//                    bankCardInfo.setFlagInfo(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "flagInfo"));
//                    bankCardInfo.setBankCityId(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "bankCityId"));
//                    bankCardInfo.setCardIdx(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "cardIdx"));
//                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i),
//                            "name"));
//                    bankCardInfo.setBranchBankId(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "branchBankId"));
//                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(
//                            banks.getJSONObject(i), "branchBankName"));
//                    selectBankList.add(bankCardInfo);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        resetBankListView(selectId);
//    }

    /**
     * 提款获取验证码
     */
    public void getMobileMac() {
        qtpayApplication.setValue("GetMobileMac.Req");
        qtpayAttributeList.add(qtpayApplication);
        Param qtpayAppType = new Param("appType", "JFPalCash");
        Param qtpayOrderId = new Param("orderId", "");
        qtpayParameterList.add(qtpayAppType);
        qtpayParameterList.add(qtpayOrderId);
        qtpayToken.setValue("0002");

        httpsPost("getMobileMac", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                timer = new Timer();
                startCountdown();
                LogUtil.showToast(WithdrawListImActivity.this, getString(R.string.sms_has_been_issued_please_note_that_check));
            }
        });
    }

    /**
     * 输入验证
     *
     * @return
     */
    public boolean checkValue() {
        //提款金额验证
        String money = mWithDrawMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            LogUtil.showToast(this, "提款金额不能为空");
            return false;
        }

        mWithDrawMoney.setText(MoneyEncoder.EncodeFormat(mWithDrawMoney.getText().toString().trim()));
        if (mWithDrawMoney.getText().toString().trim().equals("￥0.00")) {
            LogUtil.showToast(this, "提款金额必须大于零");
            return false;
        }
        String currentText=mWithDrawMoney.getText().toString().trim().replace(",", "").replace("￥", "");
        try {
            Double.parseDouble(currentText);
        }catch (Exception e){
            LogUtil.showToast(this, "提款金额输入有误");
            return false;
        }

        int indexOf=mWithDrawMoney.getText().toString().trim().replace(",", "").replace("￥", "").indexOf(".");
        if (indexOf>0&&mWithDrawMoney.getText().toString().trim().replace(",", "").replace("￥", "")
                .length() -
                indexOf > 3) {
            LogUtil.showToast(this, "提款金额单位过小");
            return false;
        }
        if (mWithDrawMoney.getText().toString().trim().replace(",", "").length() > 9) {
            LogUtil.showToast(this, "提款金额超限");
            return false;
        }
        try {
            if (MoneyEncoder.encodeToPost(mWithDrawMoney.getText().toString().trim())
                    .compareTo(
                            MoneyEncoder.encodeToPost(availableAmt)) > 0) {
                LogUtil.showToast(this, "提款金额超出可提现余额");
                return false;
            }
        }catch (Exception e){
            LogUtil.showToast(this, "可提款余额信息有误!");
            return false;
        }

        if (TextUtils.isEmpty(flag)) {
            LogUtil.showToast(this, "请选择提款方式");
            return false;
        }
        //密码验证
        String pwd = mWithDrawPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            LogUtil.showToast(this, "请输入支付密码");
            return false;
        }
        if (pwd.length() < 6) {
            LogUtil.showToast(this, "支付密码不符合要求");
            return false;
        }
//        //mac
//        String mac = mMobileMac.getText().toString().trim();
//        if (TextUtils.isEmpty(mac)) {
//            LogUtil.showToast(this, "请输入短信验证码");
//            return false;
//        }
//        if (mac.length() != 4) {
//            LogUtil.showToast(this, "短信验证码为四位数字");
//            return false;
//        }
        return true;
    }

    /**
     * 提款
     */
    private void doWithDraw() {
        if (selectBankList != null) {
            mSelectBankCardInfo = selectBankList.get(selectBankIndex);
            String  currentCardstatus=    mSelectBankCardInfo.getCardstatus();
            String  cardnote=    mSelectBankCardInfo.getCardnote();
            if("1".equals(currentCardstatus)){
                LogUtil.showToast(WithdrawListImActivity.this, cardnote);
                return;
            }
            qtpayApplication.setValue("JFPalCash.Req");
            qtpayCardIdx = new Param("cardIdx", mSelectBankCardInfo.getCardIdx());
            qtpayCardTag = new Param("cardTag", mSelectBankCardInfo.getAccountNo()
                    .substring(mSelectBankCardInfo.getAccountNo().length() - 4,
                            mSelectBankCardInfo.getAccountNo().length()));

            qtpayCashAmt = new Param("cashAmt", MoneyEncoder.encodeToPost(mWithDrawMoney.getText().toString().trim()));
            qtpayPassword = new Param("password",
                    QtPayEncode.encryptUserPwd(mWithDrawPwd.getText().toString(),
                            QtpayAppData.getInstance(WithdrawListImActivity.this)
                                    .getPhone(), RyxAppconfig.DEBUG));
            qtpayMobileMac = new Param("mobileMac", "0");
            qtpayCashType = new Param("cashType", flag);//1 及时取 0普通
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(qtpayCardIdx);
            qtpayParameterList.add(qtpayCardTag);
            qtpayParameterList.add(qtpayCashAmt);
            qtpayParameterList.add(qtpayPassword);
            qtpayParameterList.add(qtpayMobileMac);
            qtpayParameterList.add(qtpayCashType);

            httpsPost("doWithDraw", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    LogUtil.showToast(WithdrawListImActivity.this, payResult.getRespDesc());
                    qtpayApplication = new Param("application", "JFPalAcctEnquiry.Req");
                    qtpayAcctType = new Param("acctType", "00");
                    qtpayAttributeList.add(qtpayApplication);
                    qtpayParameterList.add(qtpayAcctType);
                    httpsPost("doQueryBalance", new XmlCallback() {
                        @Override
                        public void onTradeSuccess(RyxPayResult payResult) {
                            availableAmt = MoneyEncoder.decodeFormat(payResult
                                    .getAvailableAmt());
                            updateUI();
                        }
                    });
                }
            });
        }
    }
}
