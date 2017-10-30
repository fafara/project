package com.ryx.payment.ruishua.authenticate;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rey.material.app.BottomSheetDialog;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.ImageLoaderUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.utils.UriUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 普通用户转为商户用户
 */
@EActivity(R.layout.activity_user_to_merchant)
public class UserToMerchantActivity extends BaseActivity {
    private BottomSheetDialog mBottomSheetDialog;
    private String imgTempName = "";
    final int TAKE_PHOTO = 11;
    private Bitmap myBitmap1;
    private String imgCardName = "";
    @AfterViews
    public void afterView(){
        setTitleLayout("商户认证",true,false);
        initQtPatParams();
    }
    @ViewById
    EditText merchant_customerName_et,merchant_yyzz_number_et,merchant_yyzz_address_et;
    @ViewById
    ImageView merchant_info_yyzz_holdiv,iv_merchant_yyzz_check_hold,iv_merchant_yyzz_dot;
    boolean hasFisrtPic = false;

    Param qtpayCertType;
    Param qtpayUserType;
    Param qtpayEmail;
    Param qtpayMerchantName;
    Param qtpayMerchantAddres, qtpayLicence,licenseImg;

    /**
     * 选择营业执照手持照片
     */
    @Click(R.id.merchant_info_yyzz_holdiv)
    public void merchantYyzzCheckHold(){
        showBottomDialog();
    }
    @Click(R.id.btn_next)
    public void submitMsg(){
        if(checkInput()){
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] imgBytes =BitmapUntils.getContent(myBitmap1);
//               final String bitmap1Str= BitmapUntils.bytesToHexString(imgBytes);
               final String bitmap1Str= BitmapUntils.changeBytesToHexString(imgBytes);
                LogUtil.showLog("bitmap==="+bitmap1Str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         String customerName=merchant_customerName_et.getText().toString();
                        String yyzzNumber=merchant_yyzz_number_et.getText().toString();
                        String yyzzAddress=merchant_yyzz_address_et.getText().toString();
                        qtpayApplication.setValue("SupplementCustomerInfo.Req");
                        qtpayMerchantName.setValue(customerName);
                        qtpayMerchantAddres.setValue(yyzzAddress);
                        qtpayLicence.setValue(yyzzNumber);
                        licenseImg.setValue(TextUtils.isEmpty(bitmap1Str)?"":bitmap1Str.toUpperCase());
                        qtpayAttributeList.add(qtpayApplication);
                        qtpayParameterList.add(qtpayMerchantName);
                        qtpayParameterList.add(qtpayMerchantAddres);
                        qtpayParameterList.add(qtpayLicence);
                        qtpayParameterList.add(licenseImg);
                    httpsPost("SupplementCustomerInfoTag", new XmlCallback() {
                        @Override
                        public void onTradeSuccess(RyxPayResult payResult) {
                            LogUtil.showToast(UserToMerchantActivity.this,"商户信息提交完毕,请耐心等待审核!");
                            finish();
                        }
                    });
                    }
                });
            }
        }).start();

        }
    }
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayMerchantName = new Param("merchantName", "");
        qtpayMerchantAddres = new Param("merchantAddres", "");
        qtpayLicence = new Param("businessLicence", "");
        licenseImg = new Param("licenseImg", "");
    }

    /**
     * 输入信息监测
     * @return
     */
    public boolean checkInput(){
        String customerName=merchant_customerName_et.getText().toString();
        if(TextUtils.isEmpty(customerName)){
            LogUtil.showToast(UserToMerchantActivity.this,"请输入商户名称");
            return false;
        }
        String yyzzNumber=merchant_yyzz_number_et.getText().toString();
        if(TextUtils.isEmpty(yyzzNumber)){
            LogUtil.showToast(UserToMerchantActivity.this,"请输入营业执照号");
            return false;
        }
        String yyzzAddress=merchant_yyzz_address_et.getText().toString();
        if(TextUtils.isEmpty(yyzzAddress)){
            LogUtil.showToast(UserToMerchantActivity.this,"请输入营业地址信息");
            return false;
        }
        if(!hasFisrtPic){
            LogUtil.showToast(UserToMerchantActivity.this,"请选择营业执照照片");
            return false;
        }
        return true;
    }

    /**
     * 选择照片
     */
    private void showBottomDialog() {
        mBottomSheetDialog = new BottomSheetDialog(UserToMerchantActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(UserToMerchantActivity.this).inflate(R.layout.dialog__bottom_take_photo, null);
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

    /**
     * 执行拍照权限判断
     */
    public void takephotoPictures() {
        String  waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
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
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, UriUtils.fromFile(f,UserToMerchantActivity.this));
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
            iv_merchant_yyzz_dot.setVisibility(View.VISIBLE);
                myBitmap1 = null;
                hasFisrtPic = false;
                imgCardName = imgTempName;
                Glide.with(UserToMerchantActivity.this)
                        .load(imgCardName)
                        .asBitmap()
                        .fitCenter()
                        .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(UserToMerchantActivity.this), PhoneinfoUtils.getWindowsHight(UserToMerchantActivity.this)) {
                            @Override
                            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                                myBitmap1=(Bitmap) resource;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(myBitmap1!=null){
                                            hasFisrtPic = true;
                                            String[] name = imgCardName.split("/");
                                            imgCardName = name[name.length - 1];
                                            imgCardName = "USERTOMERCHANT_" + imgCardName.substring(1, imgCardName.length());
                                            imgCardName = RyxAppconfig.imageCachePath + imgCardName;
                                            PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_IDENTITY1, imgCardName);
                                            merchant_info_yyzz_holdiv.setImageBitmap(myBitmap1);
                                        }else{
                                            LogUtil.showToast(UserToMerchantActivity.this, "拍照失败请重新拍照!");
                                        }
                                    }
                                });

                            }
                        });
            }
}
