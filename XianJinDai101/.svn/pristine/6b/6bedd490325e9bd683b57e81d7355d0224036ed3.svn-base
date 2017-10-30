package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import rm.com.clocks.ClockImageView;

@EActivity(R.layout.activity_pay_result)
public class ScanningPayResultActivity extends BaseActivity {

    @ViewById
    TextView tv_status;
    @ViewById
    TextView tv_info;
    @ViewById
    ImageView img_status;
    @ViewById
    ClockImageView clocks;

    public String orderId;//订单编号
    Timer loadingStatetimer = new Timer();//请求计时器
    public String amount;//交易金额
    String flag="";
    OrderInfo orderInfo;

    @AfterViews
    public void initViews() {
        setTitleLayout("交易结果", true, false);
        initQtPatParams();
        Intent intent=getIntent();
        if(intent.hasExtra("flag")){
            flag= intent.getStringExtra("flag");
        }

        if("swiper".equals(flag)){
            orderInfo=(OrderInfo) intent.getSerializableExtra("orderinfo");
            orderId=orderInfo.getOrderId();
            amount = MoneyEncoder.decodeFormat(orderInfo.getOrderAmt());
        }else{
            if (intent.hasExtra("orderId")) {
                orderId = intent.getStringExtra("orderId");
            }
            if (intent.hasExtra("amount")) {
                amount = intent.getStringExtra("amount");
            }
            if(!TextUtils.isEmpty(amount)){
                amount = MoneyEncoder.decodeFormat(amount);
            }
        }
        showLoadingStateDialog();

    }

    //点击了返回按钮，关闭计时器，并且关闭页面，此处还应该加上关闭扫码页面
    @Override
    public void backUpImgOnclickListen() {
        if("swiper".equals(flag)){
            setResult(RyxAppconfig.CLOSE_SWIPER_SHOWIMPAY);
        }
        loadingStatefinishSelf();
    }

    /**
     * 展示提示信息退出对话框
     *
     * @param message
     */
    public void showMsgDialog(String message, boolean isOnlyOk) {
    }

    /**
     * 展示等待处理结果
     */
    public void showLoadingStateDialog() {
        clocks.animateIndeterminate();
        tv_status.setText("处理中");
        tv_info.setTextColor(Color.BLACK);
        //如果金额不为空，显示交易金额
        if (!TextUtils.isEmpty(amount)) {
            tv_info.setText(amount);
        }
        if (loadingStatetimer != null) {
            loadingStatetimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            }, 3000);
        }
    }

    /**
     * 关闭当前activity
     */
    public void loadingStatefinishSelf() {
        try {
            loadingStatetimer.cancel();
            loadingStatetimer = null;
        } catch (Exception e) {
        }
        finish();
    }

    public void initiativeSaoMaPayStateCheckTag(String orderId, final int number) {
        if("swiper".equals(flag)){
            qtpayApplication.setValue("SwipToKJZFChecked.Req");
        }else{
            qtpayApplication.setValue("InitiativeSaoMaPayStateCheck.Req");
        }
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("orderId", orderId));
        httpsPost(false, false, "InitiativeSaoMaPayStateCheckTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {

                try {
                    JSONObject jsonObject = new JSONObject(payResult.getData());
                    String code = JsonUtil.getValueFromJSONObject(jsonObject, "code");
                    String msg = JsonUtil.getValueFromJSONObject(jsonObject, "msg");
                    if (number == 1 && "200002".equals(code) && loadingStatetimer != null) {
                        loadingStatetimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(2);
                            }
                        }, 10000);
                    } else if (number == 2 && "200002".equals(code)) {
                        //第二次查询无结果
                        setImgState(3, msg);

                    } else if ("0000".equals(code)) {
                        LogUtil.showToast(ScanningPayResultActivity.this, msg);
                        setImgState(2, msg);
                    } else {
                        LogUtil.showToast(ScanningPayResultActivity.this, msg);
                        showMsgDialog(msg, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                if (loadingStatetimer != null) {
                    loadingStatetimer.cancel();
                    loadingStatetimer = null;
                }
                setImgState(3, "查询结果失败,请稍后再试");
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                if (loadingStatetimer != null) {
                    loadingStatetimer.cancel();
                    loadingStatetimer = null;
                }
                setImgState(3, "查询结果失败,请稍后再试");
            }

            @Override
            public void onLoginAnomaly() {
                super.onLoginAnomaly();
                if (loadingStatetimer != null) {
                    loadingStatetimer.cancel();
                    loadingStatetimer = null;
                }
                setImgState(3, "查询结果失败,请稍后再试");
            }

        });
    }

    /**
     * 动态设置处理状态
     *
     * @param code 1等待处理中,2处理OK 3处理失败
     */
    public void setImgState(int code, String msg) {
        if (code == 1) {
            clocks.animateIndeterminate();
            img_status.setVisibility(View.GONE);
            tv_status.setText("处理中");
            tv_info.setTextColor(Color.BLACK);
            if (!TextUtils.isEmpty(amount)) {
            tv_info.setText(amount);}
        } else if (code == 2) {
            clocks.stop();
            clocks.setVisibility(View.GONE);
            img_status.setVisibility(View.VISIBLE);
            img_status.setBackgroundResource(R.drawable.circle_success);
            tv_status.setText("交易成功");
            tv_info.setTextColor(Color.parseColor("#1db7f0"));
            if (!TextUtils.isEmpty(amount)) {
            tv_info.setText(amount);}
            if("swiper".equals(flag)){
                setResult(REQUEST_SUCCESS);
                loadingStatefinishSelf();
            }

        } else if (code == 3) {
            clocks.stop();
            clocks.setVisibility(View.GONE);
            img_status.setVisibility(View.VISIBLE);
            img_status.setBackgroundResource(R.drawable.circle_fail);
            tv_status.setText(msg);
            tv_info.setTextColor(Color.BLACK);
            if (!TextUtils.isEmpty(amount)) {
            tv_info.setText(amount);}
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                initiativeSaoMaPayStateCheckTag(orderId, msg.what);
            } else if (msg.what == 2) {
                initiativeSaoMaPayStateCheckTag(orderId, msg.what);
            }
            super.handleMessage(msg);
        }

        ;
    };

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        switch (keycode) {
            case KeyEvent.KEYCODE_BACK: {
                if("swiper".equals(flag)){
                    setResult(RyxAppconfig.CLOSE_SWIPER_SHOWIMPAY);
                }
                loadingStatefinishSelf();
                return true;
            }
            default:
                return super.onKeyDown(keycode, event);
        }
    }
}
