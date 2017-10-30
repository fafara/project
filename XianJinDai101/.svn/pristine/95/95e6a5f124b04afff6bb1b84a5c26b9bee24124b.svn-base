package com.ryx.payment.ruishua.convenience;

import android.os.Handler;
import android.os.Message;

import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.LoadingStateDialog;
import com.ryx.payment.ruishua.listener.LoadingStateDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 快捷支付和主扫码支付异步处理BaseActivity
 */
public class PayStateCheckBaseActivity extends BaseActivity {
    String orderId;
    LoadingStateDialog loadingStateDialog;
    Timer loadingStatetimer = new Timer();
    /**
     * 展示提示信息退出对话框
     * @param message
     */
    public void showMsgDialog(String message,boolean isOnlyOk){}
    /**
     * 展示等待处理结果
     */
    public void showLoadingStateDialog(String orderId){
        loadingStateDialog=new LoadingStateDialog(PayStateCheckBaseActivity.this, new LoadingStateDialogListener() {
            @Override
            public void onPositiveActionClicked(LoadingStateDialog loadingStateDialog) {
                loadingStateDialog.dismiss();
                try {
                    loadingStatetimer.cancel();
                    loadingStatetimer=null;
                }catch (Exception e){
                }
                loadingStatefinishSelf();
            }
        });
        loadingStateDialog.show();
        loadingStateDialog.setStatusContent("交易处理中,请耐心等待处理结果...");
        this.orderId=orderId;
        if(loadingStatetimer!=null){
            loadingStatetimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            }, 3000);
        }
        loadingStateDialog.setCancelable(false);
    }

    /**
     * 关闭当前activity
     */
    public void loadingStatefinishSelf(){
        finish();
    };
    public void initiativeSaoMaPayStateCheckTag(String orderId, final int number ){
        qtpayApplication.setValue("InitiativeSaoMaPayStateCheck.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("orderId",orderId));
        httpsPost(false, false, "InitiativeSaoMaPayStateCheckTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {

                try {
                    JSONObject jsonObject=new JSONObject(payResult.getData());
                    String code=  JsonUtil.getValueFromJSONObject(jsonObject,"code");
                    String msg=  JsonUtil.getValueFromJSONObject(jsonObject,"msg");
                    if(number==1&&"200002".equals(code)&&loadingStatetimer!=null){
                        loadingStatetimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(2);
                            }
                        }, 10000);
                    }else if(number==2&&"200002".equals(code)){
                        //第二次查询无结果
                        if(loadingStateDialog!=null){
                            loadingStateDialog.setImgState(3);
                            loadingStateDialog.setStatusContent(msg);
                        }

                    }else if("0000".equals(code)){
                        LogUtil.showToast(PayStateCheckBaseActivity.this,msg);
                        if(loadingStateDialog!=null){
                            loadingStateDialog.setImgState(2);
                            loadingStateDialog.setStatusContent(msg);
                        }
                    }
                    else{
                        LogUtil.showToast(PayStateCheckBaseActivity.this,msg);
                        showMsgDialog(msg,false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                if(loadingStatetimer!=null){
                    loadingStatetimer.cancel();
                    loadingStatetimer=null;
                }
                if(loadingStateDialog!=null){
                    loadingStateDialog.setImgState(3);
                    loadingStateDialog.setStatusContent("查询结果失败,请稍后再试");
                }
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                if(loadingStatetimer!=null){
                    loadingStatetimer.cancel();
                    loadingStatetimer=null;
                }
                if(loadingStateDialog!=null){
                    loadingStateDialog.setImgState(3);
                    loadingStateDialog.setStatusContent("查询结果失败,请稍后再试");
                }
            }

            @Override
            public void onLoginAnomaly() {
                super.onLoginAnomaly();
                if(loadingStatetimer!=null){
                    loadingStatetimer.cancel();
                    loadingStatetimer=null;
                }
                if(loadingStateDialog!=null){
                    loadingStateDialog.setImgState(3);
                    loadingStateDialog.setStatusContent("查询结果失败,请稍后再试");
                }
            }

        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                initiativeSaoMaPayStateCheckTag(orderId,msg.what);
            }else if(msg.what == 2){
                initiativeSaoMaPayStateCheckTag(orderId,msg.what);
            }
            super.handleMessage(msg);
        };
    };

}
