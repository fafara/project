package com.ryx.payment.ruishua.authenticate.newauthenticate;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.authenticate.MerchantCredentialsUpload_;
import com.ryx.payment.ruishua.authenticate.UserToMerchantActivity_;
import com.ryx.payment.ruishua.authenticate.adapter.CreditcardListAdapter;
import com.ryx.payment.ruishua.authenticate.newauthenticate.newcreditcard.CreditAddCardNumberAct_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.newcreditcard.CreditcardVertifyFailNewActivity_;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 实名高级认证结果展示页
 */
@EActivity(R.layout.activity_newauth_result)
public class NewAuthResultAct extends BaseActivity {

    @ViewById(R.id.lay_user_level)
    AutoRelativeLayout lay_user_level;
    @ViewById(R.id.tv_user_level)
    TextView tv_user_level;
    @ViewById(R.id.tv_username)
    TextView tv_username;
    @ViewById(R.id.tv_userid)
    TextView tv_userid;
    @ViewById(R.id.tv_authmsg)
    TextView tv_authmsg;
    @ViewById(R.id.btn_auth)
    Button btn_auth;
    @ViewById
    ImageView img_right,img_merchtauthmsg_right;

    @ViewById(R.id.lv_creditcard)
    ListView lv_creditcard;
    @ViewById
    AutoRelativeLayout businessLicencelayout,merchantNamelyout,merchantAddreslayout,merchtstatelayout;
    @ViewById
    TextView businessLicencenumber_tv,merchantName_tv,merchantAddres_tv,tv_merchtauthmsg;
    @Bean
    CreditcardListAdapter creditcardListAdapter;

    private Param qtpayTransType;
    private String realName, certPid, result, remark, viplevel, tagdesc,merchantRemark;
    //商户相关信息
    private String customerName,customerType,customerAddr,businessLicence;
    private int authenFlag;
    private String grauanteelist, grauanteeadd, vipname;
    private String powermsg = "", rmsmsg = "";
    //是否需要再次请求商户信息接口
    private String isUpgrade;//1正在审核2通过3，审核失败4不允许提交

    /**
     * 绑定卡类型
     */
    private Param qtpayflag;

    private Param qtpayoffset;

    private ArrayList<Map<String, String>> bankList = new ArrayList<Map<String, String>>();
    private final int ADDCARD = 22;
    private final int DELETECARD = 23;
    private final int uploadphoto = 24;
    private boolean isneedreq = false;
    //是否开启商户认证功能
    int isOpenMerchanttag = 0;
    @AfterViews
    public void initViews() {
        setTitleLayout("用户认证", true, false);
        isOpenMerchanttag = RyxAppdata.getInstance(NewAuthResultAct.this).getIsOpenMerchantFlag();
        initDataView();
        powermsg = getIntent().getStringExtra("PowerMsg");
        rmsmsg = getIntent().getStringExtra("rmsmsg");
        creditcardListAdapter.setList(bankList);
        lv_creditcard.setAdapter(creditcardListAdapter);
        innitListViewHeight();
        initQtPatParams();
        getUserInfo();
    }

    /**
     * 初始化listView的高度
     */
    public void innitListViewHeight(){
        try {
            int totalHeight = 0;
            for (int i = 0; i < creditcardListAdapter.getCount(); i++) {
                View listItem = creditcardListAdapter.getView(i, null, lv_creditcard);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = lv_creditcard.getLayoutParams();
            params.height = totalHeight + (lv_creditcard.getDividerHeight() * (lv_creditcard.getCount()-1));
            ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
            lv_creditcard.setLayoutParams(params);
        }catch (Exception e){

        }

    }
    /***
     * 初始化数据及布局
     */
    private void initDataView() {
        //用户认证布局
        lay_user_level.setVisibility(View.GONE);
        img_right.setVisibility(View.GONE);
        btn_auth.setVisibility(View.GONE);
        //商户认证布局
        businessLicencelayout.setVisibility(View.GONE);
        merchantNamelyout.setVisibility(View.GONE);
        merchantAddreslayout.setVisibility(View.GONE);
        merchtstatelayout.setVisibility(View.GONE);
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application", "UserInfoQuery.Req");
        qtpayTransType = new Param("transType", "00");
        qtpayMobileNO.setValue(QtpayAppData.getInstance(NewAuthResultAct.this)
                .getMobileNo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (QtpayAppData.getInstance(NewAuthResultAct.this).isLogin()) {
            bankList.clear();
            creditcardListAdapter.notifyDataSetChanged();
            innitListViewHeight();
            //如果上个页面已经获取过powermsg
            if(isneedreq){
                if(TextUtils.isEmpty(powermsg)||"-1".equals(powermsg)){
                    getPowerMsg();
                }else{
                    getCreditCardList();
                }
            }
            isneedreq=true;
        }
    }

    private void getUserInfo() {
        qtpayApplication.setValue("UserInfoQuery.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayTransType);
        httpsPost("UserInfoQuery", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                isUpgrade=null;
                analyzeUserInfo(payResult.getData());
              if("00".equals(customerType)&&!TextUtils.isEmpty(isUpgrade)){
                //isUpgrade只要有值代表当前个人用户提交过商户信息需要单独获取商户信息接口
                qtpayApplication.setValue("CustomerGetInfo.Req");
                  qtpayAttributeList.add(qtpayApplication);
                httpsPost("CustomerGetInfo", new XmlCallback() {
                    @Override
                    public void onTradeSuccess(RyxPayResult payResult) {
                        //根据后台数据情况解析必要字段
                        analyzeCustomerInfo(payResult.getData());
                        drawViewForServerData();
                        //商户信息展示
                        drawViewForServerCustomerData();
                    }
                });
            }else{
                  drawViewForServerData();
              }

            }
        });
    }

    /**
     * 商户信息解析
     * @param jsonStr
     */
    private void analyzeCustomerInfo(String jsonStr) {

        try {
            if (jsonStr != null && jsonStr.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                customerAddr=JsonUtil.getValueFromJSONObject(jsonObj,
                            "customeraddr");
                customerName=JsonUtil.getValueFromJSONObject(jsonObj,
                            "customername");
                businessLicence=JsonUtil.getValueFromJSONObject(jsonObj,
                            "businesslicence");
                if("3".equals(isUpgrade)){
                    merchantRemark=JsonUtil.getValueFromJSONObject(jsonObj,
                            "reason");
                }
            }
            QtpayAppData.getInstance(NewAuthResultAct.this).setTagDesc(tagdesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //解析用户内容
    private void analyzeUserInfo(String jsonStr) {

        try {
            if (jsonStr != null && jsonStr.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                LogUtil.showLog("result",jsonObj.toString());
                if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                        "result").getString("resultCode"))) {
                    JSONObject userinfo = jsonObj.getJSONObject("resultBean");
                    customerType= JsonUtil.getValueFromJSONObject(userinfo,
                            "customerType");
                    //商户信息
                    customerName=JsonUtil.getValueFromJSONObject(userinfo,
                            "customerName");
                    customerAddr=JsonUtil.getValueFromJSONObject(userinfo,
                            "customerAddr");
                    businessLicence=JsonUtil.getValueFromJSONObject(userinfo,
                            "businessLicence");
                    //以上商户信息
                    realName = JsonUtil.getValueFromJSONObject(userinfo,
                            "realName");
                    certPid = JsonUtil.getValueFromJSONObject(userinfo,
                            "certPid");
                    authenFlag = userinfo.getInt("authenFlag");
                    Log.i("authenFlag","authenFlag:"+authenFlag);
                    remark = JsonUtil.getValueFromJSONObject(userinfo,
                            "remark");
                    viplevel = JsonUtil.getValueFromJSONObject(userinfo,
                            "vipLevel");
                    tagdesc = JsonUtil.getValueFromJSONObject(userinfo,
                            "tagDesc");
                    grauanteelist = JsonUtil.getValueFromJSONObject(userinfo,
                            "guarantorId");
                    grauanteeadd = JsonUtil.getValueFromJSONObject(userinfo,
                            "auditFlag");
                    vipname = JsonUtil.getValueFromJSONObject(userinfo,
                            "vipLevelDescription");

                    //商户信息
                    isUpgrade=JsonUtil.getValueFromJSONObject(userinfo,
                            "isUpgrade");
                }
            }
            QtpayAppData.getInstance(NewAuthResultAct.this).setTagDesc(tagdesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据服务端
     */
    public void drawViewForServerData(){
        QtpayAppData.getInstance(NewAuthResultAct.this).setAuthenFlag(
                authenFlag);
        QtpayAppData.getInstance(NewAuthResultAct.this).setUserType(customerType);
        if("01".equals(customerType)){
            //当前单纯商户
            businessLicencelayout.setVisibility(View.VISIBLE);
            merchantNamelyout.setVisibility(View.VISIBLE);
            merchantAddreslayout.setVisibility(View.VISIBLE);
            //商户信息
            businessLicencenumber_tv.setText(businessLicence);
            merchantName_tv.setText(customerName);
            merchantAddres_tv.setText(customerAddr);
        }
        //认证失败
        if (authenFlag == 4||("00".equals(customerType)&&authenFlag==3&&"3".equals(isUpgrade))) {
            //authenFlag个人或者纯商户认证审核失败 ||个人升级为商户的时候失败
            btn_auth.setText("再次认证");
            btn_auth.setVisibility(View.VISIBLE);
            if(authenFlag == 4){
                img_right.setVisibility(View.VISIBLE);
            }
        } else if("00".equals(customerType)&&authenFlag==3&&TextUtils.isEmpty(isUpgrade)&&1==isOpenMerchanttag){
            //普通用户审核通过并且没有提交过信息
            btn_auth.setText("商户认证");
            btn_auth.setVisibility(View.VISIBLE);
            img_right.setVisibility(View.GONE);
        }else{
            //商户或者个人目前处于审核中
            btn_auth.setVisibility(View.GONE);
            img_right.setVisibility(View.GONE);
        }
        initMerchantStateView(customerType,isUpgrade);
        iniuserInfo(authenFlag);
    }
    /**
     * 根据服务端CustomerGetInfo接口返回的数据展示商户信息
     */
    public void drawViewForServerCustomerData(){
            //商户信息展示
            businessLicencelayout.setVisibility(View.VISIBLE);
            merchantNamelyout.setVisibility(View.VISIBLE);
            merchantAddreslayout.setVisibility(View.VISIBLE);
            //商户信息
            businessLicencenumber_tv.setText(businessLicence);
            merchantName_tv.setText(customerName);
            merchantAddres_tv.setText(customerAddr);
//        "licence_pic_addr": "https://mposprepo.ruiyinxin.com:4430/00800084/A000382000/A000382848/DFIMAGE/1475029511994A000382848.png",
//                "customerid": "A000382848",
//                "create_date": "20160928",
//                "count": "0",
//                "status": "1",
//                "reason": "",
//                "customeraddr": "天津职业大学",
//                "create_time": "102511",
//                "reject_reason": "",
//                "customername": "bbhn",
//                "businesslicence": "ghjk"
            //status和isUpgrade是一致的
    }
    /**
     * 初始化商户信息状态
     * @param isUpgrade
     */
    private void initMerchantStateView(String customerType,String isUpgrade) {
        if("00".equals(customerType)&&"3".equals(isUpgrade)){
            //个人升级商户：个人状态为3为升级失败
            merchtstatelayout.setVisibility(View.VISIBLE);
            img_merchtauthmsg_right.setVisibility(View.VISIBLE);
            tv_merchtauthmsg.setText("审核失败");
            tv_merchtauthmsg.setTextColor(Color.RED);
        }else if("00".equals(customerType)&&"1".equals(isUpgrade)){
            //个人升级商户：个人状态为1为升级审核中
            img_merchtauthmsg_right.setVisibility(View.GONE);
            merchtstatelayout.setVisibility(View.VISIBLE);
            tv_merchtauthmsg.setText("审核中");
            tv_merchtauthmsg.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
    }


    //显示失败原因对话框
    @Click(R.id.img_right)
    public void showFailReason() {
        SimpleDialog.Builder builder = getDialog();
        String failReason =remark;
        if (TextUtils.isEmpty(failReason)) {
            failReason = "暂无原因，请重新认证！";
        }
        builder.message(failReason);
        builder.title("温馨提示");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    //显示失败原因对话框
    @Click(R.id.img_merchtauthmsg_right)
    public void showmerchtauthFailReason() {
        SimpleDialog.Builder builder = getDialog();
        String failReason =merchantRemark;
        if (TextUtils.isEmpty(failReason)) {
            failReason = "暂无原因，请重新认证！";
        }
        builder.message(failReason);
        builder.title("温馨提示");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    private void iniuserInfo(int authenFlag) {
        switch (authenFlag) {
            case 2:
                result = getResources().getString(R.string.audit_documents);
                break;
            case 3:
                result = getResources().getString(R.string.audit_success);
                if (!"".equals(viplevel)) {
                    lay_user_level.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(viplevel) - 1 < 6&&Integer.parseInt(viplevel) - 1>0) {
                        String text = "V" + (Integer.parseInt(viplevel)-1);
                        SpannableString sp = new SpannableString(text);
                        //设置斜体
                        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        tv_user_level.setText(sp);
                    } else {
                        lay_user_level.setVisibility(View.GONE);
                    }
                }
                break;
            case 4:
                result = getResources().getString(R.string.audit_failure);
                break;
        }
        RyxAppdata.getInstance(NewAuthResultAct.this).setRealName(realName);
        tv_username.setText(StringUnit.realNameJiaMi(realName));
        tv_userid.setText(StringUnit.certPidJiaMi(certPid));
        tv_authmsg.setText(tagdesc);
        if (tv_authmsg.getText().toString().equals("审核失败")){
            tv_authmsg.setTextColor(Color.RED);
        }else{
            tv_authmsg.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
        if (TextUtils.isEmpty(powermsg) || "-1".equals(powermsg)){
            getPowerMsg();
        }else{
            getCreditCardList();
        }
    }

    //重新实名认证
    @Click(R.id.btn_auth)
    public void reAuth(View view) {
        if(disabledTimerAnyView()){
            return;
        }
        disabledTimerView(view);
        isneedreq = false;
        if("00".equals(customerType)&&authenFlag==3){
            //当前为普通用户并且审核通过,进行普通用户转商户认证
            startActivityForResult(new Intent(NewAuthResultAct.this,
                    UserToMerchantActivity_.class),uploadphoto);
        }else if("00".equals(customerType)&&authenFlag==4){
            ///普通用户审核失败
//            startActivityForResult(new Intent(NewAuthResultAct.this,
//                    UserAuthPhotoUploadActivity_.class),uploadphoto);
            startActivityForResult(new Intent(NewAuthResultAct.this,
                    IdCardUploadAct_.class),uploadphoto);
        }else if("01".equals(customerType)&&authenFlag==4){
            //普通商户认证失败
            startActivityForResult(new Intent(NewAuthResultAct.this,
                    MerchantCredentialsUpload_.class),uploadphoto);
        }else{
            LogUtil.showToast(NewAuthResultAct.this,"用户状态有误,请重新获取!");
        }
    }

    //添加一张信用卡
    @Click(R.id.btn_credit_auth)
    public void seniorAuth() {
        if(disabledTimerAnyView()){
            return;
        }
        //达到升级标准
        if ("0".equals(powermsg)) {
            isneedreq = false;
//            Intent intent = new Intent(NewAuthResultAct.this,
//                    CreditCardAuthStep1Activity_.class);
            Intent intent = new Intent(NewAuthResultAct.this,
                    CreditAddCardNumberAct_.class);
            intent.putExtra("BankcardId", "");
            startActivityForResult(intent, ADDCARD);
        } else {
            showdialog("cantadd");
        }
    }

    @ItemClick(R.id.lv_creditcard)
    public void clickbankItem(int pos) {
        if ("2".equals(bankList.get(pos).get("status"))
                || "3".equals(bankList.get(pos)
                .get("status"))) {
            isNeedThread = false;
            startActivityForResult(new Intent(NewAuthResultAct.this,
                    CreditcardVertifyFailNewActivity_.class).putExtra("BankCardInfo",
                    (Serializable) bankList.get(pos)), DELETECARD);
        }
    }

    @Click(R.id.tileleftImg)
    public void back() {
        finish();
    }

    private void getPowerMsg() {
        qtpayApplication.setValue("GetPowerMessage.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("MergeFlag", "1"));
        httpsPost("GetPowerMessage", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeCreditInfo(payResult.getData());
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

    private void analyzeCreditInfo(String dataStr) {
        try {
            JSONObject jsonObj = new JSONObject(dataStr);
            powermsg = JsonUtil.getValueFromJSONObject(jsonObj, "FLAG");
            rmsmsg = JsonUtil.getValueFromJSONObject(jsonObj, "RMS");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getCreditCardList();
    }

    private SimpleDialog.Builder getDialog() {
        final SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                fragment.dismiss();
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                fragment.dismiss();
            }
        };
        builder.positiveAction("确定");
        return builder;
    }

    public void showdialog(String type) {
        SimpleDialog.Builder builder = getDialog();
        if ("cantadd".equals(type)) {
            if (TextUtils.isEmpty(rmsmsg)) {
                rmsmsg = "亲爱的用户，您暂未达到升级标准，请再接再厉哦！";
            }
            builder.message(rmsmsg);
        } else {
            builder.message("确认删除该信用卡吗?");
        }
        builder.title("温馨提示");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    private void getCreditCardList() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayflag = new Param("flag", "01");
        qtpayoffset = new Param("offset", "1");
        qtpayApplication.setValue("GetAdvancedVipList.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayflag);
        qtpayParameterList.add(qtpayoffset);
        httpsPost("GetAdvancedVipList", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyseBankList(payResult.getData());
                LogUtil.showLog(payResult.getData() + "---");
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

    private void analyseBankList(String dataStr) {
        bankList.clear();
        try {
            JSONObject jsObj = new JSONObject(dataStr);
            JSONArray banks = jsObj.getJSONArray("resultBean");
            int len = banks.length();
            for (int i = 0; i < len; i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("bankCity", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "bankCity"));
                map.put("remark", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "remark"));
                map.put("bankProvinceId", JsonUtil
                        .getValueFromJSONObject(banks.getJSONObject(i),
                                "bankProvinceId"));
                map.put("account", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "account"));
                map.put("bankName", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "bankName"));
                map.put("bankProvince", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "bankProvince"));
                map.put("bankId", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "bankId"));
                map.put("flagInfo", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "flagInfo"));
                map.put("bankCityId", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "bankCityId"));
                map.put("cardIdx", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "cardIdx"));
                map.put("name", QtpayAppData.getInstance(
                        NewAuthResultAct.this).getRealName());
                map.put("branchBankId", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "branchBankId"));
                map.put("branchBankName", JsonUtil
                        .getValueFromJSONObject(banks.getJSONObject(i),
                                "branchBankName"));

                map.put("type", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "type"));
                map.put("id", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "id"));
                map.put("creditCardTime", JsonUtil
                        .getValueFromJSONObject(banks.getJSONObject(i),
                                "creditCardTime"));
                map.put("addFlag", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "addFlag"));
                map.put("branchBankName", JsonUtil
                        .getValueFromJSONObject(banks.getJSONObject(i),
                                "branchBankName"));
                map.put("status", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "status"));
                map.put("rejectReason", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "rejectReason"));
                map.put("renewFee", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "renewFee"));
                map.put("feeDate", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "feeDate"));

                map.put("statusDesc", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "statusDesc"));
                map.put("feeDesc", JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "feeDesc"));
                if (!"4".equals(JsonUtil.getValueFromJSONObject(
                        banks.getJSONObject(i), "status"))) { // 去除逻辑删除的卡片
                    bankList.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        creditcardListAdapter.setList(bankList);
        creditcardListAdapter.notifyDataSetChanged();
        innitListViewHeight();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADDCARD || requestCode == DELETECARD) {
            if (QtpayAppData.getInstance(NewAuthResultAct.this).isLogin()) {
               bankList.clear();
                getPowerMsg();
            }
        } else if(requestCode == uploadphoto){
            initQtPatParams();
            getUserInfo();
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 网络请求完成接口
     */
    interface  HttpCompleteListen{
        public void completeSuccess(RyxPayResult payResult);
    }
}
