package com.ryx.payment.ruishua.convenience;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.view.FingerPaintView;
import com.ryx.swiper.utils.CryptoUtils;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@EActivity(R.layout.activity_order_signature)
public class OrderSignature extends BaseActivity implements View.OnClickListener {
    ImageView iv_left; // 左上的关闭图标
    FingerPaintView fingerpaint;
    TextView tv_money; // 显示交易金额
    TextView tv_moneyrate; // 显示交易金额手续费
    boolean queren = false; // 二次确认时用

    String picurl; // 签名截图保存的路径
    Intent intent; // 跳转
    OrderInfo orderinfo;
    String cardInfo;
    Bitmap bitmap; // 签名图片
    byte[] bitmapBytes;
    Param qtpayMerchantId = new Param("merchantId", "000000");
    Param qtpayOrderId = new Param("orderId", RyxAppconfig.QTNET_SUCCESS);
    Param qtpayLongitude; // 经度
    Param qtpayLatitude; // 纬度
    TextView tv_moneyrate_display;
    String orderid="",cardpsw="",icdata="",serialnum="",cardtype="",translognumber="",rateStr="",moneyratedisplay="";
    @AfterViews
    public void initMyData(){
        try {
            orderinfo = (OrderInfo) getIntent().getExtras().get("orderinfo");
            cardInfo= getIntent().getExtras().getString("cardInfo");
            orderid= getIntent().getExtras().getString("orderid");
            rateStr= getIntent().getExtras().getString("rateStr");
            moneyratedisplay= getIntent().getExtras().getString("moneyratedisplay","");
            cardpsw= getIntent().getExtras().getString("cardpsw");
            icdata= getIntent().getExtras().getString("icdata");
            serialnum= getIntent().getExtras().getString("serialnum");
            cardtype= getIntent().getExtras().getString("cardtype");
            baseprovinceid= getIntent().getExtras().getString("baseprovinceid");
            translognumber= getIntent().getExtras().getString("translognumber");
            initView();
            initFingerView();
            initQtPatParams();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LogUtil.showToast(OrderSignature.this, "参数传递异常");
            e.printStackTrace();
        }

    }
    public void initView() {
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_moneyrate = (TextView) findViewById(R.id.tv_moneyrate);
        tv_moneyrate_display = (TextView) findViewById(R.id.tv_moneyrate_display);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_money.setText(MoneyEncoder.decodeFormat(orderinfo.getRealAmt())
                .replace("￥", ""));
        if(TextUtils.isEmpty(rateStr)){
            tv_moneyrate.setVisibility(View.GONE);
        }else{
            tv_moneyrate.setVisibility(View.VISIBLE);
            tv_moneyrate.setText(rateStr);
        }
        if(TextUtils.isEmpty(moneyratedisplay)){
            tv_moneyrate_display.setVisibility(View.GONE);
        }else{
            tv_moneyrate_display.setVisibility(View.VISIBLE);
            tv_moneyrate_display.setText(moneyratedisplay);
        }
        iv_left.setOnClickListener(OrderSignature.this);
        fingerpaint = (FingerPaintView) findViewById(R.id.fingerpaint);

    }
    /**
     * 初始化网络请求参数
     */
    public void initQtPatParams() {
        // TODO Auto-generated method stub
        super.initQtPatParams();
        CryptoUtils.getInstance().setTransLogUpdate(true);// translogno 更新
        qtpayApplication = new Param("application", "UserSignatureUpload.Req");
    }
    public void initFingerView() {
        fingerpaint.setConfirm(false);
        switch (orderinfo.getId()) {
            case RyxAppconfig.PHONE_RECHARGE: // 手机充值
            case RyxAppconfig.CREDITCARD_REPAYMENT: // 信用卡还款
            case RyxAppconfig.TRANSFER_REPAYMENT: // 转帐还款
            case RyxAppconfig.COMMON_WITHDRAW: // 手机号普通提款
            case RyxAppconfig.QUICK_WITHDRAW: // 手机号快速提款
//                if (RyxAppconfig.BRANCH.equals("01")) {
//                    fingerpaint.setMenuText((MessageFormat.format(getResources()
//                                    .getString(R.string.signature_notice), getResources()
//                                    .getString(R.string.app_name))),getCurrentTime(),
//                            getResources().getString(R.string.clear_signature),
//                            "支付确认", R.drawable.bg_anniu_blank,
//                            R.drawable.bg_anniu_blue);
//                }else if (RyxAppconfig.BRANCH.equals("02")) {
//                    fingerpaint.setMenuText((MessageFormat.format(getResources()
//                                    .getString(R.string.signature_notice), getResources()
//                                    .getString(R.string.app_name_ryx))),getCurrentTime(),
//                            getResources().getString(R.string.clear_signature),
//                            "支付确认", R.drawable.bg_anniu_blank,
//                            R.drawable.bg_anniu_blue);
//                }
                fingerpaint.setMenuText((MessageFormat.format(getResources()
                                    .getString(R.string.signature_notice), RyxAppdata.getInstance(this).getCurrentBranchName())),getCurrentTime(),
                            getResources().getString(R.string.clear_signature),
                            "支付确认", R.drawable.bg_anniu_blank,
                            R.drawable.bg_anniu_blue);
                break;

            case RyxAppconfig.IMPAY_FREE:
            case RyxAppconfig.FACE_OF_RECEIVABLES: // 当面收款
//                if (RyxAppconfig.BRANCH.equals("01")) {
//                    fingerpaint.setMenuText((MessageFormat.format(getResources()
//                                    .getString(R.string.signature_notice), getResources()
//                                    .getString(R.string.app_name))),getCurrentTime(),
//                            getResources().getString(R.string.clear_signature),
//                            "支付确认", R.drawable.bg_anniu_blank,
//                            R.drawable.bg_anniu_blue);
//                }else if (RyxAppconfig.BRANCH.equals("02")) {
//                    fingerpaint.setMenuText((MessageFormat.format(getResources()
//                                    .getString(R.string.signature_notice), getResources()
//                                    .getString(R.string.app_name_ryx))),getCurrentTime(),
//                            getResources().getString(R.string.clear_signature),
//                            "支付确认", R.drawable.bg_anniu_blank,
//                            R.drawable.bg_anniu_blue);
//                }
                fingerpaint.setMenuText((MessageFormat.format(getResources()
                                    .getString(R.string.signature_notice), RyxAppdata.getInstance(this).getCurrentBranchName())),getCurrentTime(),
                            getResources().getString(R.string.clear_signature),
                            "支付确认", R.drawable.bg_anniu_blank,
                            R.drawable.bg_anniu_blue);
                break;

            case RyxAppconfig.GOODS_RECEIVABLES: // 商品销售
                fingerpaint.setMenuText((MessageFormat.format(getResources()
                                    .getString(R.string.signature_notice), RyxAppdata.getInstance(this).getCurrentBranchName())),getCurrentTime(),
                            getResources().getString(R.string.clear_signature),
                            "支付确认", R.drawable.bg_anniu_blank,
                            R.drawable.bg_anniu_blue);
//                if (RyxAppconfig.BRANCH.equals("01")) {
//                    fingerpaint.setMenuText((MessageFormat.format(getResources()
//                                    .getString(R.string.signature_notice), getResources()
//                                    .getString(R.string.app_name))),getCurrentTime(),
//                            getResources().getString(R.string.clear_signature),
//                            "支付确认", R.drawable.bg_anniu_blank,
//                            R.drawable.bg_anniu_blue);
//                }else if (RyxAppconfig.BRANCH.equals("02")) {
//                    fingerpaint.setMenuText((MessageFormat.format(getResources()
//                                    .getString(R.string.signature_notice), getResources()
//                                    .getString(R.string.app_name_ryx))),getCurrentTime(),
//                            getResources().getString(R.string.clear_signature),
//                            "支付确认", R.drawable.bg_anniu_blank,
//                            R.drawable.bg_anniu_blue);
//                }
                break;

            default:
                fingerpaint.setMenuText((MessageFormat.format(getResources()
                                    .getString(R.string.signature_notice), RyxAppdata.getInstance(this).getCurrentBranchName())),getCurrentTime(),
                            getResources().getString(R.string.clear_signature),
                            "支付确认", R.drawable.bg_anniu_blank,
                            R.drawable.bg_anniu_blue);
//                if (RyxAppconfig.BRANCH.equals("01")) {
//                    fingerpaint.setMenuText((MessageFormat.format(getResources()
//                                    .getString(R.string.signature_notice), getResources()
//                                    .getString(R.string.app_name))),getCurrentTime(),
//                            getResources().getString(R.string.clear_signature),
//                            "支付确认", R.drawable.bg_anniu_blank,
//                            R.drawable.bg_anniu_blue);
//                }else if (RyxAppconfig.BRANCH.equals("02")) {
//
//                    fingerpaint.setMenuText((MessageFormat.format(getResources()
//                                    .getString(R.string.signature_notice), getResources()
//                                    .getString(R.string.app_name_ryx))),getCurrentTime(),
//                            getResources().getString(R.string.clear_signature),
//                            "支付确认", R.drawable.bg_anniu_blank,
//                            R.drawable.bg_anniu_blue);
//                }
                break;
        }
        setFingerView();
    }
    public void setFingerView() {
        fingerpaint.SetLeftClick(new FingerPaintView.FingerPaintClickListener() {

            @Override
            public void onClick(View iv) {
                // TODO Auto-generated method stub
                if (fingerpaint.isSigned()) {
                    fingerpaint.clearSign(); // 清除签名
                    fingerpaint.setSigned(false);
                    if (fingerpaint.isConfirm()) {// 重新签名
                        initFingerView();
                    }
                } else {
                    LogUtil.showToast(OrderSignature.this, "请先签名");
                }
            }
        });

        fingerpaint.SetRightClick(new FingerPaintView.FingerPaintClickListener() {

            @Override
            public void onClick(View iv) {
                // TODO Auto-generated method stub
                if (fingerpaint.isSigned()) {
                    fingerpaint.setlockSignature(true);
                    switch (orderinfo.getId()) {
                        case RyxAppconfig.GOODS_RECEIVABLES: // 商品销售
                        case RyxAppconfig.FACE_OF_RECEIVABLES: // 当面收款
//						if (!fingerpaint.isConfirm()) {
//							fingerpaint.setConfirm(true);
//							fingerpaint.setAfterConfirm(getResources()
//									.getString(R.string.re_signature), "掌柜确认",
//									R.drawable.bg_anniu_blue);
//							LOG.showToast(OrderSignature.this, "消费者已经确认");
//							if(fingerpaint.isNeedTimer()){
//								fingerpaint.startCountdown();
//							}
//						}else {
                            sentSignRequisitons();
//						}

                            break;

                        case RyxAppconfig.PHONE_RECHARGE: // 手机充值
                        case RyxAppconfig.CREDITCARD_REPAYMENT: // 信用卡还款
                        case RyxAppconfig.TRANSFER_REPAYMENT: // 转帐还款
                        case RyxAppconfig.COMMON_WITHDRAW: // 手机号普通提款
                        case RyxAppconfig.QUICK_WITHDRAW: // 手机号快速提款
                            sentSignRequisitons();
                            break;

                        default:
//						if (!fingerpaint.isConfirm()) {
//							fingerpaint.setConfirm(true);
//							fingerpaint.setAfterConfirm(getResources()
//									.getString(R.string.re_signature), "掌柜确认",
//									R.drawable.bg_anniu_blue);
//							LOG.showToast(OrderSignature.this, "消费者已经确认");
//						} else {
                            sentSignRequisitons();
//						}
                            break;
                    }
                } else {

                    LogUtil.showToast(OrderSignature.this, "请先签名");
                }

            }
        });
    }
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    uploadSign();
                    break;
            }
        }

    };
    public void uploadSign() {
        if (baselongitude == null || baselongitude.length() == 0) {
            baselongitude = "";
        }
        if (baselatitude == null || baselatitude.length() == 0) { // 如果获取不到的时候就给一个默认值
            baselatitude = "";
        }
        Param qtpayMerchantId = new Param("merchantId",
                orderinfo.getMerchantId());
        Param qtpayOrderId = new Param("orderId", orderinfo.getOrderId());
        qtpayLongitude = new Param("longitude", baselongitude);
        qtpayLatitude = new Param("latitude", baselatitude);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayLongitude); // 经度
        qtpayParameterList.add(qtpayLatitude); // 纬度
        qtpayParameterList.add(qtpayMerchantId);
        qtpayParameterList.add(qtpayOrderId);
        qtpayParameterList.add(new Param("signPicAscii", BitmapUntils
                .changeBytesToHexString(bitmapBytes)));
        qtpayParameterList.add(new Param("picSign", CryptoUtils.getInstance()
                .EncodeDigest(bitmapBytes)));
            httpsPost("uploadSignTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    byte[] signarray = BitmapUntils.bitmapToArray(bitmap);
                    Intent intent = new Intent(OrderSignature.this, Swiper.class);
                    intent.putExtra("signarray", signarray);
                    intent.putExtra("longitude", baselongitude);
                    intent.putExtra("latitude", baselatitude);
                    intent.putExtra("cardInfo", cardInfo);

                    intent.putExtra("orderinfo", orderinfo);
                    intent.putExtra("orderid", orderid);
                    intent.putExtra("cardpsw", cardpsw);
                    intent.putExtra("icdata", icdata);
                    intent.putExtra("serialnum", serialnum);
                    intent.putExtra("cardtype", cardtype);
                    intent.putExtra("baseprovinceid", baseprovinceid);
                    intent.putExtra("translognumber", translognumber);
                    setResult(RyxAppconfig.QT_RESULT_OK, intent);
                    OrderSignature.this.finish();
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
    public void sentSignRequisitons() {
        // String picurl =fingerpaint.SaveAsFile();
        // LOG.showToast(QianMing.this, "签名文件已经保存到"+picurl);
        bitmap = fingerpaint.SaveAsBitmap(); // 保存图片
        showLoading();
        new Thread(new Runnable() {

            @Override
            public void run() {
                bitmapBytes = BitmapUntils.getBitmapByte(bitmap);
                mHandler.sendEmptyMessage(1);
            }
        }).start();

    }
    public String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);// 设置日期格式
        return df.format(new Date());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_left:
                setResult(RyxAppconfig.QT_RESULT_SIGN_CANCEL);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {

        switch (keycode) {
            case KeyEvent.KEYCODE_BACK: {
                setResult(RyxAppconfig.QT_RESULT_SIGN_CANCEL);
                finish();
                return true;
            }
            default:
                return super.onKeyDown(keycode, event);
        }
    }
}
