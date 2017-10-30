package com.ryx.payment.ruishua.bindcard;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.inteface.PermissionResult;
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

@EActivity(R.layout.activity_my_credit_card_auth)
public class MyCreditCardAuthActivity extends BaseActivity {

    @ViewById
    ImageView iv_creditcard_front;
    @ViewById
    ImageView iv_check_front;
    String warning="";
    private String imgTempName = "", imgCardName = "";
    private final int TAKE_PHOTO = 11;
    private Bitmap tempBitmap;

    @AfterViews
    public void initViews() {
        setTitleLayout("信用卡认证",true,false);
    }

    @Click(R.id.iv_creditcard_front)
    public void takePic() {
        requestCode = 2;
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            warning  = "请在\"设置\"里，打开\""+getResources().getString(R.string.app_name)+"\"的拍照权限，否则您将无法拍照";
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            warning  = "请在\"设置\"里，打开\""+getResources().getString(R.string.app_name_ryx)+"\"的拍照权限，否则您将无法拍照";
//        }
        warning  = "请在\"设置\"里，打开\""+RyxAppdata.getInstance(this).getCurrentBranchName()+"\"的拍照权限，否则您将无法拍照";
        requesDevicePermission(warning, requestCode, permissionResult, Manifest.permission.CAMERA);
    }

    private PermissionResult permissionResult = new PermissionResult() {
        @Override
        public void requestSuccess() {
            requestStrorage();
        }

        @Override
        public void requestFailed() {
        }
    };

    private void requestStrorage() {
        String   waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
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
                            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
                            imgTempName = "/temp_" + format.format(new Date()) + ".jpg";
                            PreferenceUtil.getInstance(getApplicationContext()).saveString(
                                    RyxAppconfig.IMAG_SENIER_TEMP, imgTempName);
                            File f = new File(Environment.getExternalStorageDirectory(),
                                    imgTempName);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, UriUtils.fromFile(f,MyCreditCardAuthActivity.this));
                            intent.putExtra("scale", true);
                            startActivityForResult(intent, TAKE_PHOTO);
                        }
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public void showPicFromCamera() {
        imgTempName = Environment.getExternalStorageDirectory()
                + PreferenceUtil.getInstance(getApplicationContext())
                .getString(RyxAppconfig.IMAG_SENIER_TEMP, "");
        LogUtil.showLog("imgTempName=" + imgTempName);
        iv_check_front.setVisibility(View.VISIBLE);
        tempBitmap = null;
        imgCardName = imgTempName;
        Glide.with(MyCreditCardAuthActivity.this)
                .load(imgCardName)
                .asBitmap()
                .fitCenter()
                .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(MyCreditCardAuthActivity.this), PhoneinfoUtils.getWindowsHight(MyCreditCardAuthActivity.this)) {

                    @Override
                    public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                        tempBitmap=(Bitmap) resource;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(tempBitmap!=null){
                                    String[] name = imgCardName.split("/");
                                    imgCardName = name[name.length - 1];
                                    imgCardName = "IMOB_" + imgCardName.substring(1, imgCardName.length());
                                    imgCardName = RyxAppconfig.imageCachePath + imgCardName;

                                    PreferenceUtil.getInstance(getApplicationContext()).saveString(
                                            RyxAppconfig.IMAG_SENIER_CREDIT, imgCardName);
//                                        ImageLoaderUtil.savePic(tempBitmap, imgCardName);

                                    iv_creditcard_front.setImageBitmap(tempBitmap);
                                }else{
                                    LogUtil.showToast(MyCreditCardAuthActivity.this, "拍照失败请重新拍照!");
                                }
                            }
                        });

                    }
                });


//            tempBitmap = BitmapFactory.decodeFile(imgCardName, options);



    }

    @Click(R.id.btn_next)
    public void goNext() {
        if (null == tempBitmap) {
            LogUtil.showToast(MyCreditCardAuthActivity.this, "请先添加信用卡正面照片");
        } else {
            initQtPatParams();
        }
    }

    //返回
    @Click(R.id.tileleftImg)
    public void turnBack() {
        PreferenceUtil.getInstance(getApplicationContext()).saveString(
                RyxAppconfig.IMAG_SENIER_TEMP, "");
        PreferenceUtil.getInstance(getApplicationContext()).saveString(
                RyxAppconfig.IMAG_SENIER_CREDIT, "");
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            PreferenceUtil.getInstance(getApplicationContext()).saveString(
                    RyxAppconfig.IMAG_SENIER_TEMP, "");
            PreferenceUtil.getInstance(getApplicationContext()).saveString(
                    RyxAppconfig.IMAG_SENIER_CREDIT, "");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                showPicFromCamera();
            }

        }
    }
}
