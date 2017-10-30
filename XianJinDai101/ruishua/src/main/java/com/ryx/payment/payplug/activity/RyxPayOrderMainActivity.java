package com.ryx.payment.payplug.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryx.payment.payplug.base.PayPlugBaseActivity;
import com.ryx.payment.payplug.bean.SerializableMap;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.RyxApplication;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.swiper.utils.MapUtil;
import com.ryx.swiper.utils.MoneyEncoder;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

import static com.ryx.payment.ruishua.RyxAppconfig.CLOSE_AT_SWIPER;

@EActivity(R.layout.activity_ryx_pay_order_main)
public class RyxPayOrderMainActivity extends PayPlugBaseActivity {
    @ViewById
    TextView tv_productname,tv_phonenumber,tv_ordermoney,tv_orderid;
    @ViewById
    RelativeLayout ryxpay_relayout_1,ryxpay_relayout_2,ryxpay_relayout_3,ryxpay_relayout_4,ryxpay_relayout_5;
    private int SHUAKAPAYCODE=0x0001;
    private int QUICKPAYMENTREQCODE=0x0002;
    private int WEIXINQRCODEPAYMENTREQCODE=0x0003;
    private int ZHIFUBAOQRCODEPAYMENTREQCODE=0x0004;
    private int UNIONQRCODEPAYMENTREQCODE=0x0005;
    String reqCode;
    public Map<String,String> reqMap;
    LocalBroadcastManager localBroadcastManager;
    @ViewById(R.id.paywaylayout)
    AutoLinearLayout paywaylayout;
    @ViewById(R.id.orderlayout)
    AutoLinearLayout orderlayout;
private RyxPayResult ryxPayResult;
//    private int QUICKPAYMENTREQCODE=0x0001;
//    private int QUICKPAYMENTREQCODE=0x0001;
    @AfterViews
    public void initview(){
        paywaylayout.setVisibility(View.GONE);
        orderlayout.setVisibility(View.GONE);

        localBroadcastManager=  LocalBroadcastManager.getInstance(this);
        setTitleLayout(RyxAppdata.getInstance(this).getCurrentBranchName()+"收款台",true,false);
//        RyxAppdata.getInstance(this).glideLoadqrcodeimgmageViewForBranch(getRightImgView());
        reqCode= getIntent().getStringExtra("reqCode");
        Bundle bundle = getIntent().getExtras();
        SerializableMap serializableMap = (SerializableMap) bundle.get("map");
        reqMap= serializableMap.getMap();
       String ordermoney= MapUtil.getString(reqMap,"money");
        String industId= MapUtil.getString(reqMap,"industId");
        String industUserId= MapUtil.getString(reqMap,"industUserId");

       initQtPatParams();
        qtpayApplication.setValue("IndustrySetOrder.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("industId",industId));
        qtpayParameterList.add(new Param("industUserId",industUserId));
        qtpayParameterList.add(new Param("merchantId","0001000009"));
        qtpayParameterList.add(new Param("productId","0000000000"));
        qtpayParameterList.add(new Param("orderDesc","6225684321000099630"));
        qtpayParameterList.add(new Param("orderAmt",ordermoney));
        qtpayParameterList.add(new Param("payTool","01"));

        httpsPost("IndustrySetOrderTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                ryxPayResult=payResult;
                String productname= MapUtil.getString(reqMap,"desc");
                tv_productname.setText(productname);
                String phonenumber= MapUtil.getString(reqMap,"phoneNumber");
                tv_phonenumber.setText(phonenumber);
                tv_ordermoney.setText(MoneyEncoder.decodeFormat(payResult.getRealAmt()));
                tv_orderid.setText(payResult.getOrderId());
                paywaylayout.setVisibility(View.VISIBLE);
                orderlayout.setVisibility(View.VISIBLE);
            }
        });


//        String reqCode= getIntent().getStringExtra("reqCode");
//        Intent intent1 = new Intent();
//        intent1.setAction("com.ryxpay.payplug.server");
//        intent1.putExtra("reqCode", reqCode);
//        intent1.putExtra("code", "0000");
//        intent1.putExtra("msg", "");
//        intent1.putExtra("result", "结果成功");
//        sendBroadcast(intent1);
    }

    @Click(R.id.ryxpay_relayout_1)
    public void cardSwiperPay(){
        if(ryxPayResult==null){
            LogUtil.showToast(RyxPayOrderMainActivity.this,"订单信息有误");
            return;
        }
        Intent intent=new Intent(this,RyxPaySwiperActivity_.class);
        Bundle bundle=new Bundle();
        OrderInfo orderInfo=new OrderInfo();
            orderInfo.setOrderAmt(ryxPayResult.getOrderAmt());
            orderInfo.setRealAmt(ryxPayResult.getRealAmt());
            orderInfo.setOrderId(ryxPayResult.getOrderId());
            orderInfo.setMerchantId("0001000009");
            orderInfo.setProductId("0000000000");
             String industId= MapUtil.getString(reqMap,"industId");
            orderInfo.setIndustId(industId);
            String industUserId= MapUtil.getString(reqMap,"industUserId");
            orderInfo.setIndustUserId(industUserId);
            orderInfo.setNeedSign(true);
        orderInfo.setInterfaceTag("13");
        bundle.putSerializable("orderinfo",orderInfo);
        intent.putExtras(bundle);
        startActivityForResult(intent,SHUAKAPAYCODE);
    }
    @Click(R.id.ryxpay_relayout_2)
    public void cardQuickPay(){
        LogUtil.showToast(RyxPayOrderMainActivity.this,"快捷支付暂不支持");
//        Intent intent=new Intent(this,QuickPaymentActivity_.class);
//        startActivityForResult(intent,QUICKPAYMENTREQCODE);
    }
    @Click(R.id.ryxpay_relayout_3)
    public void weixinqrcodePay(){
//        LogUtil.showToast(RyxPayOrderMainActivity.this,"微信二维码支付");
        Intent intent=new Intent(this,WeiXinQrcodePayActivity_.class);
        intent.putExtra("reqCode",reqCode);
        final SerializableMap myMap=new SerializableMap();
        myMap.setMap(reqMap);//将map数据添加到封装的myMap中,当前仅为测试暂时放源数据
        Bundle bundle=new Bundle();
        bundle.putSerializable("map", myMap);
        intent.putExtras(bundle);
        startActivityForResult(intent,WEIXINQRCODEPAYMENTREQCODE);
    }
    @Click(R.id.ryxpay_relayout_4)
    public void zfbqrcodePay(){
//        LogUtil.showToast(RyxPayOrderMainActivity.this,"支付宝二维码支付");
        Intent intent=new Intent(this,ZfbQrcodeActivity_.class);
        intent.putExtra("reqCode",reqCode);
        final SerializableMap myMap=new SerializableMap();
        myMap.setMap(reqMap);//将map数据添加到封装的myMap中,当前仅为测试暂时放源数据
        Bundle bundle=new Bundle();
        bundle.putSerializable("map", myMap);
        intent.putExtras(bundle);
        startActivityForResult(intent,ZHIFUBAOQRCODEPAYMENTREQCODE);
    }
    @Click(R.id.ryxpay_relayout_5)
    public void yinlianqrcodePay(){
//        LogUtil.showToast(RyxPayOrderMainActivity.this,"银联二维码支付");
        Intent intent=new Intent(this,UnionQrcodePayActivity_.class);
        intent.putExtra("reqCode",reqCode);
        final SerializableMap myMap=new SerializableMap();
        myMap.setMap(reqMap);//将map数据添加到封装的myMap中,当前仅为测试暂时放源数据
        Bundle bundle=new Bundle();
        bundle.putSerializable("map", myMap);
        intent.putExtras(bundle);
        startActivityForResult(intent,UNIONQRCODEPAYMENTREQCODE);
    }

    @Override
    protected void backUpImgOnclickListen() {
        showExitDialog();
    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {

        switch (keycode) {
            case KeyEvent.KEYCODE_BACK: {
                showExitDialog();
                return true;
            }
            default:
                return super.onKeyDown(keycode, event);
        }
    }
    private void showExitDialog() {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                sendBackMsg("0001","用户操作取消","");
               finishSelf();
            }
            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("确定要离开收款台?");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(QUICKPAYMENTREQCODE==requestCode&&resultCode==CLOSE_AT_SWIPER){
            //快捷支付结果
            sendBackMsg("0002","交易成功","");
            finishSelf();
        }
    }
    /**
     * 返回给调用方信息
     * @param code
     * @param msg
     * @param result
     */
    private void sendBackMsg(String code,String msg,String result){
        Intent intent1 = new Intent();
        intent1.setAction("com.ryxpay.payplug.server");
        intent1.putExtra("reqCode", reqCode);
        intent1.putExtra("code",code);
        intent1.putExtra("msg", msg);
        intent1.putExtra("result", result);
        if(localBroadcastManager!=null){
            localBroadcastManager.sendBroadcast(intent1);
        }else{
            LogUtil.showToast(RyxPayOrderMainActivity.this,"广播管理异常！");
        }
    }

    @Override
    protected void onDestroy() {
        Intent intentDestroy = new Intent();
        intentDestroy.setAction("com.ryxpay.payplug.server");
        intentDestroy.putExtra("reqCode", reqCode);
        intentDestroy.putExtra("code","unregisterReceiver");
        if(localBroadcastManager!=null){
            localBroadcastManager.sendBroadcast(intentDestroy);
        }else{
            LogUtil.showToast(RyxPayOrderMainActivity.this,"广播管理异常！");
        }
        super.onDestroy();
    }

    /**
     * 保证广播发送接收完毕，延迟finish
     */
    private void finishSelf(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        RyxApplication ryxApplication= RyxApplication.getInstance();
                        ryxApplication.removeActivity(RyxPayOrderMainActivity.this);
                        ryxApplication.finishAllActivity();
                    }
                });
            }
        }).start();
    }
   @Override
    public void upRequestParams(){
       super.upRequestParams();
       if("IndustrySetOrder.Req".equals(qtpayApplication.getValue())){
           //创建订单
           qtpayParameterList.add(new Param("mobileNo","18769798020"));
           qtpayAttributeList.add(new Param("phone","18769798020"));
       }

   }
}
