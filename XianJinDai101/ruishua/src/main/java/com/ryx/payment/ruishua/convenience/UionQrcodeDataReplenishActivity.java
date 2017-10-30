package com.ryx.payment.ruishua.convenience;

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
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.BankLisAdapter;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.swiper.utils.MoneyEncoder;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 银联二维码主扫信息补充
 */
@EActivity(R.layout.activity_uion_qrcode_data_replenish)
public class UionQrcodeDataReplenishActivity extends PayStateCheckBaseActivity {
    @ViewById
    View  banklistviewLine;
    @ViewById
    AutoRelativeLayout im_pay_selectbankid;
    ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();
    private int selectedBankId = 0;//当前选中的银行角标
    @ViewById
    TextView  impay_bankname, impay_accountno;
    @ViewById
    ImageView impay_bankimg;
    String accountNo = "",cardIdx = "", code="";
    @ViewById
    EditText impay_paymoney;
    @AfterViews
    public void initView(){
        setTitleLayout("扫一扫收款", true, false);
        initQtPatParams();
        bindData();

    }
    private void bindData() {
        String mobileNo = QtpayAppData.getInstance(UionQrcodeDataReplenishActivity.this).getMobileNo();
        if (TextUtils.isEmpty(mobileNo)) {
            toAgainLogin(UionQrcodeDataReplenishActivity.this, RyxAppconfig.TOLOGINACT);
            finish();
            return;
        }
       code= getIntent().getExtras().getString("code");
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

    }
    private void resetBankListView(int i) {
        if (bankcardlist.size() > 0) {
            selectedBankId = i;
            im_pay_selectbankid.setVisibility(View.VISIBLE);
            banklistviewLine.setVisibility(View.VISIBLE);
            //闪付储蓄卡操作
            BanksUtils.selectIcoidToImgView(UionQrcodeDataReplenishActivity.this, bankcardlist.get(i).getBankId(), impay_bankimg);
            impay_bankname.setText(bankcardlist.get(i).getBankName());
            impay_accountno.setText(StringUnit.impaycardJiaMi(bankcardlist.get(i).getAccountNo()));
            accountNo = bankcardlist.get(i).getAccountNo();
            cardIdx = bankcardlist.get(i).getCardIdx();
        } else {
            im_pay_selectbankid.setVisibility(View.GONE);
        }
    }

    @Click(R.id.im_pay_selectbankid)
    public void showBottomSheet(View view) {
        disabledTimerView(view);
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(UionQrcodeDataReplenishActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(UionQrcodeDataReplenishActivity.this).inflate(R.layout.impay_bottomsheet, null);
        final ListView boottomListView = (ListView) boottomView.findViewById(R.id.im_pay_bottomListViewid);
        final BankLisAdapter bankLisAdapter = new BankLisAdapter(UionQrcodeDataReplenishActivity.this, bankcardlist, accountNo);
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
                    LogUtil.showToast(UionQrcodeDataReplenishActivity.this, cardnote + "");
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
            LogUtil.showToast(UionQrcodeDataReplenishActivity.this, "请正确输入金额!");
            return;
        }
        String moneyStr = MoneyEncoder.EncodeFormat(textStr);

        if (moneyStr.length() == 0) {
            LogUtil.showToast(UionQrcodeDataReplenishActivity.this, "付款金额不能为空");
            return;
        }

        if (moneyStr.equals("￥0.00")) {
            LogUtil.showToast(UionQrcodeDataReplenishActivity.this, "付款金额必须大于零");
            return;
        }
        if (moneyStr.replace(",", "").replace("￥", "")
                .length()
                - moneyStr.replace(",", "")
                .replace("￥", "").indexOf(".") > 3) {
            LogUtil.showToast(UionQrcodeDataReplenishActivity.this, "付款金额单位过小");
            return;
        }
        if (moneyStr.replace(",", "").replace("￥", "")
                .length() > 9) {
            LogUtil.showToast(UionQrcodeDataReplenishActivity.this, "付款金额超限");
            return;
        }
        if (TextUtils.isEmpty(accountNo)) {
            LogUtil.showToast(UionQrcodeDataReplenishActivity.this, "请选择收款账户");
            return;
        }
        unionQrcodePayReq(moneyStr);
    }

    /**
     * 银联支付
     */
    public void unionQrcodePayReq(String moneyStr){
        qtpayApplication.setValue("SaomaSMZF003.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("code",code));
        qtpayParameterList.add(new Param("amount", MoneyEncoder.encodeToPost(moneyStr)));
        qtpayParameterList.add(new Param("cardIdx", cardIdx));
        httpsPost("SaomaSMZF003Tag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    JSONObject jsonObject=new JSONObject(payResult.getData());
                    String msg=   JsonUtil.getValueFromJSONObject(jsonObject,"msg");
                    String code=   JsonUtil.getValueFromJSONObject(jsonObject,"code");
                    if("0000".equals(code)){
                        //交易完成
                        showMsgDialog(msg,true);
                    }else if("200002".equals(code)){
                        //交易结果未知弹出框进行轮询
                        JSONObject resultObj=   JsonUtil.getJSONObjectFromJsonObject(jsonObject,"result");
                        String orderId=  JsonUtil.getValueFromJSONObject(resultObj,"orderid");
                        showLoadingStateDialog(orderId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.showToast(UionQrcodeDataReplenishActivity.this,"交易结果异常,请查看交易记录!");
                }
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                finish();
            }
        });

    }


    /**
     * 展示提示信息退出对话框
     * @param message
     */
    @Override
    public void showMsgDialog(String message,boolean isOnlyOk){
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(UionQrcodeDataReplenishActivity.this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                finish();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {

                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent(message);
        ryxSimpleConfirmDialog.setCancelable(false);
        ryxSimpleConfirmDialog.setOkbtnText("退出");
        ryxSimpleConfirmDialog.setCancelbtnText("重试");
        if(isOnlyOk){
            ryxSimpleConfirmDialog.setOnlyokLinearlayout();
        }
    }
}

