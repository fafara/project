package com.ryx.payment.ruishua.authenticate;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rey.material.app.BottomSheetDialog;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.authenticate.newauthenticate.NewAuthResultAct_;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.ImageLoaderUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.utils.UriUtils;
import com.ryx.payment.ruishua.widget.RyxLoadDialog;
import com.ryx.payment.ruishua.widget.RyxLoadDialogBuilder;
import com.ryx.swiper.utils.CryptoUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 商户照片心
 */
@EActivity(R.layout.activity_merchant_credentials_upload)
public class MerchantCredentialsUpload extends BaseActivity {
    int selectiv;
    private BottomSheetDialog mBottomSheetDialog;
    private String imgTempName = "";
    final int TAKE_PHOTO = 11;
    boolean hasFisrtPic = false, hasSecondPic = false, hasThirdPic = false,hasFourPic=false;
    @ViewById
    ImageView iv_check_front;
    @ViewById
    ImageView iv_check_back;
    @ViewById
    ImageView iv_check_hold;
    @ViewById
    ImageView iv_merchant_yyzz_check_hold;
    private String imgProfile = "", imgCardName = "", imgCardReverseName = "",imgYyzzFileName="";
    private Bitmap myBitmap1, myBitmap2, myBitmap3,myBitmap4;
    @ViewById
    ImageView iv_idcard_front;
    @ViewById
    ImageView iv_idcard_back;
    @ViewById
    ImageView iv_idcard_hold;
    @ViewById
    ImageView merchant_info_yyzz_holdiv;
    @ViewById
    com.rey.material.widget.Button btnSubmit;
    final int START_UPLOADED = 0; // 准备上传照片，展示进度条
    final int FIRST_UPLOADED_SUCCESS = 1; // 第 1 张照片上传成功
    final int SECOND_UPLOADED_SUCCESS = 2; // 第 2 张照片上传成功
    final int THRID_UPLOADED_SUCCESS = 3; // 第 3 张照片上传成功
    final int FOUR_UPLOADED_SUCCESS = 4; // 第 4 张照片上传成功
    final int UPLOADED_FINISH = 4; // 上传完毕，关掉进度条
    Param qtpayImg;
    Param qtpayImgApplyType;
    Param qtpayImgSign;
    RyxLoadDialogBuilder ryxLoadDialogBuilder;
    int currentindex = 0; // 当前索引
    boolean[] isokflag = {false, false, false,false};
    String[] upindexs = {"01", "02", "03","04"};
    String[] infos = {"", "", "",""};
    private String powermsg, rmsmsg;
    @AfterViews
    public void afterView(){
        setTitleLayout("上传照片",true,false);
        initQtPatParams();
    }
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        isNeedThread = false;
        qtpayApplication = new Param("application");
        qtpayImg = new Param("img");
        qtpayImgApplyType = new Param("imgApplyType");
        qtpayImgSign = new Param("imgSign");

    }
    /**
     * 身份证正面照片
     */
    @Click(R.id.iv_idcard_front)
    public void idcardFront(){
        selectiv = 1;
        takePhoto();
    }

    /**
     * 身份证反面照片
     */
    @Click(R.id.iv_idcard_back)
    public void idcardBack(){
        selectiv = 2;
        takePhoto();
    }

    /**
     *手持身份证照片
     */
    @Click(R.id.iv_idcard_hold)
    public void idcardHold(){
        selectiv = 3;
        takePhoto();
    }

    /**
     *营业执照
     */
    @Click(R.id.merchant_info_yyzz_holdiv)
    public void merchantInfoYyzzHoldiv(){
        selectiv = 4;
        takePhoto();
    }
@Click(R.id.btnSubmit)
public void nextClick(){
    if (QtpayAppData.getInstance(MerchantCredentialsUpload.this).isLogin()) {
        if ((isokflag[0] && isokflag[1] && isokflag[2]&&isokflag[3])) {
            QtpayAppData.getInstance(MerchantCredentialsUpload.this).setAuthenFlag(2);
            //进行是否符合新增条件状态判断
            LogUtil.showToast(MerchantCredentialsUpload.this,"图片已上传完毕.");
            doRequestPoint();
        }else{
        PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.TEMP_IMAGENAME, "");
        PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_IDENTITY1, "");
        PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_IDENTITY2, "");
        PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_PROFILE, "");
        PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_YYZZFILE, "");
        isNeedThread = false;
        if(checkInput()){
            myhandler.sendEmptyMessage(START_UPLOADED); // 初始化提示框
            //设置不可点击下一步按钮
            btnSubmit.setEnabled(false);
            uploadPics();
        }
        }
    } else {
        LogUtil.showToast(getApplicationContext(),
                getResources().getString(R.string.please_login_first));
    }
}

    /**
     * 子线程执行bitMap数据转换
     * @param completeListen
     */
    private void threadgetContent(final CompleteListen completeListen){
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] imgBytes = null;
                if (currentindex == 0)
                    imgBytes = BitmapUntils.getContent(myBitmap1);
                else if (currentindex == 1)
                    imgBytes = BitmapUntils.getContent(myBitmap2);
                else if (currentindex == 2)
                    imgBytes = BitmapUntils.getContent(myBitmap3);
                else if(currentindex == 3)
                    imgBytes = BitmapUntils.getContent(myBitmap4);
               String  bitmapStr=BitmapUntils.bytesToHexString(imgBytes);
                LogUtil.showLog("bitmap==merchant=="+bitmapStr);
                qtpayImg.setValue(bitmapStr); // 图片传输：采用Base64编码
                qtpayImgApplyType.setValue(upindexs[currentindex]);// 01：身份证正面02：身份证反面03：脸部头像（按顺序上传）
                final byte[] finalImgBytes = imgBytes;
                if(imgBytes==null){
                    qtpayImgSign.setValue(""); // 原图片文件内容MD5（转码前计算）
                }else{
                    String result= CryptoUtils.getInstance().EncodeDigest(imgBytes);
                    if(TextUtils.isEmpty(result)){
                        result="";
                    }
                    qtpayImgSign.setValue(result);
                }
                qtpayParameterList.add(qtpayImg);
                qtpayParameterList.add(qtpayImgApplyType);
                qtpayParameterList.add(qtpayImgSign);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        completeListen.bitMaptoStringSuccess(finalImgBytes);
                    }
                });

            }
        }).start();
    }

    /**
     * 图片资源上传
     */
    private void uploadPics() {

        if (!isokflag[currentindex]) {
            qtpayApplication.setValue("UserIdentityPicUpload3.Req");
            qtpayAttributeList.add(qtpayApplication);
            threadgetContent(new CompleteListen() {
                @Override
                public void bitMaptoStringSuccess(byte[] bitMapByte) {
                    bitMapByte = null;
                    httpsPost("UserIdentityPicUpload3", new XmlCallback() {
                        @Override
                        public void onLoginAnomaly() {
                            ryxLoadDialogBuilder.dismiss();
                        }

                        @Override
                        public void onTradeSuccess(RyxPayResult payResult) {
                            isokflag[currentindex] = true;
                            if (null != payResult) {
                                infos[currentindex] = payResult.getRespDesc();
                            }
                            currentindex = currentindex + 1;
                            myhandler.sendEmptyMessage(currentindex); // 提示在第几张更新成功

                        }

                        @Override
                        public void onOtherState() {
                            btnSubmit.setEnabled(true);
                            ryxLoadDialogBuilder.dismiss();
                        }

                        @Override
                        public void onTradeFailed() {
                            btnSubmit.setEnabled(true);
                            ryxLoadDialogBuilder.dismiss();
                        }
                    });
                }
            });
        }
    }

    /**
     * 判断是否全部上传成功
     *
     * @author tianyingzhong <br/>
     */
    public boolean isUploadSuccess() {
        String msginfo = "";
        for (int i = 0; i < upindexs.length; i++) {
            if (isokflag[i] == false) {
                msginfo = msginfo + "第" + (i + 1) + "张照片上传失败，失败原因：" + infos[i]
                        + "\r\n";
            }
        }
        if (isokflag[0] && isokflag[1] && isokflag[2]&&isokflag[3]) {
            return true;
        } else {
            LogUtil.showToast(MerchantCredentialsUpload.this, msginfo);
            return false;
        }

    }
    private Handler myhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case START_UPLOADED: // 显示
                    if (ryxLoadDialogBuilder == null) {
                        ryxLoadDialogBuilder = new RyxLoadDialog().getInstance(MerchantCredentialsUpload.this);
                        ryxLoadDialogBuilder.setCancelable(false);
                    }
                    ryxLoadDialogBuilder.setMessage("图片开始上传,请不要关闭此页面");
                    ryxLoadDialogBuilder.show();
                    break;
                case FIRST_UPLOADED_SUCCESS: // 更新
                    ryxLoadDialogBuilder.setMessage("当前进度" + 1 + "/4,上传过程中请不要关闭此页面");
                    uploadPics();
                    break;
                case SECOND_UPLOADED_SUCCESS: // 更新
                    ryxLoadDialogBuilder.setMessage("当前进度" + 2 + "/4,上传过程中请不要关闭此页面");
                    uploadPics();
                    break;
                case THRID_UPLOADED_SUCCESS: // 更新
                    ryxLoadDialogBuilder.setMessage("当前进度" + 3 + "/4,上传过程中请不要关闭此页面");
                    uploadPics();
                    break;
                case FOUR_UPLOADED_SUCCESS: // 关闭
                    ryxLoadDialogBuilder.setMessage("当前进度" + 4 + "/4,上传过程中请不要关闭此页面");
                    ryxLoadDialogBuilder.dismiss();
                    if (isUploadSuccess()) {
                        QtpayAppData.getInstance(MerchantCredentialsUpload.this).setAuthenFlag(2);
                        //进行是否符合新增条件状态判断
                        LogUtil.showToast(MerchantCredentialsUpload.this,"图片上传完毕.");
                        doRequestPoint();
                    } else {
                        btnSubmit.setEnabled(true);
                    }
                    break;
            }
        }
    };
    public void doRequestPoint() {
        isNeedThread = true;
        qtpayApplication.setValue("GetPowerMessage.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("MergeFlag", "1"));
        httpsPost("GetPowerMessage", new XmlCallback() {
            @Override
            public void onLoginAnomaly() {

            }

            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                analyzeResult(payResult);
            }

            @Override
            public void onOtherState() {
                currentindex = 0;
                btnSubmit.setEnabled(true);
            }

            @Override
            public void onTradeFailed() {
                currentindex = 0;
                btnSubmit.setEnabled(true);
            }
        });

    }

    private void analyzeResult(RyxPayResult payResult) {
        if (qtpayApplication.getValue().equals("GetPowerMessage.Req")) {
            String datastring = payResult.getData();

            JSONObject jsonObj;
            try {
                jsonObj = new JSONObject(datastring);
                powermsg = JsonUtil.getValueFromJSONObject(jsonObj, "FLAG");
                rmsmsg = JsonUtil.getValueFromJSONObject(jsonObj, "RMS");
                //直接进入到刷卡界面
                if ("0".equals(powermsg)) {
//废弃旧的高级认证流程改为新的高级认证流程xucc
//                    Intent intent = new Intent(MerchantCredentialsUpload.this,
//                            CreditCardAuthStep1Activity_.class);
                    Intent intent = new Intent(MerchantCredentialsUpload.this,
                            NewAuthResultAct_.class);
                    startActivityForResult(intent,
                            RyxAppconfig.WILL_BE_CLOSED);
                    finish();
                } else {
                    //废弃旧的高级认证流程改为新的高级认证流程xucc
//                    startActivity(new Intent(MerchantCredentialsUpload.this, AuthResultActivity_.class)
//                            .putExtra("PowerMsg", powermsg).putExtra("rmsmsg", rmsmsg));
                    startActivity(new Intent(MerchantCredentialsUpload.this, NewAuthResultAct_.class)
                            .putExtra("PowerMsg", powermsg).putExtra("rmsmsg", rmsmsg));
                    finish();
                }
                btnSubmit.setEnabled(true);
            } catch (JSONException e) {
                LogUtil.showToast(MerchantCredentialsUpload.this, "数据异常！");
                e.printStackTrace();
            }


        }
    }
    /**
     * 输入信息校验
     * @return
     */
    private boolean checkInput(){
//        boolean hasFisrtPic = false, hasSecondPic = false, hasThirdPic = false,hasFourPic=false;
        if(!hasFisrtPic){
            LogUtil.showToast(MerchantCredentialsUpload.this,"请选择身份证正面照片");
            return false;
        }
        if(!hasSecondPic){
            LogUtil.showToast(MerchantCredentialsUpload.this,"请选择身份证反面照片");
            return false;
        }
        if(!hasThirdPic){
            LogUtil.showToast(MerchantCredentialsUpload.this,"请选择手持证件照片");
            return false;
        }
        if(!hasFourPic){
            LogUtil.showToast(MerchantCredentialsUpload.this,"请选择营业执照照片");
            return false;
        }
        return  true;
    }

    private void takePhoto() {
        PreferenceUtil.getInstance(MerchantCredentialsUpload.this).saveInt("selectiv", selectiv);
        //弹出对话框
        showBottomDialog();


    }
    private void showBottomDialog() {
        mBottomSheetDialog = new BottomSheetDialog(MerchantCredentialsUpload.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(MerchantCredentialsUpload.this).inflate(R.layout.dialog__bottom_take_photo, null);
        Button photoBtn = (Button) boottomView.findViewById(R.id.btn_photo);
        Button cancelBtn = (Button) boottomView.findViewById(R.id.btn_cancel);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                takephotoPictures();
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
    public void takephotoPictures() {
        String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), getResources().getString(R.string.app_name));
//        } else if (RyxAppconfig.BRANCH.equals("02")) {
//            waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), getResources().getString(R.string.app_name_ryx));
//        }
        requesDevicePermission(waring, 0x00011, new PermissionResult() {
                    @Override
                    public void requestSuccess() {

                        requestStrorage();
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.CAMERA);

    }
    private void requestStrorage() {
        String  waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), getResources().getString(R.string.app_name));
//        } else if (RyxAppconfig.BRANCH.equals("02")) {
//            waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), getResources().getString(R.string.app_name_ryx));
//        }
        requesDevicePermission(waring, 0x00012, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        if (ImageLoaderUtil.avaiableSdcard()) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");
                            imgTempName = "/temp_" + format2.format(new Date()) + ".jpg";
                            PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.TEMP_IMAGENAME, imgTempName);
                            LogUtil.showLog("imgTempName=" + imgTempName);
                            File f = new File(Environment.getExternalStorageDirectory(), imgTempName);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, UriUtils.fromFile(f,MerchantCredentialsUpload.this));
                            intent.putExtra("scale", true);
                            startActivityForResult(intent, TAKE_PHOTO);
                        } else {
                            //Toast.makeText(ApproveActivity.this, R.string.approve_msg_nosdcord, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    showPicFromCamera(null);
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void showPicFromCamera(String filePath)
    {

        if (TextUtils.isEmpty(filePath)) {
            imgTempName = Environment.getExternalStorageDirectory() +
                    PreferenceUtil.getInstance(getApplicationContext()).getString(RyxAppconfig.TEMP_IMAGENAME, "");
        } else {
            imgTempName = filePath;
        }
        selectiv = PreferenceUtil.getInstance(getApplicationContext())
                .getInt("selectiv", 1);
        switch (selectiv) {
            case 1: {
                hasFisrtPic = true;
                iv_check_front.setVisibility(View.VISIBLE);
                myBitmap1 = null;
                imgCardName = imgTempName;
                Glide.with(MerchantCredentialsUpload.this)
                        .load(imgCardName)
                        .asBitmap()
                        .fitCenter()
                        .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(MerchantCredentialsUpload.this), PhoneinfoUtils.getWindowsHight(MerchantCredentialsUpload.this)) {
                            @Override
                            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                                myBitmap1=(Bitmap) resource;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(myBitmap1!=null){
                                            String[] name = imgCardName.split("/");
                                            imgCardName = name[name.length - 1];
                                            imgCardName = "IMERCHANT_" + imgCardName.substring(1, imgCardName.length());
                                            imgCardName = RyxAppconfig.imageCachePath + imgCardName;
                                            PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_IDENTITY1, imgCardName);
                                            iv_idcard_front.setImageBitmap(myBitmap1);
                                        }else{
                                            LogUtil.showToast(MerchantCredentialsUpload.this, "拍照失败请重新拍照!");
                                        }
                                    }
                                });

                            }
                        });
                break;
            }
            case 2: {
                hasSecondPic = true;
                iv_check_back.setVisibility(View.VISIBLE);
                myBitmap2 = null;
                imgCardReverseName = imgTempName;
                Glide.with(MerchantCredentialsUpload.this)
                        .load(imgCardReverseName)
                        .asBitmap()
                        .fitCenter()
                        .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(MerchantCredentialsUpload.this), PhoneinfoUtils.getWindowsHight(MerchantCredentialsUpload.this)) {
                            @Override
                            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                                myBitmap2=(Bitmap) resource;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(myBitmap2!=null){
                                            String[] name = imgCardReverseName.split("/");
                                            imgCardReverseName = name[name.length - 1];
                                            imgCardReverseName = "IMERCHANT_" + imgCardReverseName.substring(1, imgCardReverseName.length());
                                            imgCardReverseName = RyxAppconfig.imageCachePath + imgCardReverseName;

                                            PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_IDENTITY2, imgCardReverseName);
                                            iv_idcard_back.setImageBitmap(myBitmap2);

                                        }else{
                                            LogUtil.showToast(MerchantCredentialsUpload.this, "拍照失败请重新拍照!");
                                        }
                                    }
                                });

                            }
                        });
                break;
            }
            case 3: {
                hasThirdPic = true;
                iv_check_hold.setVisibility(View.VISIBLE);
                myBitmap3 = null;
                imgProfile = imgTempName;
                Glide.with(MerchantCredentialsUpload.this)
                        .load(imgProfile)
                        .asBitmap()
                        .fitCenter()
                        .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(MerchantCredentialsUpload.this), PhoneinfoUtils.getWindowsHight(MerchantCredentialsUpload.this)) {
                            @Override
                            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                                myBitmap3=(Bitmap) resource;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(myBitmap3!=null){
                                            String[] name = imgProfile.split("/");
                                            imgProfile = name[name.length - 1];
                                            imgProfile = "IMERCHANT_" + imgProfile.substring(1, imgProfile.length());
                                            imgProfile = RyxAppconfig.imageCachePath + imgProfile;
                                            PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_PROFILE, imgProfile);
                                            iv_idcard_hold.setImageBitmap(myBitmap3);
                                        }else{
                                            LogUtil.showToast(MerchantCredentialsUpload.this, "拍照失败请重新拍照!");
                                        }
                                    }
                                });

                            }
                        });
                break;
            }
            case 4: {
                hasFourPic = true;
                iv_merchant_yyzz_check_hold.setVisibility(View.VISIBLE);
                myBitmap4 = null;
                imgYyzzFileName = imgTempName;
                Glide.with(MerchantCredentialsUpload.this)
                        .load(imgYyzzFileName)
                        .asBitmap()
                        .fitCenter()
                        .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(MerchantCredentialsUpload.this), PhoneinfoUtils.getWindowsHight(MerchantCredentialsUpload.this)) {
                            @Override
                            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                                myBitmap4=(Bitmap) resource;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(myBitmap4!=null){
                                            String[] name = imgYyzzFileName.split("/");
                                            imgYyzzFileName = name[name.length - 1];
                                            imgYyzzFileName = "IMERCHANT_" + imgYyzzFileName.substring(1, imgYyzzFileName.length());
                                            imgYyzzFileName = RyxAppconfig.imageCachePath + imgYyzzFileName;
                                            PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_YYZZFILE, imgYyzzFileName);
                                            merchant_info_yyzz_holdiv.setImageBitmap(myBitmap4);
                                        }else{
                                            LogUtil.showToast(MerchantCredentialsUpload.this, "拍照失败请重新拍照!");
                                        }
                                    }
                                });

                            }
                        });
                break;
            }
        }

    }
    interface  CompleteListen{
        public void bitMaptoStringSuccess(byte[] bitMapByte );
    }
}
