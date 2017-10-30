package com.ryx.payment.ruishua.convenience;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.HttpUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.RyxCountDownTimer;
import com.ryx.payment.ryxhttp.callback.StringCallback;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.Map;

import okhttp3.Call;

/**
 * 扫码收款确定后展示二维码Activity
 */
@EActivity(R.layout.activity_scan_pay_qrcode_dis_paly)
public class ScanPayQrcodeDisPalyActivity extends BaseActivity {
    @ViewById
    ImageView qrcodeimg;
    @ViewById
    Button bt_identifyqrcode;
    @ViewById
    AutoLinearLayout qrcodeallaout;
    @ViewById
    TextView tv_qrcodedesc;
    private String qrcodecontent;
    @ViewById
    Button bt_saveqrcodeimg;
    String qrcodeStatus;
    RyxCountDownTimer ryxCountDownTimer;
    @AfterViews
    public void initView(){
        setTitleLayout("二维码收款",true,true);
        setRightImgMessage("用户须知", RyxAppconfig.Notes_Scancode);
        initQtPatParams();
        qtpayApplication.setValue("ScanCode.Req");
        qtpayAttributeList.add(qtpayApplication);
    Bundle bundle=    getIntent().getExtras();

     String qrcodepath=   bundle.getString("qrcodepath");
     qrcodeStatus=   bundle.getString("qrcodeStatus");
     String qrcodeDesc=   bundle.getString("qrcodeDesc");
     String outtime=   bundle.getString("outtime");
        int outtime_int=Integer.parseInt(outtime);
        if("1".equals(qrcodeStatus)&&!TextUtils.isEmpty(qrcodepath)){
            Glide.with(ScanPayQrcodeDisPalyActivity.this).load(qrcodepath).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(qrcodeimg);
            ryxCountDownTimer= new RyxCountDownTimer(outtime_int*1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    tv_qrcodedesc.setText("二维码剩余有效时间:" + millisUntilFinished / 1000+"秒");
                }
                public void onFinish() {
                    tv_qrcodedesc.setText("二维码已失效!");
                    qrcodeStatus="2";
                    Glide.with(ScanPayQrcodeDisPalyActivity.this).load(R.drawable.qrcodefail).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(qrcodeimg);
                }
            }.start();

        }else{
            tv_qrcodedesc.setText("  "+qrcodeDesc);
        }
//        httpsPost("ScanCodeTag", new XmlCallback() {
//            @Override
//            public void onTradeSuccess(RyxPayResult payResult) {
//                try {
//                    if (payResult.getData() != null && payResult.getData().length() > 0) {
//                        JSONObject jsonObj = new JSONObject(payResult.getData());
//                        if ("1".equals(jsonObj.getString("qrcodeStatus"))) {
//                            qrcodeStatus="1";
//                            tv_qrcodedesc.setVisibility(View.INVISIBLE);
//                            qrcodecontent=jsonObj.getString("qrcodecontent");
//                            String qrcodepath =jsonObj.getString("qrcodepath");
//                            Glide.with(ScanPayQrcodeDisPalyActivity.this).load(qrcodepath).asBitmap().into(qrcodeimg);
//                        }else{
//                            qrcodeStatus="2";
//                            String qrcodeDesc =jsonObj.getString("qrcodeDesc");
//                            tv_qrcodedesc.setVisibility(View.VISIBLE);
//                            tv_qrcodedesc.setText("  "+qrcodeDesc);
//                            Glide.with(ScanPayQrcodeDisPalyActivity.this).load(R.drawable.qrcodefail).asBitmap().into(qrcodeimg);
//                        }
//
//                    }
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    Glide.with(ScanPayQrcodeDisPalyActivity.this).load(R.drawable.qrcodefail).asBitmap().into(qrcodeimg);
//                }
//            }
//        });

    }

    @Click(R.id.bt_saveqrcodeimg)
    public void saveQrcodeImg(){
        if(!"1".equals(qrcodeStatus)){
            LogUtil.showToast(ScanPayQrcodeDisPalyActivity.this,"二维码失效,不允许保存!");
            return;
        }
        dirpermissionCheck();
    }
    @Click(R.id.bt_identifyqrcode)
    public void identifyqrcode(){
        //识别二维码
        LogUtil.showToast(this, "敬请期待!");
//    if(TextUtils.isEmpty(qrcodecontent)){
//        LogUtil.showToast(ScanPayQrcodeDisPalyActivity.this,"二维码有误,请重新获取!");
//        return;
//    }
//        requstScanningCode(qrcodecontent);
    }

    /**
     * 请求二维码链接
     */
    public void requstScanningCode(String url){
        if (!QtpayAppData.getInstance(ScanPayQrcodeDisPalyActivity.this).isLogin()) {
            toAgainLogin(ScanPayQrcodeDisPalyActivity.this,RyxAppconfig.TOLOGINACT);
//            startActivityForResult(new Intent(ScanPayQrcodeDisPalyActivity.this, LoginActivity_.class),RyxAppconfig.TOLOGINACT);
            LogUtil.showToast(ScanPayQrcodeDisPalyActivity.this,"为保证账户安全，请你重新登录！");
            finish();
            return ;
        }
        url=url+"?customerId="+ RyxAppdata.getInstance(ScanPayQrcodeDisPalyActivity.this).getCustomerId()+"&token="+RyxAppdata.getInstance(ScanPayQrcodeDisPalyActivity.this).getToken();
        showLoading();
        HttpUtil.getInstance().httpsPostaddHeader("scanningCodeTag", url,"ryx_rs_pay","", new StringCallback() {
            @Override
            public void onResponse(String response) {
                cancleLoading();
//  {"result":{"custoemrid":"A000662329","username":"*长国","bankid":"105","cardno":"6217002340006606661","cardidx":"3","mobileno":"132*****613"},"code":"0000","msg":"成功"}
                LogUtil.showLog("result=="+response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if(RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getString("code"))){
                        JSONObject resultObject = jsonObj.getJSONObject("result");
                        String custoemrid=resultObject.getString("custoemrid");
                        String username=resultObject.getString("username");
                        String bankid=resultObject.getString("bankid");
                        String cardno=resultObject.getString("cardno");
                        String cardidx=resultObject.getString("cardidx");
                        String mobileno=resultObject.getString("mobileno");
                        Intent intent=new Intent(ScanPayQrcodeDisPalyActivity.this,PaymentActivity_.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("custoemrid",custoemrid);
                        bundle.putString("username",username);
                        bundle.putString("bankid",bankid);
                        bundle.putString("cardno",cardno);
                        bundle.putString("cardidx",cardidx);
                        bundle.putString("mobileno",mobileno);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }else{
                        String msg=jsonObj.getString("msg");
                        LogUtil.showToast(ScanPayQrcodeDisPalyActivity.this,msg);
                    }
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                cancleLoading();
                LogUtil.showLog("onErrorresult=="+e.getLocalizedMessage());
            }
        });
    }

    /**
     *
     * 文件操作权限判断
     */
    public void dirpermissionCheck(){
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), getResources().getString(R.string.app_name));
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), getResources().getString(R.string.app_name_ryx));
//        }

        final String finalWaring = waring;
        requesDevicePermission(waring, 0x0021, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        StringBuffer stringBuffer=new StringBuffer();
//                        if(RyxAppconfig.BRANCH.equals("01")){
//                            stringBuffer.append("ruishua/");
//                        }else if(RyxAppconfig.BRANCH.equals("02")){
//                            stringBuffer.append("ruiyinxin/");
//                        }
                        stringBuffer.append(RyxAppdata.getInstance(ScanPayQrcodeDisPalyActivity.this).getCurrentFileBranchRootName());
                        stringBuffer.append("PayQrCode");
                        //确定
                        //保存二维码图片到手机qrcodeallaout
                        Map<String,String> ssMap = BitmapUntils.saveQrcodeAsFile(qrcodeallaout,
                                stringBuffer.toString());
//        Bitmap bitmap= ((BitmapDrawable) qrcodeimg.getDrawable()).getBitmap();
//        String ss = BitmapUntils.saveBitmap(bitmap, "RuiShuaPayQrCode");
                        if(TextUtils.isEmpty(ssMap.get("path"))||TextUtils.isEmpty(ssMap.get("fileName"))){
                            LogUtil.showToast(ScanPayQrcodeDisPalyActivity.this,  ssMap.get("result"));
                            return;
                        }
                        // 其次把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(ScanPayQrcodeDisPalyActivity.this.getContentResolver(),
                                    ssMap.get("path") + File.separator + ssMap.get("fileName")
                                            + ".jpg", ssMap.get("fileName"), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        // 最后通知图库更新
                        ScanPayQrcodeDisPalyActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + ssMap.get("path") + File.separator + ssMap.get("fileName")
                                + ".jpg")));
                        LogUtil.showToast(ScanPayQrcodeDisPalyActivity.this, ssMap.get("result"));
                    }

                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(ScanPayQrcodeDisPalyActivity.this, finalWaring);
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ryxCountDownTimer!=null){
            ryxCountDownTimer.cancel();
        }
    }
}
