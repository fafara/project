package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.BankLisAdapter;
import com.ryx.payment.ruishua.adapter.UnionQrcodePayTypeAdapter;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.ScanningCodeActivity_;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.widget.RecyclerViewDivider;
import com.ryx.swiper.utils.MoneyEncoder;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码付款（微信，支付宝，银联二维码支付）
 */
@EActivity(R.layout.union_qrcode_pay)
public class UnionQrcodePayActivity extends BaseActivity {
    @ViewById
    AutoRelativeLayout im_pay_selectbankid;
    @ViewById
    TextView impayphonetv, impay_username, impay_bankname, impay_accountno;
    String accountNo = "", realName, cardIdx = "", customerId = "";
    @ViewById
    View im_pay_poundagetvline, banklistviewLine;
    ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();
    ArrayList<Map<String, String>> payTypesMap = new ArrayList<Map<String, String>>();
    private int selectedId = 0;//上次选中的值
    private int selectedBankId = 0;//当前选中的银行角标
    boolean isRegisted = false;// 用户是否注册
    String slectedval;//选择的支付类型名
    String slectedName;//选择的支付类型名
    @ViewById
    ImageView impay_bankimg;
    @ViewById
    EditText impay_paymoney;
    @ViewById
    RecyclerView recyclerView_typesid;

    @AfterViews
    public void initView() {
        setTitleLayout("二维码收款", true);
        setRightImgMessage("用户须知", RyxAppconfig.Notes_ScanReceipt);
        initQtPatParams();
        bindData();
    }

    private void bindData() {
        String mobileNo = QtpayAppData.getInstance(UnionQrcodePayActivity.this).getMobileNo();
        if (TextUtils.isEmpty(mobileNo)) {
            toAgainLogin(UnionQrcodePayActivity.this, RyxAppconfig.TOLOGINACT);
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
        userInfo(new CompleteResultListen() {
            @Override
            public void compleResultok() {
                //二维码支付方式列表
                _initPayType(new CompleteResultListen() {
                    @Override
                    public void compleResultok() {

                    }
                });
            }
        });
    }

    private void _initPayType(CompleteResultListen completeResultListen) {
//        二维码支付方式列表
        qtpayApplication.setValue("APPParamsService.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("id", "SK"));
        httpsPost("APPParamsServiceTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeMerchantInfoJson(payResult.getData());

            }
        });
    }

    /**
     * 解析支付类型JSON
     */
    private void analyzeMerchantInfoJson(String jsonstring) {
        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                JSONArray resultBeanArray = jsonObj.getJSONArray("result");
                String code = JsonUtil.getValueFromJSONObject(jsonObj, "code");
                String msg = JsonUtil.getValueFromJSONObject(jsonObj, "msg");
                if (!RyxAppconfig.QTNET_SUCCESS.equals(code)) {
                    LogUtil.showToast(UnionQrcodePayActivity.this, msg + "");
                    return;
                }
                for (int i = 0; i < resultBeanArray.length(); i++) {
                    JSONObject jsonobject = (JSONObject) resultBeanArray.get(i);
                    String val = JsonUtil.getValueFromJSONObject(jsonobject, "val");
                    String name = JsonUtil.getValueFromJSONObject(jsonobject, "name");

                    Map<String, String> paytyp2 = new HashMap<>();
                    paytyp2.put("val", val);
                    paytyp2.put("merchantName", name);
                    payTypesMap.add(paytyp2);
                }
                if (payTypesMap.isEmpty()) {
                    recyclerView_typesid.setVisibility(View.GONE);
                    return;
                }

                recyclerView_typesid.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(UnionQrcodePayActivity.this);
                recyclerView_typesid.setLayoutManager(linearLayoutManager);
                UnionQrcodePayTypeAdapter unionqrcodepaytypeadapter=    new UnionQrcodePayTypeAdapter(payTypesMap,UnionQrcodePayActivity.this,R.layout.unionqrcodepay_paytype_item);
                recyclerView_typesid.setAdapter(unionqrcodepaytypeadapter);
                RecyclerViewDivider horizontalLine = new RecyclerViewDivider(
                        UnionQrcodePayActivity.this, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(UnionQrcodePayActivity.this, com.ryx.quickadapter.R.color.grey_line));
                recyclerView_typesid.addItemDecoration(horizontalLine);
                unionqrcodepaytypeadapter.setOnItemClickListener(new OnListItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, Object data) {
                        payTypeitemClick(position);
                    }
                });
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.showToast(UnionQrcodePayActivity.this, "数据解析异常!");
        }
    }

    /**
     * 获取用户真实姓名
     */
    public void userInfo(final CompleteResultListen listenInterface) {
        qtpayApplication.setValue("UserInfoQuery.Req");
        Param qtpayTransType = new Param("transType", "01");
        qtpayMobileNO.setValue(impayphonetv.getText().toString());
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayTransType);
        httpsPost("userinfoTg", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeJson(payResult.getData());
                impay_username.setText(realName);
                if (!isRegisted) {
                    LogUtil.showToast(UnionQrcodePayActivity.this, "用户未注册");
                }
                listenInterface.compleResultok();
            }
        });
    }

    @Click(R.id.im_pay_selectbankid)
    public void showBottomSheet(View view) {
        disabledTimerView(view);
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(UnionQrcodePayActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(UnionQrcodePayActivity.this).inflate(R.layout.impay_bottomsheet, null);
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        final BankLisAdapter bankLisAdapter = new BankLisAdapter(UnionQrcodePayActivity.this, bankcardlist, accountNo);
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
                    LogUtil.showToast(UnionQrcodePayActivity.this, cardnote + "");
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

    private void resetBankListView(int i) {
        if (bankcardlist.size() > 0) {
            selectedBankId = i;
            im_pay_selectbankid.setVisibility(View.VISIBLE);
            banklistviewLine.setVisibility(View.VISIBLE);
            //闪付储蓄卡操作
            BanksUtils.selectIcoidToImgView(UnionQrcodePayActivity.this, bankcardlist.get(i).getBankId(), impay_bankimg);
            impay_bankname.setText(bankcardlist.get(i).getBankName());
            impay_accountno.setText(StringUnit.impaycardJiaMi(bankcardlist.get(i).getAccountNo()));
            accountNo = bankcardlist.get(i).getAccountNo();
            cardIdx = bankcardlist.get(i).getCardIdx();
        } else {
            im_pay_selectbankid.setVisibility(View.GONE);
        }
    }
    public void nextBtnCLick(final int position) {
        String textStr = impay_paymoney.getText().toString();
        try {
            Double.parseDouble(textStr);
        } catch (Exception e) {
            LogUtil.showToast(UnionQrcodePayActivity.this, "请正确输入金额!");
            return;
        }
        final String moneyStr = MoneyEncoder.EncodeFormat(textStr);

        if (!isRegisted) {
            LogUtil.showToast(UnionQrcodePayActivity.this, "收款用户未注册,不能付款！");
            return;
        }

        if (moneyStr.length() == 0) {
            LogUtil.showToast(UnionQrcodePayActivity.this, "付款金额不能为空");
            return;
        }

        if (moneyStr.equals("￥0.00")) {
            LogUtil.showToast(UnionQrcodePayActivity.this, "付款金额必须大于零");
            return;
        }

        if (moneyStr.replace(",", "").replace("￥", "")
                .length()
                - moneyStr.replace(",", "")
                .replace("￥", "").indexOf(".") > 3) {
            LogUtil.showToast(UnionQrcodePayActivity.this, "付款金额单位过小");
            return;
        }
        if (moneyStr.replace(",", "").replace("￥", "")
                .length() > 9) {
            LogUtil.showToast(UnionQrcodePayActivity.this, "付款金额超限");
            return;
        }
        if (TextUtils.isEmpty(accountNo)) {
            LogUtil.showToast(UnionQrcodePayActivity.this, "请选择收款账户");
            return;
        }

        if("UPZF_SAOMA".equals(slectedval)){
            Intent intent=new Intent(UnionQrcodePayActivity.this, ScanningCodeActivity_.class);
                Bundle bundle=new Bundle();
                bundle.putString("amount",MoneyEncoder.encodeToPost(moneyStr));
                bundle.putString("cardIdx",cardIdx);
                bundle.putBoolean("unionQrcode",true);
                bundle.putString("payType","shoukuan");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            return;
        }
        qtpayApplication.setValue("APPParamsService.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("id", "SMSK"));
        httpsPost("APPParamsServiceTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                Intent intent = new Intent(UnionQrcodePayActivity.this,UnionQrcodePayShowActivity_.class);
                        intent.putExtra("typeJsonStr",payResult.getData());
                        intent.putExtra("amount", MoneyEncoder.encodeToPost(moneyStr));
                        intent.putExtra("cardIdx", cardIdx);
                        intent.putExtra("payType", slectedval);
                        startActivity(intent);
                    finish();
            }
        });
    }

    public void payTypeitemClick(int position) {
        slectedval = payTypesMap.get(position).get("val");
        slectedName = payTypesMap.get(position).get("merchantName");
        nextBtnCLick(position);

    }


}
