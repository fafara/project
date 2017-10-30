package com.ryx.payment.ruishua.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.activity.MainFragmentActivity;
import com.ryx.payment.ruishua.activity.MessageScreenActivity_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.authenticate.AuthResultActivity_;
import com.ryx.payment.ruishua.authenticate.Authenticate_;
import com.ryx.payment.ruishua.authenticate.MerchantCredentialsUpload_;
import com.ryx.payment.ruishua.authenticate.UserAuthPhotoUploadActivity_;
import com.ryx.payment.ruishua.authenticate.UserInfoAddActivity_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.IdCardUploadAct_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.NewAuthResultAct_;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.BMIncomeAndExpenditureDetails2_;
import com.ryx.payment.ruishua.usercenter.ChooseCouponsActivity_;
import com.ryx.payment.ruishua.usercenter.DeViceListActivity_;
import com.ryx.payment.ruishua.usercenter.MyCouponActivity_;
import com.ryx.payment.ruishua.usercenter.QrcodePayActivity_;
import com.ryx.payment.ruishua.usercenter.WithdrawListImActivity_;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@EFragment(R.layout.frag_main_user)
public class UserFragment extends BaseFragment {

    private static UserFragment sUserFragment;
    @ViewById(R.id.rl_user_withdrawim)
    RelativeLayout user_withdrawim_rl;
    @ViewById(R.id.userinfo_rl)
    RelativeLayout userinfo_rl;
    @ViewById(R.id.tv_user_name)
    TextView mUserName;
    @ViewById(R.id.tv_user_phonenumber)
    TextView mPhoneNumber;
    @ViewById(R.id.tv_user_level)
    TextView mUserLevelImg;
    @ViewById(R.id.rl_user_message)
    RelativeLayout mMyMessageRl;
    @ViewById(R.id.tv_user_bg_msg_number)
    TextView mMessageCount;
    @ViewById(R.id.rl_user_transfer)
    RelativeLayout mTransferRl;
    @ViewById(R.id.rl_user_kefu)
    RelativeLayout mKeFuRl;
    @ViewById(R.id.rl_user_coupon)//我的优惠券
    RelativeLayout mCoupon;
    @ViewById(R.id.rl_userauthresult)//实名认证
    RelativeLayout rl_userauthresult;

    private Param qtpayAcctType;
    private BaseActivity mBaseActivity;
    private Param qtpayTransType;
    private String level;
    private String noticeCode = "0000";
    private String toastmsg = "";
    private String powermsg = "-1";// 实名认证有关

    @ViewById
    TextView tv_balance,tv_drysk;
    @ViewById
    LinearLayout dayTradeAllayout;

    @ViewById
    TextView tv_userinfomsg;

    @ViewById
    ImageView iv_user_photo,iv_dev_use,iv_user_mymessage,iv_userauthresult,iv_user_coupon,iv_user_code,iv_user_kefu,iv_user_transfer,iv_user_withdrawim;
    private ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();// 已经绑定银行卡列表
    BankCardInfo bankCardInfo = null;
    private FragmentListener mListener;
    @AfterInject
    public void create() {
        sUserFragment = this;
    }

    @AfterViews
    public void initViews() {
        mBaseActivity = super.getBaseActivity();
        initQtPatParams();
        refreshData();
    }

    /**
     * MainFrag会调用执行数据刷新操作
     */
    @Override
    public void refreshData(){
        super.refreshData();
        bindData();
        iniStyle();
        //查询用户等级
        doQueryUserLevel();
    }
    /**
     * 瑞和宝差异化风格区分
     */
    private void iniStyle() {
        //1,蓝色主题,2灰色主题
     int styleTag=   RyxAppdata.getInstance(getContext()).getCurrentBranchMainStyleTag();
        if(2==styleTag){
            iv_user_photo.setImageResource(R.drawable.icon_user_photo_rhb);
            iv_dev_use.setImageResource(R.drawable.icon_user_device_rhb);
            iv_user_mymessage.setImageResource(R.drawable.icon_user_message_rhb);

            iv_user_coupon.setImageResource(R.drawable.setting_coupon_rhb);
            iv_user_code.setImageResource(R.drawable.setting_mycode_rhb);
            iv_user_kefu.setImageResource(R.drawable.icon_user_kefu_rhb);
            iv_user_transfer.setImageResource(R.drawable.icon_user_transfer_rhb);
            iv_user_withdrawim.setImageResource(R.drawable.icon_skin_withdrawim_blue_rhb);
//            iv_dev_use.setImageResource();
        }

    }

    public UserFragment getInstance() {
        return sUserFragment;
    }

    private void initQtPatParams() {
        mBaseActivity.initQtPatParams();
        qtpayAcctType = new Param("acctType", "00");
        mBaseActivity.qtpayApplication = new Param("application");
        qtpayTransType = new Param("transType", "00");
        mBaseActivity.qtpayMobileNO.setValue(QtpayAppData.getInstance(getActivity()).getMobileNo());
    }

    private void bindData() {
        // case 0: "未实名");
        // case 1:
        // case 5: "未认证");
        // case 2: "认证中");
        // case 4: "认证失败");
        // case 3: "已认证");
        if (QtpayAppData.getInstance(getActivity().getApplicationContext()).getAuthenFlag() == 0) {
            //未实名
            mUserName.setText(RyxAppdata.getInstance(getActivity()).getCurrentBranchName()+"用户");
            mUserLevelImg.setVisibility(View.GONE);
        } else {
            mUserName.setText(QtpayAppData.getInstance(getActivity().getApplicationContext())
                    .getRealName());
        }
        if (QtpayAppData.getInstance(getActivity().getApplicationContext()).getAuthenFlag() == 3) {
            tv_userinfomsg.setText("已认证");
        }else {
            tv_userinfomsg.setText("未认证");
        }
        mPhoneNumber.setText(StringUnit.phoneJiaMi(QtpayAppData.getInstance(getActivity().getApplicationContext())
                .getMobileNo()));
    }



    @Click(R.id.rl_user_message)
    public void setUserMessage(View view) {
        disabledTimerView(view);
        //我的消息
        startActivity(new Intent(mBaseActivity, MessageScreenActivity_.class));
    }
    @Click(R.id.rl_user_coupon)
    public void setUserCoupon(View view) {
        disabledTimerView(view);
        if(RyxAppconfig.isSuportMember){
            //我的优惠券
            startActivity(new Intent(mBaseActivity, ChooseCouponsActivity_.class));
        }else{
            startActivity(new Intent(mBaseActivity, MyCouponActivity_.class));
        }
    }
    @Click(R.id.rl_dev_use)
    public void setDevUse(View view)
    {
        disabledTimerView(view);
        Intent swiperIntent=new Intent(mBaseActivity, DeViceListActivity_.class);
        swiperIntent.putExtra("flag","banding");
        startActivity(swiperIntent);
    }


@Click(R.id.rl_user_code)
public void setuserCodeClick(View view){
    disabledTimerView(view);
    int flag = QtpayAppData.getInstance(getActivity().getApplicationContext()).getAuthenFlag();
    if (flag != 3) {
        showAuthDialog();
        return ;
    }
    startActivity(new Intent(mBaseActivity, QrcodePayActivity_.class));
}

    /**
     *
     * 实名认证布局
     */
    @Click(R.id.rl_userauthresult)
    public void setUserauthresult(View view){
        disabledTimerView(view);
        gotoRealName(null);
    }
    @Click(R.id.rl_user_transfer)
    public void setUserTransfer(View view) {
        disabledTimerView(view);
        //跳转交易明细
        Intent intent = new Intent(getActivity(), BMIncomeAndExpenditureDetails2_.class);
        startActivity(intent);
    }

    @Click(R.id.userinfo_rl)
    public void gotoRealName(View view) {
        if(view!=null){
            disabledTimerView(view);
        }
        Intent intent = null;
        int tag = QtpayAppData.getInstance(getActivity().getApplicationContext()).getAuthenFlag();
        // case 0: "未实名");
        // case 1:
        // case 5: "未认证");
        // case 2: "认证中");
        // case 4: "认证失败");
        // case 3: "已认证");
        //是否开启商户认证功能
        int isOpenMerchanttag = RyxAppdata.getInstance(getActivity().getApplicationContext()).getIsOpenMerchantFlag();
        switch (tag) {
            case 0:
                if(isOpenMerchanttag==1){
                    intent = new Intent(getActivity().getApplicationContext(), Authenticate_.class);
                }else{
                    if(RyxAppdata.getInstance(getActivity().getApplicationContext()).isAuthswitchOpen()){
                        //启用新的实名认证流程
                        intent = new Intent(getActivity().getApplicationContext(), IdCardUploadAct_.class);
                    }else{
                         intent = new Intent(getActivity().getApplicationContext(), UserInfoAddActivity_.class);
                    }
                }
                break;
            case 1:
            case 5:
                if (QtpayAppData
                        .getInstance(getActivity().getApplicationContext())
                        .getUserType().equals("00")) {
                    if(RyxAppdata.getInstance(getActivity().getApplicationContext()).isAuthswitchOpen()){
                        intent = new Intent(getActivity().getApplicationContext(), IdCardUploadAct_.class);
                    }else {
                        intent = new Intent(getActivity().getApplicationContext(),
                                UserAuthPhotoUploadActivity_.class);
                    }

                }else if(QtpayAppData
                        .getInstance(getActivity().getApplicationContext())
                        .getUserType().equals("01")){
                    intent = new Intent(getActivity().getApplicationContext(),
                            MerchantCredentialsUpload_.class);
                }
                break;
            case 2:
            case 3:
            case 4:
                if(RyxAppdata.getInstance(getActivity().getApplicationContext()).isAuthswitchOpen()){
                    intent = new Intent(getActivity().getApplicationContext(),
                            NewAuthResultAct_.class).putExtra("PowerMsg", powermsg);
                }else{
                    intent = new Intent(getActivity().getApplicationContext(),
                            AuthResultActivity_.class).putExtra("PowerMsg", powermsg);
                }
                break;
        }
        try {
            mListener.doDataRequest(intent);
        }catch (Exception e){
            LogUtil.showLog("e:==="+e.getLocalizedMessage());
            LogUtil.showToast(getActivity().getApplicationContext(),"当前用户状态有误!");
        }
    }


    @Click(R.id.rl_user_kefu)
    public void setKeFu() {
        //跳转系统拨号
        Uri uri = Uri.parse("tel:" + getResources().getString(R.string.service_phone));
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(it);
    }

 /*   @Click(R.id.rl_user_device)
    public void setUserDevice() {
        //跳转我的设备
//        Intent intent = new Intent(getActivity(), DeviceList_.class);
        Intent intent = new Intent(getActivity(), DeViceListActivity_.class);
        startActivity(intent);
    }*/
    @Click(R.id.rl_user_withdrawim)
    public void  setUser_withdrawim_rl(View view){
        disabledTimerView(view);
        getNewBankCardList();
    }
    /**
     * 调用新的卡管理中接口获取已绑定银行卡列表接口
     *
     */
    private void getNewBankCardList() {
        mBaseActivity.qtpayApplication.setValue("BindCardList.Req");
        mBaseActivity.qtpayAttributeList.add(mBaseActivity.qtpayApplication);
        mBaseActivity.qtpayParameterList.add(new Param("cardType", "10"));
        mBaseActivity.httpsPost("BindCardListTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                newinitBankListData(payResult.getData());
                final Bundle bundle = new Bundle();
                if (bankcardlist.isEmpty()) {
                    LogUtil.showToast(getActivity().getApplicationContext(), "您还未绑定有效结算卡,请先绑定有效结算卡!");
                    return;
                }
                bundle.putSerializable("bankcardlist", bankcardlist);
                Intent intent = new Intent(getActivity(), WithdrawListImActivity_.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });


    }

    /**
     * 初始化银行卡列表(new)
     *
     * @param banklistJson
     */
    private void newinitBankListData(String banklistJson) {
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            bankcardlist.clear();
            if ("0000".equals(jsonObj.getString("code"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONObject("result").getJSONArray("cardlist");
                for (int i = 0; i < banks.length(); i++) {
                    bankCardInfo = new BankCardInfo();
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
                    bankcardlist.add(bankCardInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    /**
//     * 当日收款
//     */
//    @Click(R.id.tv_user_zichan_number)
//    public void showShouKuan(){
//        if (!QtpayAppData.getInstance(getActivity().getApplication()).isLogin()) {
//            startActivity(new Intent(getActivity().getApplication(), LoginActivity_.class));
//            return;
//        }
//        if (mZiChanTextView.getText().toString().equals("*******")){
//            //查询当日收款
//            doQueryTradeAmt();
//        }else{
//            mZiChanTextView.setText("*******");
//        }
//    }


    //    /**
//     * 账户余额
//     */
//    @Click(R.id.tv_user_balance_number)
//    public void showBalance(){
//        if (!QtpayAppData.getInstance(getActivity().getApplication()).isLogin()) {
//            startActivity(new Intent(getActivity().getApplication(), LoginActivity_.class));
//            return;
//        }
//        if (mBalanceTextView.getText().toString().equals("*******")){
//            //查询钱包余额
//            doQueryAccountBalance();
//        }else{
//            mBalanceTextView.setText("*******");
//        }
//    }
    @Click(R.id.dayTradeAllayout)
    public void showdayTrade() {
        if (!QtpayAppData.getInstance(getActivity().getApplication()).isLogin()) {
            toAgainLogin(getActivity().getApplicationContext(),RyxAppconfig.TOLOGINACT);
            return;
        }
        if (tv_drysk.getText().toString().equals("*******")) {
            doQueryTradeAmt();
        } else {
            tv_drysk.setText("*******");
            tv_balance.setText("*******");
        }
    }

//    public void doExit() {
//        mBaseActivity.qtpayApplication = new Param("application");
//        mBaseActivity.qtpayApplication.setValue("UserLoginExit.Req");
//        mBaseActivity.qtpayAttributeList.add(mBaseActivity.qtpayApplication);
//        mBaseActivity.httpsPost(false, true, "UserLoginExit", new XmlCallback() {
//            @Override
//            public void onTradeSuccess(RyxPayResult payResult) {
//                QtpayAppData.getInstance(getActivity().getApplicationContext())
//                        .setLogin(false);
//                QtpayAppData.getInstance(getActivity().getApplicationContext())
//                        .setRealName("");
//                QtpayAppData.getInstance(getActivity().getApplicationContext())
//                        .setMobileNo("");
//                QtpayAppData.getInstance(getActivity().getApplicationContext())
//                        .setPhone("");
//                QtpayAppData.getInstance(getActivity().getApplicationContext()).setCustomerId("");
//                QtpayAppData.getInstance(getActivity().getApplicationContext()).setAuthenFlag(0);
//                QtpayAppData.getInstance(getActivity().getApplicationContext()).setCustomerName("");
//                QtpayAppData.getInstance(getActivity().getApplicationContext()).setToken("");
//                //清空接口AppKey集合
//                RyxAppdata.resultKeyList.clear();
//                getActivity().finish();
//                startActivity(new Intent(getActivity(), MainFragmentActivity_.class));
//            }
//        });
//    }

//    private void showExitDialog() {
//        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(getContext(), new ConFirmDialogListener() {
//
//            @Override
//            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
//                ryxSimpleConfirmDialog.dismiss();
//                doExit();
//            }
//
//            @Override
//            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
//                ryxSimpleConfirmDialog.dismiss();
//            }
//        });
//        ryxSimpleConfirmDialog.show();
//        ryxSimpleConfirmDialog.setContent("确认要安全退出吗？");
//    }

    @Override
    public void onResume() {
        if (QtpayAppData.getInstance(getActivity().getApplicationContext()).isLogin()) {
            setMsgNum();
            bindData();
        }
        super.onResume();
    }

    /**
     * 查询用户等级
     */
    private void doQueryUserLevel() {
        mBaseActivity.qtpayApplication = new Param("application", "UserInfoQuery.Req");
        mBaseActivity.qtpayAttributeList.clear();
        mBaseActivity.qtpayParameterList.clear();
        mBaseActivity.qtpayAttributeList.add(mBaseActivity.qtpayApplication);
        mBaseActivity.qtpayParameterList.add(qtpayTransType);
        mBaseActivity.httpsPost(true, false, "queryUserLevel", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                String jsonStr = payResult.getData();
                String vipLevel = analyzeJson(jsonStr);
                if (Integer.parseInt(vipLevel) - 1 < 6 && Integer.parseInt(vipLevel) - 1 > 0) {
                    String level = "V" + String.valueOf(Integer.parseInt(vipLevel) - 1);
                    SpannableString sp = new SpannableString(level);
                    //设置斜体
                    sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0, level.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    mUserLevelImg.setVisibility(View.VISIBLE);
                    mUserLevelImg.setText(sp);
                } else {
                    mUserLevelImg.setVisibility(View.GONE);
                }
                getAllMsg();
            }
        });
    }

    public void getAllMsg() {
        mBaseActivity.qtpayApplication = new Param("application");
        mBaseActivity.qtpayApplication.setValue("GetPublicNotice.Req");
        Param qtpayNoticeCode = new Param("noticeCode", noticeCode);
        mBaseActivity.qtpayAttributeList.add(mBaseActivity.qtpayApplication);
        mBaseActivity.qtpayParameterList.add(qtpayNoticeCode);
        mBaseActivity.qtpayParameterList.add(new Param("noticeType", "1"));
        mBaseActivity.qtpayParameterList.add(new Param("readFlag", "0"));
        mBaseActivity.qtpayParameterList.add(new Param("offset", "1"));
        mBaseActivity.httpsPost(false, true, "GetPublicNotice", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeNotices(payResult.getData());
            }
        });
    }

    //解析通知内容
    private void analyzeNotices(String noticeData) {
        if (noticeData != null) {
            try {
                JSONObject noticeObj = new JSONObject(noticeData);
                toastmsg = (String) noticeObj.getJSONObject("result").getString(
                        "message");
                if (RyxAppconfig.QTNET_SUCCESS.equals(noticeObj.getJSONObject(
                        "result").getString("resultCode"))) {
                    int unreadPersonalNoticeNumber = noticeObj.getInt("count");
                    PreferenceUtil.getInstance(mBaseActivity).saveInt(
                            "unreadNoticePersonNumber_"
                                    + RyxAppdata.getInstance(mBaseActivity)
                                    .getMobileNo(), unreadPersonalNoticeNumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                noticeData = null;
            }
        }
        setMsgNum();
    }

    private void setMsgNum() {
        int publicunReadNum = PreferenceUtil.getInstance(mBaseActivity)
                .getInt("unreadNoticeNumber_" + RyxAppdata.getInstance(mBaseActivity)
                        .getMobileNo(), 0);
        int personalunReadNum = PreferenceUtil.getInstance(mBaseActivity).getInt("unreadNoticePersonNumber_" + RyxAppdata.getInstance(mBaseActivity)
                .getMobileNo(), 0);
        LogUtil.showLog("setMsgNumooooo", (publicunReadNum + personalunReadNum) + "--"
                + publicunReadNum + "----" + personalunReadNum);
        try {
            if ((publicunReadNum + personalunReadNum) > 0) {
                mMessageCount.setVisibility(View.VISIBLE);
                mMessageCount.setText((publicunReadNum + personalunReadNum) + "");
            } else {
                mMessageCount.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }
    }

    public String analyzeJson(String jsonstring) {
        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                        "result").getString("resultCode"))) {
                    JSONObject userinfo = jsonObj.getJSONObject("resultBean");
                    if(userinfo.has("authenFlag")){
                        RyxAppdata.getInstance(getActivity()).setAuthenFlag(
                                userinfo.getInt("authenFlag"));
                    }

                    if (userinfo.has("vipLevel")) {
                        level = userinfo.getString("vipLevel");
                        return level;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1";
    }

//    /**
//     * 钱包余额
//     */
//    private void doQueryAccountBalance() {
//        mBaseActivity.qtpayApplication.setValue("JFPalAcctEnquiry.Req");
//        mBaseActivity.qtpayAttributeList.add(mBaseActivity.qtpayApplication);
//        mBaseActivity.qtpayParameterList.add(qtpayAcctType);
//        mBaseActivity.httpsPost("queryAccountBalance", new XmlCallback() {
//            @Override
//            public void onTradeSuccess(RyxPayResult payResult) {
//                accountBalance = payResult.getAvailableAmt();
//                mBalanceTextView.setText(MoneyEncoder.decodeFormat(
//                        accountBalance));
//            }
//        });
//    }

    /**
     * 当日收款金额
     */
    private void doQueryTradeAmt() {
        mBaseActivity.qtpayApplication.setValue("dayTradeAmt.Req");
        mBaseActivity.qtpayAttributeList.add(mBaseActivity.qtpayApplication);
        mBaseActivity.qtpayParameterList.add(qtpayAcctType);
        mBaseActivity.httpsPost("queryTradeAmt", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
//                dayTradeAmt = payResult.getDayTradeAmt();
//                mZiChanTextView.setText("￥"
//                        + new java.text.DecimalFormat("#0.00").format(new BigDecimal(
//                        dayTradeAmt).setScale(2, BigDecimal.ROUND_DOWN)
//                        .doubleValue()));

                String payResultData = payResult.getData();
//  {"t1":{"monthsurplus":"2000.0","dayamt":"0.0","monthamt":"0.0","daysurplus":"2000.0"},
// "t0":{"monthsurplus":"49998.0","dayamt":"0.0","monthamt":"2.0","daysurplus":"49998.0"},
// "sf":{"monthsurplus":"2000.0","dayamt":"0.0","monthamt":"0.0","daysurplus":"2000.0"},
// "xe":{"monthsurplus":"1988.0","dayamt":"0.0","monthamt":"12.0","daysurplus":"1988.0"}}
                try {
                    if (payResultData != null && payResultData.length() > 0) {
                        JSONObject jsonObj = new JSONObject(payResultData);
                        JSONObject amountcontrolObject = jsonObj.getJSONObject("amountcontrol");
                        JSONObject t1Object = amountcontrolObject.getJSONObject("t1");
                        JSONObject sft1Object = amountcontrolObject.getJSONObject("sft1");
                        JSONObject t0Object = amountcontrolObject.getJSONObject("t0");
                        JSONObject xeObject = amountcontrolObject.getJSONObject("xe");//小额
                        JSONObject sfObject = amountcontrolObject.getJSONObject("sf");
//                            收款剩余额度:取值t1的daysurplus，、T0提现剩余额度0 :取值T0提现daysurplus、
                            String sfdayamtStr = sfObject.getString("dayamt");
//                            dayTradeAmt_content1.setText("￥"
//                                    + new java.text.DecimalFormat("#0.00").format(new BigDecimal(
//                                    Double.parseDouble(sfdayamtStr) / 100).setScale(2, BigDecimal.ROUND_DOWN)
//                                    .doubleValue()));
                        tv_drysk.setText(MoneyEncoder.decodeFormat(sfdayamtStr));
//                            String  sfdaysurplusStr=sfObject.getString("daysurplus");
//                            dayTradeAmt_content2.setText("￥"
//                                    + new java.text.DecimalFormat("#0.00").format(new BigDecimal(
//                                    Double.parseDouble(sfdaysurplusStr)/100).setScale(2, BigDecimal.ROUND_DOWN)
//                                    .doubleValue()));
                            JSONObject walletObject = jsonObj.getJSONObject("wallet");
                            String balanceStr = walletObject.getString("balance");
//                            dayTradeAmt_content3.setText("￥"
//                                    + new java.text.DecimalFormat("#0.00").format(new BigDecimal(
//                                    Double.parseDouble(balanceStr) / 100).setScale(2, BigDecimal.ROUND_DOWN)
//                                    .doubleValue()));
                        tv_balance.setText(MoneyEncoder.decodeFormat(balanceStr));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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

    public void send(int actiontype, RyxPayResult qtpayResult) {
//        switch (actiontype) {
//
//            case CARDBALANCE_SUCCESS: // 余额查询更新界面
//
//                main_tv_myzichan.setText(MoneyEncoder.decodeFormat(qtpayResult
//                        .getAvailableAmt()));
//                // main_tv_ketixian.setText(""+
//                // MoneyEncoder.decodeFormat(qtpayResult
//                // .getDayTradeAmt()));
//                if (!"".equals(qtpayResult
//                        .getDayTradeAmt())) {
//                    main_tv_ketixian.setText("￥"
//                            + new java.text.DecimalFormat("#0.00").format(new BigDecimal(
//                            qtpayResult
//                                    .getDayTradeAmt()).setScale(2, BigDecimal.ROUND_DOWN)
//                            .doubleValue()));
//                }
//                break;
//            case LOGINOUT: // 退出了登录，
//                break;
//            case CAN_WITHDRAW: // 可以提款
//            case CAN_NOT_WITHDRAW: // 可以提款
//                break;
//            case SET_HEAD:
//
//                cropfile = new File(PreferenceUtil.getInstance(
//                        getActivity().getApplicationContext()).getString(
//                        "cropPath", ""));
//
//                Log.v("jics", "cropfile===" + cropfile.getPath());
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = false;
//
//                options.inSampleSize = 1;
//                options.inPreferredConfig = Bitmap.Config.ARGB_4444; // 默认是Bitmap.Config.ARGB_8888
//                options.inPurgeable = true;
//                options.inInputShareable = true;
//
//                if (bitmap != null && !bitmap.isRecycled())
//                    bitmap.recycle();
//                bitmap = null;
//
//                if (cropfile.exists()) {
//                    // bitmap =
//                    // BitmapUntils.decodeUriAsBitmap(Uri.fromFile(cropfile));
//                    bitmap = BitmapFactory.decodeFile(cropfile.getAbsolutePath(),
//                            options);
//
//                }
//
//                if (null == bitmap) {
//                    tempPath = PreferenceUtil.getInstance(
//                            getActivity().getApplicationContext()).getString(
//                            "tempPath", "");
//                    // bitmap = BitmapUntils.decodeUriAsBitmap(Uri.fromFile(new
//                    // File(tempPath)), "");
//                    bitmap = BitmapFactory.decodeFile(tempPath, options);
//                }
//
//                // minibitmap = BitmapUntils.DengBichangeBitmapSize(bitmap, 100,
//                // 100);
//                // ci.setImageBitmap(minibitmap);
//                ci.setImageBitmap(bitmap);
//                PreferenceUtil.getInstance(getActivity().getApplicationContext())
//                        .saveString(
//                                "ICO_"
//                                        + QtpayAppData.getInstance(
//                                        getActivity()
//                                                .getApplicationContext())
//                                        .getMobileNo(), SaveAsFile(bitmap));
//
//                break;
//
//            case UPDATE_POINT:
//
//                datastring = qtpayResult.getData();
//
//                JSONObject jsonObj;
//                try {
//
//                    if (datastring != null) {
//                        jsonObj = new JSONObject(datastring);
//                        powermsg = JsonUtils.getValueFromJSONObject(jsonObj, "FLAG");
//                    }
//
//                    // LogUtil.printInfo("powermsg==="+powermsg+"eeeeee"+datastring);
//
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                showimg();
//
//                break;
//
//        }
//        actiontype = -1; // 用完把状态变掉，防止出错
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode==RyxAppconfig.CLOSE_HOMEFRG_REQ&&resultCode==RyxAppconfig.CLOSE_ALL){
//           mListener.doDataRequest("mainFrg_change");
//        }else{
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//        LogUtil.showLog("UserFragment===onActivityResult");
//    }

    @Override
    public void onAttach(Context context) {
        try {
            mListener = (MainFragmentActivity) getBaseActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);
    }
    /**
     * 展示实名认证框
     */
    private void showAuthDialog() {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(getContext(), new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                gotoRealName(null);
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("亲爱的用户，为了确保您的资金安全，进行此业务前需通过实名认证。");
    }
}
