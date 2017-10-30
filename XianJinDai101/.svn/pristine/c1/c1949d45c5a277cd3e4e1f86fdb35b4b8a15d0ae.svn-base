package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * 购买瑞豆
 */
public class RuiBeanPayActivity extends BaseActivity {
    TextView ruibean_count_tv,ruibean_money_tv;
    String beansType,discountamountval;
    AutoRelativeLayout qrcodepay_layout,quickpay_layout,cardswiper_layout;
    OrderInfo orderInfo= Order.RUIBEAN_PAY;
    private int TOSWIPERPAY=0x0021;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rui_bean_pay);
        setTitleLayout("支付",true,false);

        initQtPatParams();
        initViewIds();
        intViewData();
    }

    private void initViewIds() {
        ruibean_count_tv=(TextView) findViewById(R.id.ruibean_count_tv);
        ruibean_money_tv=(TextView)findViewById(R.id.ruibean_money_tv);
        qrcodepay_layout=(AutoRelativeLayout) findViewById(R.id.qrcodepay_layout);
        quickpay_layout=(AutoRelativeLayout) findViewById(R.id.quickpay_layout);
        cardswiper_layout=(AutoRelativeLayout) findViewById(R.id.cardswiper_layout);
        qrcodepay_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                LogUtil.showToast(RuiBeanPayActivity.this,"暂不支持");
            }
        });
        quickpay_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                LogUtil.showToast(RuiBeanPayActivity.this,"暂不支持");
            }
        });
        cardswiper_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                showMsgDialog("温馨提示:瑞豆一旦购买,不支持退货和提现", new CompleteResultListen() {
                    @Override
                    public void compleResultok() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                requestOrder();
                            }
                        });
                    }
                });

//                LogUtil.showToast(RuiBeanPayActivity.this,beansType);
            }
        });
    }
    /**
     * 展示提示信息退出对话框
     * @param message
     */
    public void showMsgDialog(String message, final CompleteResultListen completeResultListen){
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(RuiBeanPayActivity.this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                completeResultListen.compleResultok();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent(message);
        ryxSimpleConfirmDialog.setCancelable(false);
        ryxSimpleConfirmDialog.setOkbtnText("确定");
        ryxSimpleConfirmDialog.setCancelbtnText("取消");
        ryxSimpleConfirmDialog.setContentgravity(Gravity.CENTER);
    }


    private void intViewData() {
        Intent intent=getIntent();
        beansType=  intent.getStringExtra("beansType");
        discountamountval=  intent.getStringExtra("discountamountval");
        orderInfo.setRealAmt(discountamountval);
        orderInfo.setInterfaceTag("15");
      String   count=  intent.getStringExtra("count");
       String discountamount=  intent.getStringExtra("discountamount");
        ruibean_count_tv.setText(count);
        ruibean_money_tv.setText(discountamount);
    }

    public void requestOrder(){
        qtpayApplication.setValue("RequestOrder.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("merchantId",orderInfo.getMerchantId()));
        qtpayParameterList.add(new Param("productId",orderInfo.getProductId()));
        qtpayParameterList.add(new Param("beansType",beansType));
        qtpayParameterList.add(new Param("orderAmt",discountamountval));
        qtpayParameterList.add(new Param("payTool","01"));
        qtpayParameterList.add(new Param("orderDesc","瑞豆"));
        qtpayParameterList.add(new Param("orderRemark","购买瑞豆"));
        httpsPost("RequestOrderTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {

                orderInfo.setOrderId(payResult.getOrderId());
                orderInfo.setRealAmt(payResult.getRealAmt());
                orderInfo.setOrderAmt(payResult.getRealAmt());
                Intent intent = new Intent(RuiBeanPayActivity.this, Swiper_.class);
                intent.putExtra("orderinfo", orderInfo);
                startActivityForResult(intent,TOSWIPERPAY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
