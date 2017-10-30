package com.ryx.payment.ruishua.usercenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.CreditClubActivity_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.PayStateCheckBaseActivity;
import com.ryx.payment.ruishua.convenience.SweepQuickPayActivity_;
import com.ryx.payment.ruishua.convenience.UionQrcodeDataReplenishActivity_;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.HttpUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ryxhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import okhttp3.Call;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.shape.CircleLightShape;

import static com.ryx.payment.ruishua.R.id.zbarview;

/**
 * 扫一扫
 */
@EActivity(R.layout.activity_scanning_code)
public class ScanningCodeActivity extends PayStateCheckBaseActivity implements QRCodeView.Delegate{
    public static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 5555;
    @ViewById(zbarview)
    QRCodeView qrCodeView;
    @ViewById(R.id.btn_select_pic)
    Button btnSelectPic;
    @ViewById(R.id.btn_open_light)
    Button btnOpenLight;
    @ViewById(R.id.btn_close_light)
    Button btnOpenClose;
    Timer timer = new Timer();
    private AlphaAnimation mShowAnimation= null;
    ArrayList<BankCardInfo> bankList;
    Boolean unionQrcode=false;
    String amount,cardIdx;
    String payType="fukuan";
    private HighLight mHightLight;
    @AfterViews
    public void initView(){
        setTitleLayout("扫一扫", true, true);
        initQtPatParams();
        initqrCodeView();
        initData();
        setRightImgMessage("扫一扫使用说明",RyxAppconfig.Notes_ScaningCode);
    }


private  void showHightLight(){
    int showHightFlag=PreferenceUtil.getInstance(ScanningCodeActivity.this).getInt("showHight",0);
    if(showHightFlag==0){
        mHightLight = new HighLight(ScanningCodeActivity.this)
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .addHighLight(R.id.tilerightImg, R.layout.scanningcode_hightlightbak, new HighLight.OnPosCallback() {
                    @Override
                    public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rectF.width()/5;
                        marginInfo.topMargin = rectF.bottom;
                    }
                }, new CircleLightShape());
        mHightLight.show();
    }
}
public void clickKnown(View view)
{
    if(mHightLight!=null&&mHightLight.isShowing()){
        PreferenceUtil.getInstance(ScanningCodeActivity.this).saveInt("showHight", 1);
        mHightLight.remove();
    }
}

    private void initData() {
      Bundle bundle=  getIntent().getExtras();
        unionQrcode=bundle.getBoolean("unionQrcode",false);

        if(unionQrcode){
            //二维码收款页面UnionQrcodePayActivity跳转过来,执行扫一扫并请求银联扣款
            amount=bundle.getString("amount","");
            cardIdx=bundle.getString("cardIdx","");
            payType=bundle.getString("payType","fukuan");
        }else{
            try {
                bankList = (ArrayList<BankCardInfo>) getIntent().getExtras().getSerializable("bankcardlist");
            }catch (Exception e){
                bankList=null;
            }
        }


    }

    public void initqrCodeView(){

        String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsgs), RyxAppdata.getInstance(this).getCurrentBranchName());
        this.requesDevicePermission(waring, 0x00018, new PermissionResult() {
            @Override
            public void requestSuccess() {
                qrCodeView.setDelegate(ScanningCodeActivity.this);
                setShowAnimation(qrCodeView,1000);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
//                        qrCodeView.setVisibility(View.VISIBLE);
                                    qrCodeView.startSpot();
                                    if(!unionQrcode){
                                        showHightLight();
                                    }
                                }catch (Exception e){

                                }
                            }
                        });
                    }
                },1000);
            }
            @Override
            public void requestFailed() {
                finish();
            }
        }, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);



    }
    @Click(R.id.btn_select_pic)
    public void setBtnSelectPic()
    {
        String waring = MessageFormat.format(getResources().getString(R.string.cameraimgwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        requesDevicePermission(waring, 0x00012, new PermissionResult() {

            @Override
            public void requestSuccess() {

                qrCodeView.setVisibility(View.INVISIBLE);
                startActivityForResult(BGAPhotoPickerActivity.newIntent(ScanningCodeActivity.this, null, 1, null,true), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
            }

            @Override
            public void requestFailed() {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);


    }
    @Click(R.id.btn_open_light)
    public void setBtnOpenLight(){
        qrCodeView.openFlashlight();
        btnOpenLight.setVisibility(View.GONE);
        btnOpenClose.setVisibility(View.VISIBLE);
    }

    @Click(R.id.btn_close_light)
    public void setBtnCloseLight()
    {
        qrCodeView.closeFlashlight();
        btnOpenLight.setVisibility(View.VISIBLE);
        btnOpenClose.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.showLog("ScanningCodeAc======onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        showAnimationqrCodeView();
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            if (data != null) {
                final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        return QRCodeDecoder.syncDecodeQRCode(picturePath);
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (TextUtils.isEmpty(result)) {
                            LogUtil.showToast(ScanningCodeActivity.this, "未发现二维码");

//                            showAnimationqrCodeView();
                        } else {
                           // TODO: 16/8/27 laomao 这里判断url是否是瑞银信 否则仅仅展示二码信息跳转/或展示信息
                            LogUtil.showLog("二维码信息:"+result);
//                            if(TextUtils.isEmpty(result)||!result.startsWith("https://mposprepo.ruiyinxin.com")){
//                                LogUtil.showToast(ScanningCodeActivity.this,"请检查二维码是否正确!");
////                                showAnimationqrCodeView();
//                                return;
//                            }
                            qrCodeView.stopSpot();
                            requstScanningCode(result);
                        }
                    }
                }.execute();

            }
        }};
private  void showAnimationqrCodeView(){
    String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
    requesDevicePermission(waring, 0x00013, new PermissionResult() {

        @Override
        public void requestSuccess() {
            setShowAnimation(qrCodeView,1000);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                        qrCodeView.setVisibility(View.VISIBLE);
                                qrCodeView.startSpot();
                            }catch (Exception e){

                            }
                        }
                    });
                }
            },1500);
        }

        @Override
        public void requestFailed() {
            finish();
        }
    }, Manifest.permission.CAMERA);
}
    /**
     * 处理扫描结果
     *
     * @param result
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        //隐藏扫描框
//        qrCodeView.hiddenScanRect();
        LogUtil.showLog("httpresult", result);
        //// TODO: 16/8/27 laomao 这里判断url是否是瑞银信 否则仅仅展示二码信息跳转/或展示信息
        LogUtil.showLog("二维码信息:"+result);
        if(TextUtils.isEmpty(result)){
            LogUtil.showToast(ScanningCodeActivity.this,"请检查二维码是否正确!");
            return;
        }
//        if(TextUtils.isEmpty(result)||!result.startsWith("https://mposprepo.ruiyinxin.com")){
//            LogUtil.showToast(ScanningCodeActivity.this,"请检查二维码是否正确!");
//            Snackbar.make(btnSelectPic, "文本信息"+result,  Snackbar.LENGTH_SHORT).setAction("", null).show();
//            qrCodeView.startSpot();
//            return;
//        }
        //暂停识别
        qrCodeView.stopSpot();
        requstScanningCode(result);
    }

    /**
     * 处理打开相机出错
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
//        qrCodeView.setVisibility(View.GONE);
        LogUtil.showToast(ScanningCodeActivity.this,"打开相机出错，请检查是否禁止照相机权限！");
    }


    @Override
    protected void onStart() {
        LogUtil.showLog("ScanningCodeAc======onStart");
        super.onStart();
        qrCodeView.startCamera();
        qrCodeView.startSpot();
        qrCodeView.showScanRect();

    }
    @Override
    protected void onStop() {
        qrCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        qrCodeView.onDestroy();
        super.onDestroy();
    }
    /**
     * 请求二维码链接
     */
    public void requstScanningCode(String url){
        if (!QtpayAppData.getInstance(ScanningCodeActivity.this).isLogin()) {
            toAgainLogin(ScanningCodeActivity.this, RyxAppconfig.TOLOGINACT);
//            startActivityForResult(new Intent(ScanningCodeActivity.this, LoginActivity_.class),RyxAppconfig.TOLOGINACT);
            LogUtil.showToast(ScanningCodeActivity.this,"为保证账户安全，请你重新登录！");
            finish();
            return ;
        }
        saomaSMZF003(url);
//        reqQrcodeContent(url);
    }

    /**
     * 拿着二维码信息进行判断是否合法，接下来流程步骤
     * @param url
     */
    private  void saomaSMZF003(final String url){
        qtpayApplication.setValue("SaomaSMZF003.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("code",url));
        if(unionQrcode){
            qtpayParameterList.add(new Param("amount",amount));
            qtpayParameterList.add(new Param("cardIdx",cardIdx));
        }
        qtpayParameterList.add(new Param("payType",payType));

        httpsPost("SaomaSMZF003Tag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    JSONObject jsonObject=new JSONObject(payResult.getData());
                    String code=   JsonUtil.getValueFromJSONObject(jsonObject,"code");
                    if("7777".equals(code)){
                        //请求当前网址
                        reqQrcodeContent(url);
                    }else if("8888".equals(code)){
                        //跳转金额和银行卡选择页面
                        if(bankList==null){
                            LogUtil.showToast(ScanningCodeActivity.this,"请绑定结算卡后再进行操作!");
                            return;
                        }
                        Intent intent=new Intent(ScanningCodeActivity.this, UionQrcodeDataReplenishActivity_.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("bankcardlist", bankList);
                        bundle.putString("code",url);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }else if("200002".equals(code)){
                        //交易结果未知弹出框进行轮询
                     JSONObject resultObj=   JsonUtil.getJSONObjectFromJsonObject(jsonObject,"result");
                      String orderId=  JsonUtil.getValueFromJSONObject(resultObj,"orderid");
//                        showLoadingStateDialog(orderId);
                        Intent intent=new Intent(ScanningCodeActivity.this, ScanningPayResultActivity_.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("orderId", orderId);
                        bundle.putString("amount",amount);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }else if("0000".equals(code)){
                            //交易成功立即返回结果
                        final String msg=   JsonUtil.getValueFromJSONObject(jsonObject,"msg");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMsgDialog(msg,true);
                            }
                        });

                    }else if("7766".equals(code)){
                        //ryx支付
                        qtpayApplication.setValue("RYXPAYGetOrderInfo.Req");
                        qtpayAttributeList.add(qtpayApplication);
                        qtpayParameterList.add(new Param("orderId",url));
                        httpsPost("RYXPAYGetOrderInfoTag", new XmlCallback() {
                            @Override
                            public void onTradeSuccess(RyxPayResult payResult) {
//       {"result":{"amount":"1000","skaccount":"4","qrcode":"1000000010775591","payee":"A000115786","skphone":"1329****613","skusername":"*长国"},"code":"0000","msg":"收款"}
                               String jsondata= payResult.getData();
                                try {
                                    JSONObject jsondataObj=new JSONObject(jsondata);
                                 String code= JsonUtil.getValueFromJSONObject(jsondataObj,"code");
                                    if(RyxAppconfig.QTNET_SUCCESS.equals(code)){
                                      JSONObject jsonObject1=JsonUtil.getJSONObjectFromJsonObject(jsondataObj,"result");

                                      String payee=JsonUtil.getValueFromJSONObject(jsonObject1,"payee");
                                      String re_orderid=JsonUtil.getValueFromJSONObject(jsonObject1,"re_orderid");
                                      String skusername=JsonUtil.getValueFromJSONObject(jsonObject1,"skusername");
                                      String skaccount=JsonUtil.getValueFromJSONObject(jsonObject1,"skaccount");
                                      String skphone=JsonUtil.getValueFromJSONObject(jsonObject1,"skphone");
                                      String qrcode=JsonUtil.getValueFromJSONObject(jsonObject1,"qrcode");
                                      String amount=JsonUtil.getValueFromJSONObject(jsonObject1,"amount");

                                        Intent intent=new Intent(ScanningCodeActivity.this,SweepQuickPayActivity_.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("payee",payee);
                                        bundle.putString("username",skusername);
                                        bundle.putString("qrcodeskaccount",skaccount);
                                        bundle.putString("mobileno",skphone);
                                        bundle.putString("qrcode",qrcode);
                                        bundle.putString("ordermoney",amount);
                                        bundle.putString("re_orderid",re_orderid);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        final String msg=   JsonUtil.getValueFromJSONObject(jsondataObj,"msg");
                                        if(!TextUtils.isEmpty(msg)){
                                            LogUtil.showToast(ScanningCodeActivity.this,msg);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    showMsgDialog(msg,false);
                                                }
                                            });

                                            return;
                                        }
                                    }

                                } catch (Exception e) {
                                    final String msg="交易结果异常,请查看交易记录!"+e.getMessage();
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showMsgDialog(msg,false);
                                        }
                                    });
                                }
//
                            }
                        });


                    } else{
                        final String msg=   JsonUtil.getValueFromJSONObject(jsonObject,"msg");
                        if(!TextUtils.isEmpty(msg)){
                            //支付成功同步结果code为0000，还有其他情况都只是仅展示信息框
                            LogUtil.showToast(ScanningCodeActivity.this,msg);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showMsgDialog(msg,false);
                                }
                            });

                            return;
                        }

                    }
                } catch (Exception e) {
                    //其他异常情况，仅展示信息框
                    e.printStackTrace();
                    LogUtil.showLog(e.getMessage());
                    final String msg="交易结果异常,请查看交易记录!"+e.getMessage();
                    LogUtil.showToast(ScanningCodeActivity.this,msg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMsgDialog(msg,false);
                        }
                    });
                }
            }

            @Override
            public void onOtherState(String rescode, final String resDesc) {
                super.onOtherState(rescode,resDesc);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsgDialog(resDesc,false);
                    }
                });
            }
        });


    }



    /**
     * 请求二维码内容信息
     * @param url
     */
    private void reqQrcodeContent(String url){
//        url=url+"?customerId="+ RyxAppdata.getInstance(ScanningCodeActivity.this).getCustomerId()+"&token="+RyxAppdata.getInstance(ScanningCodeActivity.this).getToken();
        showLoading();
        LogUtil.showLog("httprequest", url);
        HttpUtil.getInstance().httpsPostaddHeader("scanningCodeTag", url,"ryx_rs_pay","", new StringCallback() {
            @Override
            public void onResponse(String response) {
                LogUtil.showLog("httpresult", response);
                cancleLoading();
//  {"result":{"custoemrid":"A000662329","username":"*长国","bankid":"105","cardno":"6217002340006606661","cardidx":"3","mobileno":"132*****613"},"code":"0000","msg":"成功"}
                LogUtil.showLog("result=="+response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if(RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getString("code"))){
                        JSONObject resultObject = jsonObj.getJSONObject("result");
                        String type=JsonUtil.getValueFromJSONObject(resultObject,"type");
                        if("url".equals(type)){
                            String val=JsonUtil.getValueFromJSONObject(resultObject,"val");
                            Intent intent=new Intent(ScanningCodeActivity.this, CreditClubActivity_.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("title", "申请信用卡");
                            bundle.putString("ccurl", val);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }else{
                            String custoemrid=resultObject.getString("custoemrid");
                            String username=resultObject.getString("username");
                            String bankid=resultObject.getString("bankid");
                            String qrcode=resultObject.getString("qrcode");
//                        String cardno=resultObject.getString("cardno");//收款人账号此字段无用，因为不一定是此账号收款
                            String cardidx=resultObject.getString("cardidx");
                            String mobileno=resultObject.getString("mobileno");
                            Intent intent=new Intent(ScanningCodeActivity.this,SweepQuickPayActivity_.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("payee",custoemrid);
                            bundle.putString("username",username);
                            bundle.putString("bankid",bankid);
                            bundle.putString("qrcode",qrcode);
//                        bundle.putString("cardno",cardno);
                            bundle.putString("cardidx",cardidx);
                            bundle.putString("mobileno",mobileno);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        String msg=jsonObj.getString("msg");
                        LogUtil.showToast(ScanningCodeActivity.this,msg);
                        showMsgDialog(msg,false);
                    }
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    String msg="二维码信息有误!";
                    LogUtil.showToast(ScanningCodeActivity.this,msg);
                    showMsgDialog(msg,false);
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                cancleLoading();
                LogUtil.showLog("onErrorresult=="+e.getLocalizedMessage());
                qrCodeView.startSpot();
            }
        });
    }
    private void setShowAnimation( View view, int duration ){
        if( null == view || duration < 0 ){
            return;
        }
        if( null != mShowAnimation ){
            mShowAnimation.cancel( );
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration( duration );
        mShowAnimation.setFillAfter( true );
        view.startAnimation( mShowAnimation );
    }

    /**
     * 展示提示信息退出对话框
     * @param message
     */
    @Override
    public void showMsgDialog(String message,boolean isOnlyOk){
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(ScanningCodeActivity.this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                finish();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qrCodeView.startSpot();
                    }
                });
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent(message);
        ryxSimpleConfirmDialog.setCancelable(false);
        ryxSimpleConfirmDialog.setOkbtnText("退出");
        ryxSimpleConfirmDialog.setCancelbtnText("重扫");
        ryxSimpleConfirmDialog.setContentgravity(Gravity.CENTER);
        if(isOnlyOk){
            ryxSimpleConfirmDialog.setOnlyokLinearlayout();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
           if(mHightLight!=null&&mHightLight.isShowing()){
               mHightLight.remove();
           }else{
               finish();
           }

        }

        return false;

    }

}
