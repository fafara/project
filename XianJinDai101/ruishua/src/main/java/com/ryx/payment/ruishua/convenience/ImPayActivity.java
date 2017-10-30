package com.ryx.payment.ruishua.convenience;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.BankLisAdapter;
import com.ryx.payment.ruishua.adapter.ImPayCouponsAdapter;
import com.ryx.payment.ruishua.adapter.PayTypeAdapter;
import com.ryx.payment.ruishua.adapter.PayTypeAdapter_;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.payment.ruishua.utils.kt.QRcodeUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.swiper.utils.MoneyEncoder;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

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
import java.util.HashMap;
import java.util.Map;

/**
 * xucc
 * 刷卡收款
 */
@EActivity(R.layout.activity_im_pay)
public class ImPayActivity extends BaseActivity {
    OrderInfo orderinfo;
    private String httpsTag = "ImPayActivityHttps";
    boolean isRegisted = false;// 用户是否注册
    ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();
    @ViewById
    AutoRelativeLayout im_pay_selectbankid;
    @ViewById
    TextView impay_username, impay_bankname, impay_accountno, im_pay_poundagetv, drawingInfo_tv, selectedcoupons_tv,impay_ruibeandesc_tv;
    @ViewById
    TextView impayphonetv;
    @ViewById
    ImageView impay_bankimg;
    String accountNo = "", realName, cardIdx = "",beansCount;
    @ViewById
    EditText impay_paymoney;
    @ViewById
    Button im_pay_nextbtn;
    @ViewById
    View im_pay_poundagetvline, banklistviewLine;
    @ViewById
    AutoLinearLayout im_pay_poundagetvLayout;
    //    private boolean isneendGetBanks;
    String customerId = "";
    ArrayList<Map<String, String>> payTypesMap = new ArrayList<Map<String, String>>();
    ArrayList<JSONObject> couponsList = new ArrayList<JSONObject>();
    private PayTypeAdapter payTypeAdapter;
    @ViewById(R.id.gv_paytype)
    GridView gridView;
    @ViewById(R.id.coupons_layout)
    AutoRelativeLayout coupons_layout;
    private String merchantId, productId, orderRemark, lowlimit, lowfee;
    String rsFee;
    String rsRate;
    private int selectedId = 0;//上次选中的值
    private int selectedBankId = 0;//当前选中的银行角标
    private String selectedbindId;
    private String selectedcouponName;
    @AfterViews
    public void initView() {
//        isneendGetBanks = false;
        setTitleLayout("刷卡收款", true);
        setRightImgMessage("用户须知", RyxAppconfig.Notes_Impay);
       String beandesc= RyxAppdata.getInstance(ImPayActivity.this).getBeanStatusDesc();
        if(TextUtils.isEmpty(beandesc)){
            impay_ruibeandesc_tv.setVisibility(View.GONE);
        }else{
            impay_ruibeandesc_tv.setVisibility(View.VISIBLE);
            impay_ruibeandesc_tv.setText(beandesc);
        }
        initQtPatParams();
        bindData();

    }
    /**
     * 我的代金券
     */
    private void initVouchersData(final CompleteResultListen completeResultListen) {

        qtpayApplication.setValue("QueryAvailableCoupon.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("QueryAvailableCouponTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                String data = payResult.getData();
                if (TextUtils.isEmpty(data)) {
                    LogUtil.showLog("data==" + data);
                    coupons_layout.setVisibility(View.GONE);
                    return;
                }
                /**
                 * acitivity_code : 333
                 * isused : 0
                 * useendtime : 20170420000000
                 * couponname : 2元代金券
                 * usestarttime : 20170414000000
                 * couponvalue : 2
                 * couponid : 1
                 * valueend : 2000
                 * coupontype : 01
                 * valuestart : 1000
                 * bindid : 27
                 */
                try {
                    JSONArray datajsonArray = new JSONArray(data);
                    if (datajsonArray == null || datajsonArray.length() == 0) {
                        coupons_layout.setVisibility(View.GONE);
                        return;
                    }
                    for (int i = 0; i < datajsonArray.length(); i++) {
                        JSONObject acitivityObject = datajsonArray.getJSONObject(i);
                        couponsList.add(acitivityObject);
                    }
                    restcouponsView(-1);

                } catch (Exception e) {
                    LogUtil.showLog("eeeeeeee"+e.getLocalizedMessage());

                }
                completeResultListen.compleResultok();
            }
        });
    }

    /**
     * 设置优惠券
     *
     * @param i
     */
    private void restcouponsView(int i) {
        if(couponsList.size()==0){
            coupons_layout.setVisibility(View.GONE);
            return;
        }

        if(i<0)
        {

            coupons_layout.setVisibility(View.VISIBLE);
            selectedbindId=null;
            selectedcoupons_tv.setText("请选择优惠劵");
            return;
        }
        try {
            LogUtil.showLog(couponsList.toString());
            coupons_layout.setVisibility(View.VISIBLE);
            JSONObject acitivityObject = QRcodeUtil.Factory.filterData(merchantId+productId,couponsList).get(i);
            selectedbindId = JsonUtil.getValueFromJSONObject(acitivityObject, "bindid");
            String coupontype = JsonUtil.getValueFromJSONObject(acitivityObject, "coupontype");
            String couponName = JsonUtil.getValueFromJSONObject(acitivityObject, "couponname");
            String couponvalue = JsonUtil.getValueFromJSONObject(acitivityObject, "couponvalue");
            if ("01".equals(coupontype)) {
                selectedcouponName = couponvalue + "元" + couponName;
            } else if ("02".equals(coupontype)) {
                selectedcouponName = Double.parseDouble(couponvalue) / 10.00 + "折  " + couponName;
            }
            selectedcoupons_tv.setText(selectedcouponName);
        } catch (Exception e) {
            coupons_layout.setVisibility(View.GONE);
        }

    }

    /**
     * 优惠券数据解析
     *
     * @param availableData
     */
    private void initAvailableData(String availableData) {
        try {
            JSONArray availableJsonArray = new JSONArray(availableData);
            for (int i = 0; i < availableJsonArray.length(); i++) {
                JSONObject availableObject = availableJsonArray.getJSONObject(i);
                String couponName = JsonUtil.getValueFromJSONObject(availableObject, "couponName");


            }

        } catch (Exception e) {

        }

    }

    /*
       *初始化支付类型：TO，T1等
        */
    public void initPayType(final ListenInterface listenInterface) {
        qtpayApplication.setValue("GetMerchantInfo.Req");
        Param merchantTypeIdParam = new Param("merchantTypeId");
        merchantTypeIdParam.setValue(RyxAppdata.getInstance(this).getCurrentBranchId());
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(merchantTypeIdParam);
        httpsPost("GetMerchantInfoTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeMerchantInfoJson(payResult.getData());
                listenInterface.onCompleteListen();
            }
        });

    }

    /**
     * 优惠券列表
     */
    @Click(R.id.coupons_layout)
    public void couponsLayoutClick(View view) {
        InputMethodManager imm = (InputMethodManager)ImPayActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        showcouponsLayout(view);

    }

    /**
     * 展示优惠券视图
     */
    private void showcouponsLayout(View view) {
        disabledTimerView(view);
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ImPayActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(ImPayActivity.this).inflate(R.layout.impay_coupons_bootomsheet, null);
        final RecyclerView boottomRecyclerView = (RecyclerView) boottomView.findViewById(R.id.coupons_recyclerView);
        RecyclerViewHelper.init().setRVGridLayout(this, boottomRecyclerView, 1);//1列
        //// TODO: 17/4/19 根据选择T0,T1传递类型
        final ImPayCouponsAdapter couponsAdapter = new ImPayCouponsAdapter(QRcodeUtil.Factory.filterData(merchantId+productId,couponsList), ImPayActivity.this, selectedbindId, R.layout.impay_coupons_item);
        boottomRecyclerView.setAdapter(couponsAdapter);
        Button couponsClosebtn = (Button) boottomView.findViewById(R.id.coupons_closebtn);
        TextView cancelTv = (TextView) boottomView.findViewById(R.id.cancel_tv);
        couponsClosebtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        cancelTv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                restcouponsView(-1);
                mBottomSheetDialog.dismiss();
            }
        });
        couponsAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                restcouponsView(position);

                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.contentView(boottomView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBottomSheetDialog.show();
                    }
                });

            }
        }).start();
    }

    @ItemClick(R.id.gv_paytype)
    public void mobileClick(int position) {
       String mymerchantId= payTypesMap.get(position).get("merchantId");
       String myproductid= payTypesMap.get(position).get("productId");
        if(!TextUtils.isEmpty(merchantId)&&!TextUtils.isEmpty(productId)&&merchantId.equals(mymerchantId)&&productId.equals(myproductid)){
            return;
        }
        restcouponsView(-1);
        merchantId =mymerchantId;
        productId =myproductid;
        orderRemark = payTypesMap.get(position).get("merchantName");
        rsFee = payTypesMap.get(position).get("fixedFee");
        rsRate = payTypesMap.get(position).get("stdRate");
        lowlimit = payTypesMap.get(position).get("lowlimit");
        lowfee = payTypesMap.get(position).get("lowfee");

        if (RyxAppconfig.israteShow) {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
            String moneyStr = impay_paymoney.getText().toString().trim();
            try {
                Double.parseDouble(moneyStr);
            } catch (Exception e) {
                LogUtil.showToast(ImPayActivity.this, "输入金额有误!");
                return;
            }
            if (!TextUtils.isEmpty(moneyStr)) {
                Double moneyVal = Double.parseDouble(moneyStr);
                Double rsFeeY = Double.parseDouble(rsFee) / 100;
                Double rsRatePer = Double.parseDouble(rsRate);
                Double poundageVal = moneyVal * rsRatePer + rsFeeY;
                Double lowfeeVal = Double.parseDouble(lowfee) / 100;
                if (poundageVal < lowfeeVal) {
                    im_pay_poundagetv.setText("本次手续费:" + df.format(lowfeeVal) + "元");
                } else {
                    im_pay_poundagetv.setText("本次手续费:" + df.format(poundageVal) + "元");
                }
            } else {
                im_pay_poundagetv.setText("本次手续费:0.00 元");
            }

        } else {
            im_pay_poundagetvline.setVisibility(View.GONE);
            im_pay_poundagetvLayout.setVisibility(View.GONE);
        }

        payTypeAdapter.setSelection(position);
        payTypeAdapter.notifyDataSetChanged();
    }

    /**
     * 解析支付类型JSON
     */
    private void analyzeMerchantInfoJson(String jsonstring) {
        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                JSONArray resultBeanArray = jsonObj.getJSONArray("resultBean");

                for (int i = 0; i < resultBeanArray.length(); i++) {
                    JSONObject jsonobject = (JSONObject) resultBeanArray.get(i);
                    String merchantName = JsonUtil.getValueFromJSONObject(jsonobject, "merchantName");
                    String id = JsonUtil.getValueFromJSONObject(jsonobject, "id");
                    String fixedFee = JsonUtil.getValueFromJSONObject(jsonobject, "fixedFee");
                    String stdRate = JsonUtil.getValueFromJSONObject(jsonobject, "stdRate");
                    String merchantValue = JsonUtil.getValueFromJSONObject(jsonobject, "merchantValue");
                    String status = JsonUtil.getValueFromJSONObject(jsonobject, "status");
                    String branchId = JsonUtil.getValueFromJSONObject(jsonobject, "branchId");
                    String merchantTypeId = JsonUtil.getValueFromJSONObject(jsonobject, "merchantTypeId");
                    String merchantId = JsonUtil.getValueFromJSONObject(jsonobject, "merchantId");
                    String appuser = JsonUtil.getValueFromJSONObject(jsonobject, "appuser");
                    String productId = JsonUtil.getValueFromJSONObject(jsonobject, "productId");
                    String lowfee = JsonUtil.getValueFromJSONObject(jsonobject, "lowfee");
                    String lowlimit = JsonUtil.getValueFromJSONObject(jsonobject, "lowlimit");
                    Map<String, String> paytyp2 = new HashMap<>();
                    paytyp2.put("merchantName", merchantName);
                    paytyp2.put("id", id);
                    paytyp2.put("fixedFee", fixedFee);
                    paytyp2.put("stdRate", stdRate);
                    paytyp2.put("merchantValue", merchantValue);
                    paytyp2.put("status", status);
                    paytyp2.put("branchId", branchId);
                    paytyp2.put("merchantTypeId", merchantTypeId);
                    paytyp2.put("merchantId", merchantId);
                    paytyp2.put("appuser", appuser);
                    paytyp2.put("productId", productId);
                    paytyp2.put("lowfee", lowfee);
                    paytyp2.put("lowlimit", lowlimit);
                    String mobileNo = RyxAppdata.getInstance(this).getMobileNo();
                    String productFlag = PreferenceUtil.getInstance(this).
                            getString(mobileNo + "_impay", "");//取值
                    if (!TextUtils.isEmpty(productFlag) && productFlag.equals(productId)) {
                        selectedId = i;
                    }
                    payTypesMap.add(paytyp2);
                }
                if (payTypesMap.isEmpty()) {
                    gridView.setVisibility(View.GONE);
                    LogUtil.showLog("001");
                    return;
                }
                if (payTypesMap.size() == 1) {
                    gridView.setVisibility(View.GONE);
                    LogUtil.showLog("002");
                } else {
                    gridView.setVisibility(View.VISIBLE);
                    payTypeAdapter = PayTypeAdapter_.getInstance_(ImPayActivity.this);
                    payTypeAdapter.setList(payTypesMap);
                    payTypeAdapter.setSelection(selectedId);
                    LogUtil.showLog("003");
                    gridView.setAdapter(payTypeAdapter);

                }
                rsFee = payTypesMap.get(selectedId).get("fixedFee");
                rsRate = payTypesMap.get(selectedId).get("stdRate");
                merchantId = payTypesMap.get(selectedId).get("merchantId");
                productId = payTypesMap.get(selectedId).get("productId");
                orderRemark = payTypesMap.get(selectedId).get("merchantName");
                lowlimit = payTypesMap.get(selectedId).get("lowlimit");
                lowfee = payTypesMap.get(selectedId).get("lowfee");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.showToast(ImPayActivity.this, "数据解析异常!");
        }
    }

    /**
     * 限额说明点击
     */
    @Click(R.id.drawingInfo_tv)
    public void drawingInfoClick() {
        Intent intent = new Intent(ImPayActivity.this, HtmlMessageActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", "限额说明");
        bundle.putString("urlkey", RyxAppconfig.Notes_Quota);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void bindData() {
        drawingInfo_tv.setText(RyxAppdata.getInstance(ImPayActivity.this).getCurrentBranchName() + "说明");
        String mobileNo = QtpayAppData.getInstance(ImPayActivity.this).getMobileNo();
        if (TextUtils.isEmpty(mobileNo)) {
            toAgainLogin(ImPayActivity.this, RyxAppconfig.TOLOGINACT);
            finish();
            return;
        }
        impayphonetv.setText(mobileNo);

        ArrayList<BankCardInfo> bankList = (ArrayList<BankCardInfo>) getIntent().getExtras().getSerializable("bankcardlist");
        if (bankList != null && bankList.size() > 0) {
            banklistviewLine.setVisibility(View.VISIBLE);
            im_pay_selectbankid.setVisibility(View.VISIBLE);
            bankcardlist.addAll(bankList);
            if (bankcardlist.size() == 1) {
                //只有一个银行卡，则直接进行选中(只有当用户点击下一步的时候进行提示)
                resetBankListView(0);
            } else {
                boolean ishaveDefault = false;
                for (int i = 0; i < bankList.size(); i++) {
                    String flagInfo = bankList.get(i).getFlagInfo();
                    String cardstatus = bankList.get(i).getCardstatus();
                    if ("1".equals(flagInfo) && !"1".equals(cardstatus)) {
                        //当前卡为默认结算卡,并且当前卡所属银行不是正处于维护状态的卡
                        resetBankListView(i);
                        ishaveDefault = true;
                        break;
                    }
                }
                if (!ishaveDefault) {
                    boolean isHaveStatusOk = false;
                    for (int j = 0; j < bankList.size(); j++) {
                        String cardstatusj = bankList.get(j).getCardstatus();
                        if (!"1".equals(cardstatusj)) {
                            isHaveStatusOk = true;
                            resetBankListView(j);
                            break;
                        }
                    }
                    if (!isHaveStatusOk) {
                        //当前用户所有绑定卡中没有一个是支持的
                        resetBankListView(0);
                    }
                }
            }
        }
        userInfo(new ListenInterface() {
            @Override
            public void onCompleteListen() {

                //获取支付类型
                initPayType(new ListenInterface() {
                    @Override
                    public void onCompleteListen() {
                        //获取优惠券
                        initVouchersData(new CompleteResultListen() {
                            @Override
                            public void compleResultok() {

                            }
                        });
                    }
                });

            }
        });
    }

    @Click(R.id.im_pay_selectbankid)
    public void showBottomSheet(View view) {
        disabledTimerView(view);
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ImPayActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(ImPayActivity.this).inflate(R.layout.impay_bottomsheet, null);
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        final BankLisAdapter bankLisAdapter = new BankLisAdapter(ImPayActivity.this, bankcardlist, accountNo);
        boottomListView.setAdapter(bankLisAdapter);
        ImageView imageView = (ImageView) boottomView.findViewById(R.id.imgview_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        boottomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cardstatus = bankcardlist.get(position).getCardstatus();
                String cardnote = bankcardlist.get(position).getCardnote();
                if ("1".equals(cardstatus)) {
                    LogUtil.showToast(ImPayActivity.this, cardnote + "");
                    return;
                }
                resetBankListView(position);
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.contentView(boottomView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBottomSheetDialog.show();
                    }
                });

            }
        }).start();
    }

    @Click(R.id.im_pay_nextbtn)
    public void nextBtnCLick() {
        String textStr = impay_paymoney.getText().toString();
        try {
            Double.parseDouble(textStr);
        } catch (Exception e) {
            LogUtil.showToast(ImPayActivity.this, "请正确输入金额!");
            return;
        }
        String moneyStr = MoneyEncoder.EncodeFormat(textStr);

        if (!isRegisted) {
            LogUtil.showToast(ImPayActivity.this, "收款用户未注册,不能付款！");
            return;
        }

        if (moneyStr.length() == 0) {
            LogUtil.showToast(ImPayActivity.this, "付款金额不能为空");
            return;
        }

        if (moneyStr.equals("￥0.00")) {
            LogUtil.showToast(ImPayActivity.this, "付款金额必须大于零");
            return;
        }

        if (moneyStr.replace(",", "").replace("￥", "")
                .length()
                - moneyStr.replace(",", "")
                .replace("￥", "").indexOf(".") > 3) {
            LogUtil.showToast(ImPayActivity.this, "付款金额单位过小");
            return;
        }
        if (moneyStr.replace(",", "").replace("￥", "")
                .length() > 9) {
            LogUtil.showToast(ImPayActivity.this, "付款金额超限");
            return;
        }
        if (TextUtils.isEmpty(accountNo)) {
            LogUtil.showToast(ImPayActivity.this, "请选择收款账户");
            return;
        }
        if (TextUtils.isEmpty(merchantId) || TextUtils.isEmpty(productId)) {
            LogUtil.showToast(ImPayActivity.this, "商户类型有误,请重新获取!");
            return;
        }
        if (!TextUtils.isEmpty(lowlimit)) {
            Double limitDouMoney = Double.parseDouble(lowlimit) / 100;
            if (Double.parseDouble(textStr) < limitDouMoney) {
                LogUtil.showToast(ImPayActivity.this, "最低金额不得低于" + limitDouMoney + "元");
                return;
            }
        }


        try {
            if(!TextUtils.isEmpty(selectedbindId)){
            Double money=Double.parseDouble(moneyStr.replace(",", "").replace("￥", ""));
            Double moneyCoupon=QRcodeUtil.Factory.findData(selectedbindId,couponsList).getDouble("valuestart");
            if(money<moneyCoupon) {
                LogUtil.showToast(ImPayActivity.this, "优惠劵金额不符合，请重新选择！");
                return;
            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

        String currentCardstatus = bankcardlist.get(selectedBankId).getCardstatus();
        String cardnote = bankcardlist.get(selectedBankId).getCardnote();
        if ("1".equals(currentCardstatus)) {
            LogUtil.showToast(ImPayActivity.this, cardnote);
            return;
        }
        Intent intent = new Intent(ImPayActivity.this, CreateOrder_.class);
        orderinfo = Order.IMPAY_SHANFU;
        orderinfo.setOrderRemark(orderRemark);
        orderinfo.setOrderAmt(moneyStr);
        orderinfo.setOrderDesc(accountNo);
        orderinfo.setAccount2(realName);
        orderinfo.setDisPlayContent(impayphonetv.getText().toString().trim() + "  " + realName);
//        orderinfo.setMustMpos(true);
        orderinfo.setPayee(customerId);
        orderinfo.setCardIdx(cardIdx);
        //根据merchantID和prodectid确定是走闪付还是小额付
        orderinfo.setMerchantId(merchantId);
        orderinfo.setProductId(productId);
        //是否进行储蓄卡交易拦截
        orderinfo.setIscashCardIntercept(true);
        //交易类型存本地
        PreferenceUtil.getInstance(this).saveString(
                RyxAppdata.getInstance(this).getMobileNo() + "_impay", productId);
        orderinfo.setInterfaceTag("01");
        if(!TextUtils.isEmpty(selectedbindId)){
            orderinfo.setCouponBindId(selectedbindId);
           String couponValue= QRcodeUtil.Factory.findData(selectedbindId,couponsList).getString("couponvalue");
           String couponName= QRcodeUtil.Factory.findData(selectedbindId,couponsList).getString("couponname");
           String coupontype= QRcodeUtil.Factory.findData(selectedbindId,couponsList).getString("coupontype");
            if("01".equals(coupontype)){
                String topDisPlay=couponValue+"元  "+couponName;
                //现金
                orderinfo.setCouponBindDisPaly(topDisPlay);
            }else if("02".equals(coupontype)){
                //折扣
                String topDisPlay=Double.parseDouble(couponValue)/10.00+"折  "+couponName;
                orderinfo.setCouponBindDisPaly(topDisPlay);
            }

        }else{
            orderinfo.setCouponBindDisPaly("");
            orderinfo.setCouponBindId("");
        }
            orderinfo.setUseRuiBean(true);
        intent.putExtra("orderinfo", orderinfo);
        startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
        }catch (Exception e){
            LogUtil.showLog("eeeeee"+e.getLocalizedMessage());
            LogUtil.showToast(ImPayActivity.this,"数据错误,请稍后再试");

        }
    }

    public void getBankCardList2() {
        getNewBankCardList();
    }

    /**
     * 新的卡管理获取银行卡列表
     */
    private void getNewBankCardList() {
        qtpayApplication.setValue("BindCardList.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("cardType", "10"));
        httpsPost("BindCardListTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                newinitBankListData(payResult.getData());
                boolean ishaveDefault = false;
                if (bankcardlist.size() == 1) {
                    resetBankListView(0);
                } else {
                    for (int i = 0; i < bankcardlist.size(); i++) {
                        String flagInfo = bankcardlist.get(i).getFlagInfo();
                        String cardstatus = bankcardlist.get(i).getCardstatus();
                        if ("1".equals(flagInfo) && !"1".equals(cardstatus)) {
                            resetBankListView(i);
                            ishaveDefault = true;
                            break;
                        }
                    }
                    if (!ishaveDefault) {
                        boolean isHaveStatusOk = false;
                        for (int j = 0; j < bankcardlist.size(); j++) {
                            String cardstatusj = bankcardlist.get(j).getCardstatus();
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
                    }
                }
            }
        });
    }


//    for (int i = 0; i < bankList.size(); i++) {
//        String flagInfo = bankList.get(i).getFlagInfo();
//        String cardstatus=bankList.get(i).getCardstatus();
//        if ("1".equals(flagInfo)&&!"1".equals(cardstatus)) {
//            //当前卡为默认结算卡,并且当前卡所属银行不是正处于维护状态的卡
//            resetBankListView(i);
//            ishaveDefault = true;
//            break;
//        }
//    }
//    if (!ishaveDefault) {
//        for(int j = 0; j < bankList.size(); j++){
//            String cardstatusj=bankList.get(j).getCardstatus();
//            if(!"1".equals(cardstatusj)){
//                resetBankListView(j);
//                break;
//            }
//
//        }
//    }

    private void newinitBankListData(String banklistJson) {
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            bankcardlist.clear();
            if ("0000".equals(jsonObj.getString("code"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONObject("result").getJSONArray("cardlist");

                for (int i = 0; i < banks.length(); i++) {
                    BankCardInfo bankCardInfo = new BankCardInfo();
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

    private void resetBankListView(int i) {
        if (bankcardlist.size() > 0) {
            selectedBankId = i;
            im_pay_selectbankid.setVisibility(View.VISIBLE);
            banklistviewLine.setVisibility(View.VISIBLE);
            //闪付储蓄卡操作
            BanksUtils.selectIcoidToImgView(ImPayActivity.this, bankcardlist.get(i).getBankId(), impay_bankimg);
            impay_bankname.setText(bankcardlist.get(i).getBankName());
            impay_accountno.setText(StringUnit.impaycardJiaMi(bankcardlist.get(i).getAccountNo()));
            accountNo = bankcardlist.get(i).getAccountNo();
            cardIdx = bankcardlist.get(i).getCardIdx();
//            RyxAppdata.getInstance(ImPayActivity.this).saveRuishuaparam(RyxAppdata.getInstance(
//                    ImPayActivity.this).getMobileNo(), accountNo, bankcardlist.get(i).getCardIdx());
        } else {
            im_pay_selectbankid.setVisibility(View.GONE);
        }
    }

    /**
     * 获取用户真实姓名
     */
    public void userInfo(final ListenInterface listenInterface) {
        qtpayApplication.setValue("UserInfoQuery.Req");
//        Param qtpayTransType = new Param("transType", "01");
        qtpayMobileNO.setValue(impayphonetv.getText().toString());
        qtpayAttributeList.add(qtpayApplication);
//        qtpayParameterList.add(qtpayTransType);
        httpsPost("userinfoTg", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeJson(payResult.getData());
                impay_username.setText(realName);
                if (!isRegisted) {
                    LogUtil.showToast(ImPayActivity.this, "用户未注册");
                } else {
//            getBankCardList2();
                }
                listenInterface.onCompleteListen();
            }

            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public void onOtherState() {
            }

            @Override
            public void onTradeFailed() {
                LogUtil.showToast(ImPayActivity.this, "获取绑定银行卡列表失败!");
            }
        });
    }

    /**
     * 解析用户信息
     */
    private void analyzeJson(String jsonstring) {

        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                        "result").getString("resultCode"))) {
                    JSONObject userinfo = jsonObj.getJSONObject("resultBean");
                    if (userinfo.has("customerId")) {
                        customerId = userinfo.getString("customerId");
                    }
                    if(userinfo.has("beansCount")){
                        beansCount=userinfo.getString("beansCount");
                    }
                    realName = userinfo.getString("realName");
                    if ("".equals(realName)) {
                        realName = MessageFormat.format(getResources().getString(R.string.phone_name), RyxAppdata.getInstance(this).getCurrentBranchName());
                    }
                    isRegisted = true;
                } else {
                    realName = "--";
                    isRegisted = false;
                    customerId = "";
                }
            } else {
                realName = "--";
                isRegisted = false;
                customerId = "";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isneendGetBanks) {
//            String mobileNo = QtpayAppData.getInstance(ImPayActivity.this).getMobileNo();
//            if (TextUtils.isEmpty(mobileNo)) {
////                Intent intent = new Intent(ImPayActivity.this, LoginActivity_.class);
////                startActivityForResult(intent,RyxAppconfig.TOLOGINACT);
//                toAgainLogin(ImPayActivity.this,RyxAppconfig.TOLOGINACT);
//                finish();
//                return;
//            }
//            impayphonetv.setText(mobileNo);
//            userInfo(new ListenInterface() {
//                @Override
//                public void onCompleteListen() {
//                    getBankCardList2();
//                }
//            });
//
//        } else {
//            isneendGetBanks = true;
//        }
//    }

    /**
     * 事件完成监听
     */
    interface ListenInterface {
        public void onCompleteListen();
    }
}
