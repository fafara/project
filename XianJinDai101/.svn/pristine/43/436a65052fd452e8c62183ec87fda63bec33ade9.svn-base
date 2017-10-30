package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.BankLisAdapter;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.BindedCardListActivity_;
import com.ryx.payment.ruishua.bindcard.BindedCardOpenQuickPay_;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.ryxkeylib.listener.EditPwdListener;
import com.ryx.ryxkeylib.service.CustomKeyBoardService;
import com.ryx.swiper.utils.MoneyEncoder;
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
 * 扫码快捷支付
 */
@EActivity(R.layout.activity_sweep_quick_pay)
public class SweepQuickPayActivity extends BaseActivity {
    @ViewById
    TextView payee_phoneid, payee_nameid, quick_btn_sendmsg,  quickpay_bankname, quickpay_accountno;
    @ViewById
    EditText quick_pay_paymoney, quick_pay_myphonenumberid,  quick_edit_loginpasw;

    @ViewById
    View banklistviewLine;
    @ViewById
    AutoRelativeLayout  quick_pay_selectbankid;
    @ViewById
    Button quick_btn_ok;
    @ViewById
    ImageView quickpay_bankimg;
    private String ordermoneyStr,qrcodeskaccount,re_orderid;

    String toPay_phoneNumber = "";
    Param qtpayCardIdx;
    String banklistJson; // 接收查询返回的 银行列表json数据
    ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();
    String selectedaccountNo = "";
    String isOpenQuickPay;
    RyxSimpleConfirmDialog ryxSimpleConfirmDialog;
     BottomSheetDialog mBottomSheetDialog;

    String toPay_userName,payee,qrcode;
    @AfterViews
    public void initView() {
        setTitleLayout("扫码付款", true, true);
        setRightImgMessage("扫码须知", RyxAppconfig.Notes_ScanExplanation);
        initIntentData();
        initQuickPayView();
        initQtPatParams();
//        iniRyxKeyWord();
    }

    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayCardIdx = new Param("cardIdx");
    }



    /**
     * 初始化Intent数据
     */
    private void initIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payee= bundle.getString("payee");
            toPay_phoneNumber= bundle.getString("mobileno");
            toPay_userName= bundle.getString("username");
            qrcode= bundle.getString("qrcode");
            ordermoneyStr= bundle.getString("ordermoney","");
            qrcodeskaccount= bundle.getString("qrcodeskaccount","");
            re_orderid= bundle.getString("re_orderid","");
        } else {
            LogUtil.showToast(SweepQuickPayActivity.this, "二维码信息有误!");
        }
    }

    /**
     * 初始化View及监听
     */
    private void initQuickPayView() {
        payee_phoneid.setText(toPay_phoneNumber);
        payee_nameid.setText(toPay_userName);
        if(!TextUtils.isEmpty(ordermoneyStr)){
            quick_pay_paymoney.setFocusable(false);
            quick_pay_paymoney.setEnabled(false);
            quick_pay_paymoney.setFocusableInTouchMode(false);
            quick_pay_paymoney.setText(String.format("%.2f", Double.parseDouble(ordermoneyStr)/100));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bankcardlist.clear();
        if (QtpayAppData.getInstance(SweepQuickPayActivity.this).isLogin()) {
            if(mBottomSheetDialog!=null&&mBottomSheetDialog.isShowing()){
                mBottomSheetDialog.dismiss();
            }
            getDebitCardList();
        }
    }

    /**
     * 是否开通快捷支付的对话框
     * @param conFirmDialogListener
     */
    public void ryxDialog(ConFirmDialogListener conFirmDialogListener){
        if(ryxSimpleConfirmDialog==null){
            ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(SweepQuickPayActivity.this);
        }
        ryxSimpleConfirmDialog.setOnClickListen(conFirmDialogListener);
        if(ryxSimpleConfirmDialog.isShowing()){
            return;
        }
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("当前卡暂未开通快捷支付,请先开通快捷支付!");
    }

    /**
     * 获取已经绑定的银行卡
     */
    public void getDebitCardList() {
        qtpayApplication.setValue("BindCardList.Req");
        qtpayAttributeList.add(qtpayApplication);
//        qtpayParameterList.add(new Param("cardType","01"));
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
        mBottomSheetDialog = new BottomSheetDialog(SweepQuickPayActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(SweepQuickPayActivity.this).inflate(R.layout.impay_bottomsheet, null);
        TextView settingTv = (TextView) boottomView.findViewById(R.id.title_setting_tv);
        settingTv.setVisibility(View.GONE);
//        settingTv.setText("管理");
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        final BankLisAdapter bankLisAdapter = new BankLisAdapter(SweepQuickPayActivity.this, bankcardlist, selectedaccountNo,true);
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
                startActivity(new Intent(SweepQuickPayActivity.this, BindedCardListActivity_.class));
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
            BanksUtils.selectIcoidToImgView(SweepQuickPayActivity.this,bankCardInfo.getBankId(), quickpay_bankimg);
            quickpay_bankname.setText(bankCardInfo.getBankName());
            quickpay_accountno.setText(StringUnit.cardJiaMi(bankCardInfo.getAccountNo()));
            isOpenQuickPay= bankCardInfo.getQuick();//1是已开通
            quick_pay_selectbankid.setVisibility(View.VISIBLE);
            if(!"1".equals(isOpenQuickPay)){
                ryxDialog(new ConFirmDialogListener() {
                    @Override
                    public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                        ryxSimpleConfirmDialog.dismiss();
                        //请求所需条件跳转页面
//                        Intent intent=new Intent(SweepQuickPayActivity.this, BindedCardOpenQuickPay_.class);
//                        intent.putExtra("accountNo",selectedaccountNo);
//                        startActivity(intent);
                        toBindedCardOpenQuickPay(selectedaccountNo);
                    }
                    @Override
                    public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                        ryxSimpleConfirmDialog.dismiss();
                    }
                });
            }
        } else {
            quick_pay_selectbankid.setVisibility(View.GONE);
        }
    }

    /**
     * 密码键盘初始化
     */
    private void iniRyxKeyWord() {
        CustomKeyBoardService customKeyBoardService = CustomKeyBoardService.registerKeyBoardForEdit(SweepQuickPayActivity.this, true, quick_edit_loginpasw, new EditPwdListener() {
            @Override
            public void getPwdVal(String realVal, String disVal) {
                quick_edit_loginpasw.setText(realVal);
            }

            @Override
            public void getPwdDisVal(String disVal, int count) {
                quick_edit_loginpasw.setText(disVal);
            }

            @Override
            public void pwdViewOkbtnLisener() {

            }

        });
        customKeyBoardService.setEditMaxLenth(20);
    }

    public boolean checkInput() {
        if(TextUtils.isEmpty(ordermoneyStr)){

        String pay_paymoney = quick_pay_paymoney.getText().toString();
        try {
            Double.parseDouble(pay_paymoney);
        } catch (Exception e) {
            LogUtil.showToast(SweepQuickPayActivity.this, "输入金额有误!");
            return false;
        }

        String moneyStr = MoneyEncoder.EncodeFormat(pay_paymoney);
        if (moneyStr.length() == 0) {
            LogUtil.showToast(SweepQuickPayActivity.this, "付款金额不能为空");
            return false;
        }

        if (moneyStr.equals("￥0.00")) {
            LogUtil.showToast(SweepQuickPayActivity.this, "付款金额必须大于零");
            return false;
        }

        if (moneyStr.replace(",", "").replace("￥", "")
                .length()
                - moneyStr.replace(",", "")
                .replace("￥", "").indexOf(".") > 3) {
            LogUtil.showToast(SweepQuickPayActivity.this, "付款金额单位过小");
            return false;
        }
        if (moneyStr.replace(",", "").replace("￥", "")
                .length() > 9) {
            LogUtil.showToast(SweepQuickPayActivity.this, "付款金额超限");
            return false;
        }
        }
        if(TextUtils.isEmpty(selectedaccountNo)){
            LogUtil.showToast(SweepQuickPayActivity.this, "请选择付款卡!");
            return false;
        }
            return  true;
    }

//    /**
//     * 发送验证码
//     */
//    @Click(R.id.quick_btn_sendmsg)
//    public void sendMsg(){
//        String myphonenumber = quick_pay_myphonenumberid.getText().toString();
//        if (TextUtils.isEmpty(myphonenumber) || myphonenumber.length() != 11) {
//            LogUtil.showToast(SweepQuickPayActivity.this, "请正确填写手机号!");
//            return ;
//        }
////            quick_pay_verificationcode.setText("");
//            sendSmS();
//            timer = null;
//            timer = new Timer();
//            startCountdown();   // 开始倒计时
//    }


//    /**
//     * 开始倒计时
//     */
//    private void startCountdown() {
//        TimerTask task = new TimerTask() {
//            int secondsRremaining = 60;
//            public void run() {
//                Message msg = new Message();
//                msg.what = secondsRremaining--;
//                timehandler.sendMessage(msg);
//            }
//        };
//        timer.schedule(task, 1000, 1000);
//    }

//    /**
//     * 发送验证码
//     */
//    private void sendSmS() {
//        qtpayApplication.setValue("SendAdvancedVipSMS.Req");
//        Param phoneParam=new Param("bankTel");
//        String phoneNumber=quick_pay_myphonenumberid.getText().toString();
//        phoneParam.setValue(phoneNumber);
//        qtpayAttributeList.add(qtpayApplication);
//        qtpayParameterList.add(phoneParam);
//        httpsPost("SendAdvancedVipSMSTag", new XmlCallback() {
//            @Override
//            public void onTradeSuccess(RyxPayResult payResult) {
//                //获取验证码成功
//                Toast.makeText(SweepQuickPayActivity.this, getResources().getString(R.string.sms_has_been_issued_please_note_that_check), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    Timer timer = new Timer();
//    Handler timehandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            if (msg.what > 0) {
//                quick_btn_sendmsg.setTextColor(getResources().getColor(R.color.text_a));
//                quick_btn_sendmsg.setText(getResources().getString(R.string.resend)
//                        + "(" + msg.what + ")");
//                quick_btn_sendmsg.setClickable(false);
//            } else {
//                timer.cancel();
//                quick_btn_sendmsg.setText(getResources().getString(
//                        R.string.resend_verification_code));
//                quick_btn_sendmsg.setClickable(true);
//                quick_btn_sendmsg.setTextColor(getResources().getColor(R.color.blue));
//            }
//        };
//    };

    /**
     * 提交信息按钮
     */
    @Click(R.id.quick_btn_ok)
    public void quickBtnClick(){
//            QuickPayment快捷支付支付
            if(!"1".equals(isOpenQuickPay)){
                ryxDialog(new ConFirmDialogListener() {
                    @Override
                    public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                        ryxSimpleConfirmDialog.dismiss();
                        //请求所需条件跳转页面
//                        Intent intent=new Intent(SweepQuickPayActivity.this, BindedCardOpenQuickPay_.class);
//                        intent.putExtra("accountNo",selectedaccountNo);
//                        startActivity(intent);
                        toBindedCardOpenQuickPay(selectedaccountNo);
                    }
                    @Override
                    public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                        ryxSimpleConfirmDialog.dismiss();
                    }
                });
            }else
            if(checkInput()){
                //创建订单
                qtpayApplication.setValue("QuickPaymentOrder.Req");
                qtpayAttributeList.add(qtpayApplication);
                qtpayParameterList.add(new Param("account",selectedaccountNo));
                //此处进行判断是否二维码信息中已经存在金额（是否为瑞银信支付）
                if(TextUtils.isEmpty(ordermoneyStr)){
                    String pay_paymoney = quick_pay_paymoney.getText().toString();
                    final String moneyStr = MoneyEncoder.EncodeFormat(pay_paymoney);
                    qtpayParameterList.add(new Param("amount",MoneyEncoder.encodeToPost(moneyStr)));
                }else{
                    qtpayParameterList.add(new Param("amount",ordermoneyStr));
                    qtpayParameterList.add(new Param("skaccount",qrcodeskaccount));
                }
                qtpayParameterList.add(new Param("payee",payee));
                qtpayParameterList.add(new Param("qrcode",qrcode));
                qtpayParameterList.add(new Param("merchantId","0007000005"));
                qtpayParameterList.add(new Param("productId","0007000000"));
                if(!TextUtils.isEmpty(re_orderid)){
                    qtpayParameterList.add(new Param("re_orderid",re_orderid));
                }
                httpsPost("", new XmlCallback() {
                    @Override
                    public void onTradeSuccess(RyxPayResult payResult) {
                        String resultData=payResult.getData();
                        try {
                            JSONObject resultObj=new JSONObject(resultData);
                           String orderId= resultObj.getString("orderid");
                           String amount= resultObj.getString("amount");
                           String skaccount= resultObj.getString("skaccount");
                           String fkaccount= resultObj.getString("fkaccount");
                           String fkphone= resultObj.getString("fkphone");
                           String skphone= resultObj.getString("skphone");
                           String skusername= resultObj.getString("skusername");
                           String fkusername= resultObj.getString("fkusername");
                            Bundle bundle=new Bundle();
                            bundle.putString("orderId",orderId);
                            bundle.putString("amount","¥"+String.format("%.2f", Double.parseDouble(amount)/100));
                            bundle.putString("toPayuserName",skusername);
                            bundle.putString("account",fkaccount);//选中的支付卡号
                            bundle.putString("mobileno",skphone);//收款人手机号
                            bundle.putString("skaccount",skaccount);//收款人账号
                            bundle.putString("fkphone",fkphone);
                            bundle.putString("fkusername",fkusername);


                            JSONObject cardparamObj=  JsonUtil.getJSONObjectFromJsonObject(resultObj,"cardparam");
                            if(cardparamObj!=null){
                                String needcvn2=  JsonUtil.getValueFromJSONObject(cardparamObj,"needcvn2");
                                String needphone=JsonUtil.getValueFromJSONObject(cardparamObj,"needphone");
                                String needexpiredate=   JsonUtil.getValueFromJSONObject(cardparamObj,"needvaliddate");
                                String needsms=  JsonUtil.getValueFromJSONObject(cardparamObj,"needsms");
                                if(!TextUtils.isEmpty(needcvn2)&&!TextUtils.isEmpty(needphone)&&!TextUtils.isEmpty(needexpiredate)&&!TextUtils.isEmpty(needsms)){
                                    bundle.putString("needcvn2",needcvn2);
                                    bundle.putString("needphone",needphone);
                                    bundle.putString("needexpiredate",needexpiredate);
                                    bundle.putString("needsms",needsms);
                                    bundle.putString("datareplenish","true");
                                    Intent intent= new Intent(SweepQuickPayActivity.this, SweepQuickPayDataReplenishActivity_.class);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent,0x001);
                                    return;
                                }
                            }
                            Intent intent= new Intent(SweepQuickPayActivity.this, SweepQuickPayOrderActivity_.class);
                            intent.putExtras(bundle);
                            startActivityForResult(intent,0x001);
                        } catch (Exception e) {
                          LogUtil.showToast(SweepQuickPayActivity.this,"返回信息有误!");
                        }

                    }
                });


            }
    }
    public void toBindedCardOpenQuickPay(final String selectedaccountNo){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initQtPatParams();
                qtpayApplication = new Param("application", "QuickPaymentBindCardPrepare.Req");
                qtpayAttributeList.add(qtpayApplication);
                qtpayParameterList.add(new Param("account", selectedaccountNo));
                httpsPost("QuickPaymentBindCardPrepareTag", new XmlCallback() {
                    @Override
                    public void onTradeSuccess(RyxPayResult payResult) {
                        String result =  payResult.getData();
                        Intent intent=new Intent(SweepQuickPayActivity.this,BindedCardOpenQuickPay_.class);
                        intent.putExtra("accountNo",selectedaccountNo);
                        intent.putExtra("prepareresult",result);
                        startActivityForResult(intent,0x001);
                    }
                });
            }
        });
    }
}