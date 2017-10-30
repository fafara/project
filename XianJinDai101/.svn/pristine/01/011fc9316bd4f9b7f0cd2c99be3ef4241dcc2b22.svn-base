package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.BankLisAdapter;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.BankCardAddActivity_;
import com.ryx.payment.ruishua.bindcard.BindedCardListActivity_;
import com.ryx.payment.ruishua.bindcard.BindedCardOpenQuickPay_;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 三级分销机具申购快捷支付
 */
@EActivity(R.layout.activity_sjfx_dev_pay_quick_pay)
public class SjfxDevPayQuickPayActivity extends BaseActivity {
    @ViewById
    TextView price_tv, orderId_tv,quickpay_bankname, quickpay_accountno,phonenumber_tv,usernam_tv;
    @ViewById
    ImageView quickpay_bankimg;
    String orderId,amount;
    String cardIdx;
    ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();
    BottomSheetDialog mBottomSheetDialog;
    String selectedaccountNo = "";
    String banklistJson; // 接收查询返回的 银行列表json数据
    @ViewById
    AutoRelativeLayout quick_pay_selectbankid;
    String isOpenQuickPay;
    RyxSimpleConfirmDialog ryxSimpleConfirmDialog;
    @ViewById
    View banklistviewLine,tobindingcard;
    @AfterViews
    public void initView(){
        setTitleLayout("设备申购支付", true, false);
        Intent intet= getIntent();
        orderId= intet.getStringExtra("orderId");
        amount= intet.getStringExtra("amount");
        orderId_tv.setText(orderId);
        price_tv.setText("¥"+amount);
        phonenumber_tv.setText( RyxAppdata.getInstance(SjfxDevPayQuickPayActivity.this).getPhone());
        usernam_tv.setText(RyxAppdata.getInstance(SjfxDevPayQuickPayActivity.this).getRealName());
        initQtPatParams();
    }

    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
    }

    @Override
    protected void onResume() {
        super.onResume();
        bankcardlist.clear();
        if (QtpayAppData.getInstance(SjfxDevPayQuickPayActivity.this).isLogin()) {
            if(mBottomSheetDialog!=null&&mBottomSheetDialog.isShowing()){
                mBottomSheetDialog.dismiss();
            }
            getDebitCardList();
        }
    }
    /**
     * 获取已经绑定的银行卡
     */
    public void getDebitCardList() {
        qtpayApplication.setValue("BindCardList.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("BindCardListTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                showBindList(payResult.getData());
            }
        });
    }

    private void showBindList(String data) {
        banklistJson = data;
        initListData();
        resetBankListView(-1);
    }
    /**
     * 初始化银行卡列表
     */
    public void initListData() {
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            if ("0000".equals(jsonObj.getString("code"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONObject("result").getJSONArray("cardlist");
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
                    bankcardlist.add(bankCardInfo);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Click(R.id.quick_pay_selectbankid)
    public void quickPaySelectClick(View view)
    {
        disabledTimerView(view);
        mBottomSheetDialog = new BottomSheetDialog(SjfxDevPayQuickPayActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(SjfxDevPayQuickPayActivity.this).inflate(R.layout.impay_bottomsheet, null);
        TextView settingTv = (TextView) boottomView.findViewById(R.id.title_setting_tv);
        settingTv.setVisibility(View.GONE);
//        settingTv.setText("管理");
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        final BankLisAdapter bankLisAdapter = new BankLisAdapter(SjfxDevPayQuickPayActivity.this, bankcardlist, selectedaccountNo,true);
        boottomListView.setAdapter(bankLisAdapter);
        ImageView imageView = (ImageView) boottomView.findViewById(R.id.imgview_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                startActivity(new Intent(SjfxDevPayQuickPayActivity.this, BindedCardListActivity_.class));
            }
        });
        boottomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
     * 重绘选中的快捷支付卡布局
     *
     * @param position
     */
    private void resetBankListView(int position) {

        if (bankcardlist.size() != 0) {

            if(position==-1&&!TextUtils.isEmpty(selectedaccountNo)){
                for (int i=0;i<bankcardlist.size();i++){
                    String bankCardAccount=bankcardlist.get(i).getAccountNo();
                    if(selectedaccountNo.equals(bankCardAccount)){
                        position=i;
                        break;
                    }
                }
            }else if(position==-1&&TextUtils.isEmpty(selectedaccountNo)){
                position=0;
            }
            //布局展示第一条
            final BankCardInfo bankCardInfo = bankcardlist.get(position);
            selectedaccountNo = bankCardInfo.getAccountNo();
            cardIdx=bankCardInfo.getCardIdx();
            BanksUtils.selectIcoidToImgView(SjfxDevPayQuickPayActivity.this,bankCardInfo.getBankId(), quickpay_bankimg);
            quickpay_bankname.setText(bankCardInfo.getBankName());
            quickpay_accountno.setText(StringUnit.cardJiaMi(bankCardInfo.getAccountNo()));
            isOpenQuickPay= bankCardInfo.getQuick();//1是已开通
            quick_pay_selectbankid.setVisibility(View.VISIBLE);
            banklistviewLine.setVisibility(View.VISIBLE);
            tobindingcard.setVisibility(View.GONE);
            if(!"1".equals(isOpenQuickPay)){
                ryxDialog(new ConFirmDialogListener() {
                    @Override
                    public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                        ryxSimpleConfirmDialog.dismiss();
                        //请求所需条件跳转页面
                        Intent intent=new Intent(SjfxDevPayQuickPayActivity.this, BindedCardOpenQuickPay_.class);
                        intent.putExtra("accountNo",selectedaccountNo);
                        startActivity(intent);
                    }
                    @Override
                    public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                        ryxSimpleConfirmDialog.dismiss();
                    }
                },"当前卡暂未开通快捷支付,请先开通快捷支付!");
            }
        } else {
            quick_pay_selectbankid.setVisibility(View.GONE);
            banklistviewLine.setVisibility(View.GONE);
            tobindingcard.setVisibility(View.VISIBLE);
        }
    }
    @Click(R.id.tobindingcard)
    public void toBindingCard(){
        //去绑定卡界面
        startActivity(new Intent(SjfxDevPayQuickPayActivity.this,BankCardAddActivity_.class));
    }
    /**
     * 是否开通快捷支付的对话框
     * @param conFirmDialogListener
     */
    public void ryxDialog(ConFirmDialogListener conFirmDialogListener,String content){
        if(ryxSimpleConfirmDialog==null){
            ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(SjfxDevPayQuickPayActivity.this);
        }
        ryxSimpleConfirmDialog.setOnClickListen(conFirmDialogListener);
        if(ryxSimpleConfirmDialog.isShowing()){
            return;
        }
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent(content);
    }
    @Click(R.id.quick_btn_ok)
    public void quickBtnOkClick(){
        if(TextUtils.isEmpty(orderId)){
            LogUtil.showToast(SjfxDevPayQuickPayActivity.this,"订单号有误!");
            return;
        }
        if(TextUtils.isEmpty(cardIdx)){
            LogUtil.showToast(SjfxDevPayQuickPayActivity.this,"请先绑定结算卡!");
            return;
        }
        if(!"1".equals(isOpenQuickPay)){
            ryxDialog(new ConFirmDialogListener() {
                @Override
                public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                    ryxSimpleConfirmDialog.dismiss();
                    //请求所需条件跳转页面
                    Intent intent=new Intent(SjfxDevPayQuickPayActivity.this, BindedCardOpenQuickPay_.class);
                    intent.putExtra("accountNo",selectedaccountNo);
                    startActivity(intent);
                }
                @Override
                public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                    ryxSimpleConfirmDialog.dismiss();
                }
            },"当前卡暂未开通快捷支付,请先开通快捷支付!");
            return;
        }
        String customerId= RyxAppdata.getInstance(SjfxDevPayQuickPayActivity.this).getCustomerId();
        qtpayApplication.setValue("PayQuick.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("orderId",orderId));
        qtpayParameterList.add(new Param("cardIdx",cardIdx));
        qtpayParameterList.add(new Param("customerId",customerId));
        httpsPost("PayQuickTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {


            }
        });

    }
}
