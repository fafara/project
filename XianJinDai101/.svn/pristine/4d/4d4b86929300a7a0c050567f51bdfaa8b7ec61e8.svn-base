package com.ryx.payment.ruishua.usercenter;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.SweepQuickPayActivity_;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.HttpUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ryxhttp.callback.StringCallback;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.Map;

import okhttp3.Call;

/**
 * 我的二维码
 */
@EActivity(R.layout.activity_qrcode)
public class QrcodePayActivity extends BaseActivity {
    @ViewById
    ImageView qrcodeimg;
    @ViewById
    AutoLinearLayout qrcodeallaout;
    @ViewById
    TextView tv_qrcodedesc;
    private String qrcodecontent;
    @ViewById
    ImageView img_qrcodelogo;

    @ViewById
    AutoLinearLayout qrcode_apply_allayout;
    @ViewById
    EditText qrcode_code_et;
    @ViewById
    EditText qrcode_codepaw_et;
    @ViewById
    LinearLayout qrcode_code_et_linelayout;
    @ViewById
    LinearLayout qrcode_codepaw_et_linelayout;
    @ViewById
    Button qrcode_submit_btn;
    @AfterViews
    public void initView(){
        setTitleLayout("我的二维码",true,true);
        setRightImgMessage("用户须知", RyxAppconfig.Notes_Scancode);
        RyxAppdata.getInstance(this).glideLoadqrcodeimgmageViewForBranch(img_qrcodelogo);
        initQtPatParams();
        qtpayApplication.setValue("ScanCode.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("ScanCodeTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    if (payResult.getData() != null && payResult.getData().length() > 0) {
                        JSONObject jsonObj = new JSONObject(payResult.getData());
                        if("9001".equals(jsonObj.getString("qrcodeStatus"))){
                            Glide.with(QrcodePayActivity.this).load(R.drawable.qrcodefail).asBitmap().error(R.drawable.qrcodefail).diskCacheStrategy(DiskCacheStrategy.NONE).into(qrcodeimg);
                            tv_qrcodedesc.setVisibility(View.VISIBLE);
                            qrcode_apply_allayout.setVisibility(View.VISIBLE);
                            String qrcodeDesc =jsonObj.getString("qrcodeDesc");
                            tv_qrcodedesc.setText("  "+qrcodeDesc);
                            LogUtil.showToast(QrcodePayActivity.this,"  "+qrcodeDesc);

                    }else  if ("1".equals(jsonObj.getString("qrcodeStatus"))) {
                            qrcode_apply_allayout.setVisibility(View.GONE);
                            tv_qrcodedesc.setVisibility(View.INVISIBLE);
                            qrcodecontent=jsonObj.getString("qrcodecontent");
                            String qrcodepath =jsonObj.getString("qrcodepath");
                            Glide.with(QrcodePayActivity.this).load(qrcodepath).asBitmap().error(R.drawable.qrcodefail).diskCacheStrategy(DiskCacheStrategy.NONE).into(qrcodeimg);
                        }else{
                            qrcode_apply_allayout.setVisibility(View.GONE);
                            String qrcodeDesc =jsonObj.getString("qrcodeDesc");
                            tv_qrcodedesc.setVisibility(View.VISIBLE);
                            tv_qrcodedesc.setText("  "+qrcodeDesc);
                            LogUtil.showToast(QrcodePayActivity.this,"  "+qrcodeDesc);
                            Glide.with(QrcodePayActivity.this).load(R.drawable.qrcodefail).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(qrcodeimg);
                        }

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    LogUtil.showToast(QrcodePayActivity.this,"数据异常,请稍后再试!");
                    e.printStackTrace();
                    Glide.with(QrcodePayActivity.this).load(R.drawable.qrcodefail).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(qrcodeimg);
                }
            }
        });

    }
    @FocusChange({R.id.qrcode_code_et, R.id.qrcode_codepaw_et})
    public void focusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.qrcode_code_et:
                qrcode_code_et_linelayout.setBackgroundResource(R.color.login_edt_getfocus);
                qrcode_codepaw_et_linelayout.setBackgroundResource(R.color.login_edt_lostfocus);
                break;
            case R.id.qrcode_codepaw_et:
                qrcode_code_et_linelayout.setBackgroundResource(R.color.login_edt_lostfocus);
                qrcode_codepaw_et_linelayout.setBackgroundResource(R.color.login_edt_getfocus);
                break;
        }
    }

    @LongClick(R.id.qrcodeimg)
    public void qrcodeimgLongClick(){
        showLongClickBottomDialog();
    }
    private void showLongClickBottomDialog() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(QrcodePayActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(QrcodePayActivity.this).inflate(R.layout.dialog__bottom_qrcodelongclick, null);
        android.widget.Button btn_saveimgBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_saveimg);
        android.widget.Button btn_identifysqrcodeBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_identifysqrcode);
        int kjzfTouchPayTag= RyxAppdata.getInstance(QrcodePayActivity.this).getkjzfTouchPayTag();
        if(kjzfTouchPayTag==1){
            btn_identifysqrcodeBtn.setVisibility(View.VISIBLE);
        }else {
            btn_identifysqrcodeBtn.setVisibility(View.GONE);
        }
        android.widget.Button cancelBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_cancel);
        btn_saveimgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                dirpermissionCheck();
            }
        });
        btn_identifysqrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                requstScanningCode();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.contentView(boottomView).show();
    }
    @Click(R.id.qrcode_submit_btn)
    public void bindQrcodeSubmit(){
        if(inputcheck()){
            String qrcodenum=  qrcode_code_et.getText().toString();
            String pasw=  qrcode_codepaw_et.getText().toString();
            qtpayApplication.setValue("BindQRcode.Req");
            Param qrcodeNum=new Param("qrCodeNum",qrcodenum);
            Param randomNum=new Param("randomNum",pasw);
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(qrcodeNum);
            qtpayParameterList.add(randomNum);
            httpsPost("BindQRcodeTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    try {
                        if (payResult.getData() != null && payResult.getData().length() > 0) {
                            JSONObject jsonObj = new JSONObject(payResult.getData());
                            if ("1".equals(jsonObj.getString("qrcodeStatus"))) {
                                tv_qrcodedesc.setVisibility(View.INVISIBLE);
                                qrcodecontent=jsonObj.getString("qrcodecontent");
                                String qrcodepath =jsonObj.getString("qrcodepath");
                                Glide.with(QrcodePayActivity.this).load(qrcodepath).asBitmap().error(R.drawable.qrcodefail).diskCacheStrategy(DiskCacheStrategy.NONE).into(qrcodeimg);

                                //如果获取二维码成功后显示
                                qrcode_apply_allayout.setVisibility(View.GONE);
                            }else{
                                qrcode_apply_allayout.setVisibility(View.VISIBLE);
                                String qrcodeDesc =jsonObj.getString("qrcodeDesc");
                                tv_qrcodedesc.setVisibility(View.VISIBLE);
                                tv_qrcodedesc.setText("  "+qrcodeDesc);
                                LogUtil.showToast(QrcodePayActivity.this,"  "+qrcodeDesc);
                                Glide.with(QrcodePayActivity.this).load(R.drawable.qrcodefail).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(qrcodeimg);
                            }

                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Glide.with(QrcodePayActivity.this).load(R.drawable.qrcodefail).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(qrcodeimg);
                    }
                }
            });
        }
    }
    /**
     * 请求二维码链接
     */
    public void requstScanningCode(){
        if (!QtpayAppData.getInstance(QrcodePayActivity.this).isLogin()) {
            toAgainLogin(QrcodePayActivity.this, RyxAppconfig.TOLOGINACT);
            LogUtil.showToast(QrcodePayActivity.this,"为保证账户安全，请你重新登录！");
            finish();
            return ;
        }
        if(TextUtils.isEmpty(qrcodecontent)){
            LogUtil.showToast(QrcodePayActivity.this,"二维码信息有误,请重试！");
            return;
        }
//        url=url+"?customerId="+ RyxAppdata.getInstance(QrcodePayActivity.this).getCustomerId()+"&token="+RyxAppdata.getInstance(QrcodePayActivity.this).getToken();
        showLoading();
        HttpUtil.getInstance().httpsPostaddHeader("scanningCodeTag", qrcodecontent,"ryx_rs_pay","", new StringCallback() {
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
                        String qrcode=resultObject.getString("qrcode");
//                        String cardno=resultObject.getString("cardno");//收款人账号此字段无用，因为不一定是此账号收款
                        String cardidx=resultObject.getString("cardidx");
                        String mobileno=resultObject.getString("mobileno");
                        Intent intent=new Intent(QrcodePayActivity.this,SweepQuickPayActivity_.class);
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
                    }else{
                        String msg=jsonObj.getString("msg");
                        LogUtil.showToast(QrcodePayActivity.this,msg);
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
     * 二维码邀请码填写
     * @return
     */
    public boolean inputcheck(){
  String qrcodenum=  qrcode_code_et.getText().toString();
  String pasw=  qrcode_codepaw_et.getText().toString();
   if(TextUtils.isEmpty(qrcodenum)||qrcodenum.length()!=10){
        LogUtil.showToast(QrcodePayActivity.this,"请正确输入邀请码!");
       return false;
   }
   if(TextUtils.isEmpty(pasw)||pasw.length()!=4){
        LogUtil.showToast(QrcodePayActivity.this,"请正确输入密码!");
       return  false;
   }

        return true ;
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
                        stringBuffer.append(RyxAppdata.getInstance(QrcodePayActivity.this).getCurrentFileBranchRootName());
//                        if(RyxAppconfig.BRANCH.equals("01")){
//                            stringBuffer.append("ruishua/");
//                        }else if(RyxAppconfig.BRANCH.equals("02")){
//                            stringBuffer.append("ruiyinxin/");
//                        }
                        stringBuffer.append("PayQrCode");
                        //确定
                        //保存二维码图片到手机qrcodeallaout
                        Map<String,String> ssMap = BitmapUntils.saveQrcodeAsFile(qrcodeallaout,
                                stringBuffer.toString());
//        Bitmap bitmap= ((BitmapDrawable) qrcodeimg.getDrawable()).getBitmap();
//        String ss = BitmapUntils.saveBitmap(bitmap, "RuiShuaPayQrCode");
                        if(TextUtils.isEmpty(ssMap.get("path"))||TextUtils.isEmpty(ssMap.get("fileName"))){
                            LogUtil.showToast(QrcodePayActivity.this,  ssMap.get("result"));
                            return;
                        }
                        // 其次把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(QrcodePayActivity.this.getContentResolver(),
                                    ssMap.get("path") + File.separator + ssMap.get("fileName")
                                            + ".jpg", ssMap.get("fileName"), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        // 最后通知图库更新
                        QrcodePayActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + ssMap.get("path") + File.separator + ssMap.get("fileName")
                                + ".jpg")));
                        LogUtil.showToast(QrcodePayActivity.this, ssMap.get("result"));
                    }

                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(QrcodePayActivity.this, finalWaring);
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }
}
