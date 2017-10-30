package com.ryx.payment.ruishua.authenticate;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rey.material.app.BottomSheetDialog;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.authenticate.creditcard.CreditCardAuthStep1Activity_;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.DateUtil;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import exocr.engine.DataCallBack;
import exocr.engine.EngineManager;
import exocr.exocrengine.EXIDCardResult;
import exocr.idcard.IDCardManager;


/***
 * xiepingping
 */
@EActivity(R.layout.activity_user_auth_photo_upload)
public class UserAuthPhotoUploadActivity extends BaseActivity implements DataCallBack {

    @ViewById(R.id.tv_title)
    TextView tv_title;

    @ViewById(R.id.tileleftImg)
    ImageView tileleftImg;
    @ViewById(R.id.tilerightImg)
    ImageView tilerightImg;

    @ViewById(R.id.iv_idcard_front)
    ImageView iv_idcard_front;
    @ViewById(R.id.iv_idcard_back)
    ImageView iv_idcard_back;
    @ViewById(R.id.iv_idcard_hold)
    ImageView iv_idcard_hold;

    @ViewById(R.id.iv_check_front)
    ImageView iv_check_front;
    @ViewById(R.id.iv_check_back)
    ImageView iv_check_back;
    @ViewById(R.id.iv_check_hold)
    ImageView iv_check_hold;

    @ViewById
    Button btn_next;

    private BottomSheetDialog mBottomSheetDialog;
    private String imgTempName = "";
    private Bitmap myBitmap1, myBitmap2, myBitmap3;
    boolean hasFisrtPic = false, hasSecondPic = false, hasThirdPic = false;
    //    private String imgProfile = "", imgCardName = "", imgCardReverseName = "";
    String[] upindexs = {"01", "02", "03"};
    boolean[] isokflag = {false, false, false};
    final int START_UPLOADED = 0; // 准备上传照片，展示进度条
    final int FIRST_UPLOADED_SUCCESS = 1; // 第 1 张照片上传成功
    final int SECOND_UPLOADED_SUCCESS = 2; // 第 2 张照片上传成功
    final int THRID_UPLOADED_SUCCESS = 3; // 第 3 张照片上传成功
    final int UPLOADED_FINISH = 4; // 上传完毕，关掉进度条
    int currentindex = 0; // 当前索引
    String[] infos = {"", "", ""};
    Param qtpayImg;
    Param qtpayImgApplyType;
    Param qtpayImgSign;
    int selectiv;
    final int TAKE_PHOTO = 11;
    RyxLoadDialogBuilder ryxLoadDialogBuilder;
    private String powermsg, rmsmsg;


    @AfterViews
    public void initData() {
        initQtPatParams();
        setTitleLayout("实名认证", true, true);
        EngineManager.getInstance().initEngine(this);
//        restShareData();
    }


    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        isNeedThread = false;
        qtpayApplication = new Param("application",
                "UserIdentityPicUpload2.Req");
        qtpayImg = new Param("img");
        qtpayImgApplyType = new Param("imgApplyType");
        qtpayImgSign = new Param("imgSign");

    }

    @Click(R.id.tilerightImg)
    public void showHelp() {
        Intent intent = new Intent(UserAuthPhotoUploadActivity.this, HtmlMessageActivity_.class);
        intent.putExtra("title", "拍照指南");
        intent.putExtra("urlkey", RyxAppconfig.Notes_Tips);
        startActivity(intent);
    }

    @Click(R.id.iv_idcard_front)
    public void showFrontPhoto() {
        selectiv = 1;
        takePhoto();
    }

    @Click(R.id.iv_idcard_back)
    public void showbackPhoto() {
        selectiv = 2;
        takePhoto();
    }

    @Click(R.id.iv_idcard_hold)
    public void getHoldPhoto() {
        selectiv = 3;
        takePhoto();
    }

    private void takePhoto() {
        PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).saveInt("selectiv", selectiv);
        //弹出对话框
        showBottomDialog();
    }

    private Bitmap cardImage;

    //获取识别到的身份证信息
    @Override
    public void onCardDetected(boolean success) {
        //识别身份证正面获取地址
        int selectIv = PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).getInt("selectiv",1);
        if(selectIv==1){
            //清除之前识别到的地址
            PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).saveString("addresss", "");
        }
        //识别身份证背面获取有效期
        else if(selectIv==2){
            //清除之前识别到的有效期
            PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).saveString("validDateStart", "");
            PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).saveString("validDateEnd", "");
        }
        if (success) {
            IDCardManager.getInstance().stopRecognize();
            EXIDCardResult result = IDCardManager.getInstance().getResult();
            cardImage = result.stdCardIm;
            if(selectIv==1){
            String  addresss = result.address;
            PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).saveString("addresss", addresss);
            LogUtil.showLog("-addresss",addresss+"--");
            }else if(selectIv==2){
                String validDate = result.validdate;
                String validDateStart="",validDateEnd="";
                if (!TextUtils.isEmpty(validDate)) {
                    if(validDate.contains("-")){
                        String[] strs=validDate.split("-");
                        if(strs!=null&&strs.length>0) {
                            validDateStart = strs[0];
                            validDateEnd = strs[1];
                            if(validDateStart.contains("/")){
                                validDateStart = validDateStart.replace("/","");
                            }
                            if(validDateEnd.contains("/")){
                                validDateEnd =  validDateEnd.replace("/","");
                            }
                        }
                    }
                }
                PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).saveString("validDateStart", validDateStart);
                PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).saveString("validDateEnd", validDateEnd);
                LogUtil.showLog("-validDate",validDateStart+"--"+validDateEnd);
            }
            showPicsFromIdentify(cardImage);
        }
    }

    @Override
    public void onCameraDenied() {

    }

    private void showBottomDialog() {
        mBottomSheetDialog = new BottomSheetDialog(UserAuthPhotoUploadActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(UserAuthPhotoUploadActivity.this).inflate(R.layout.dialog__bottom_take_photo, null);
        Button photoBtn = (Button) boottomView.findViewById(R.id.btn_photo);
        Button cancelBtn = (Button) boottomView.findViewById(R.id.btn_cancel);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                takephoto();
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

    @Click(R.id.tileleftImg)
    public void closeWindow() {
        restShareData();
        finish();
    }

    @Click(R.id.btn_next)
    public void nextPage() {
        if (QtpayAppData.getInstance(UserAuthPhotoUploadActivity.this).isLogin()) {
            isNeedThread = false;
            qtpayApplication.setValue("UserIdentityPicUpload2.Req");

            if (hasFisrtPic == true && hasSecondPic == true
                    && hasThirdPic == true) {
                if (isokflag[0] && isokflag[1] && isokflag[2]) {
                    btn_next.setEnabled(false);
                    //当前照片上传完毕,直接请求新增条件判断
                    LogUtil.showToast(UserAuthPhotoUploadActivity.this, "图片全部上传完毕");
                    //进行是否符合新增条件状态判断
                    doRequestPoint();
                } else {
                    myhandler.sendEmptyMessage(START_UPLOADED); // 初始化提示框
                    //设置不可点击下一步按钮
                    btn_next.setEnabled(false);
                    uploadPics();
                }

            } else {
                LogUtil.showToast(UserAuthPhotoUploadActivity.this, getResources()
                        .getString(R.string.please_upload_all_pic));
            }
        } else {
            LogUtil.showToast(getApplicationContext(),
                    getResources().getString(R.string.please_login_first));
        }
    }

    private Handler myhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case START_UPLOADED: // 显示
                    if (ryxLoadDialogBuilder == null) {
                        ryxLoadDialogBuilder = new RyxLoadDialog().getInstance(UserAuthPhotoUploadActivity.this);
                        ryxLoadDialogBuilder.setCancelable(false);
                    }
                    ryxLoadDialogBuilder.setMessage("当前进度" + currentindex
                            + "/3,上传过程中请不要关闭此页面");
                    ryxLoadDialogBuilder.show();
                    break;
                case FIRST_UPLOADED_SUCCESS: // 更新
                    ryxLoadDialogBuilder.setMessage("当前进度" + 1 + "/3,上传过程中请不要关闭此页面");
                    uploadPics();
                    break;
                case SECOND_UPLOADED_SUCCESS: // 更新
                    ryxLoadDialogBuilder.setMessage("当前进度" + 2 + "/3,上传过程中请不要关闭此页面");
                    uploadPics();
                    break;
                case THRID_UPLOADED_SUCCESS: // 关闭
                    ryxLoadDialogBuilder.setMessage("当前进度" + 3 + "/3,上传过程中请不要关闭此页面");
                    ryxLoadDialogBuilder.dismiss();
                    if (isUploadSuccess()) {
                        QtpayAppData.getInstance(UserAuthPhotoUploadActivity.this).setAuthenFlag(2);
                        //进行是否符合新增条件状态判断
                        doRequestPoint();
                    } else {
                        btn_next.setEnabled(true);
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
                btn_next.setEnabled(true);
            }

            @Override
            public void onTradeFailed() {
                currentindex = 0;
                btn_next.setEnabled(true);
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

                    Intent intent = new Intent(UserAuthPhotoUploadActivity.this,
                            CreditCardAuthStep1Activity_.class);
                    startActivityForResult(intent,
                            RyxAppconfig.WILL_BE_CLOSED);
                    finish();
                } else {
                    //进入列表页
                    startActivity(new Intent(UserAuthPhotoUploadActivity.this, AuthResultActivity_.class)
                            .putExtra("PowerMsg", powermsg).putExtra("rmsmsg", rmsmsg));
                    finish();
                }
                btn_next.setEnabled(true);
            } catch (JSONException e) {
                LogUtil.showToast(UserAuthPhotoUploadActivity.this, "数据异常！");
                e.printStackTrace();
            }


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
        if (isokflag[0] && isokflag[1] && isokflag[2]) {
            return true;
        } else {
            LogUtil.showToast(UserAuthPhotoUploadActivity.this, msginfo);
            return false;
        }

    }

    /**
     * 上传照片
     */
    public void uploadPics() {
        if (!isokflag[currentindex]) {
            qtpayAttributeList.add(qtpayApplication);
            byte[] imgBytes = null;
            if (currentindex == 0)
                imgBytes = BitmapUntils.getContent(myBitmap1);
            else if (currentindex == 1)
                imgBytes = BitmapUntils.getContent(myBitmap2);
            else if (currentindex == 2)
                imgBytes = BitmapUntils.getContent(myBitmap3);
            qtpayImg.setValue(BitmapUntils.bytesToHexString(imgBytes)); // 图片传输：采用Base64编码
            qtpayImgApplyType.setValue(upindexs[currentindex]);// 01：身份证正面02：身份证反面03：脸部头像（按顺序上传）
            if (imgBytes == null) {
                qtpayImgSign.setValue(""); // 原图片文件内容MD5（转码前计算）
            } else {
                String result = CryptoUtils.getInstance().EncodeDigest(imgBytes);
                if (TextUtils.isEmpty(result)) {
                    result = "";
                }
                qtpayImgSign.setValue(result);
            }
            qtpayParameterList.add(qtpayImg);
            qtpayParameterList.add(qtpayImgApplyType);
            qtpayParameterList.add(qtpayImgSign);
            String addresss = PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).getString("addresss","");
            String validDateStart = PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).getString("validDateStart","");
            String validDateEnd = PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).getString("validDateEnd","");
            qtpayParameterList.add(new Param("addresss",addresss));
            qtpayParameterList.add(new Param("validDateStart",validDateStart));
            qtpayParameterList.add(new Param("validDateEnd",validDateEnd));
            LogUtil.showLog("addresss---validDateEnd",addresss+"---"+validDateStart+"---"+validDateEnd);
            imgBytes = null;
            httpsPost("UserIdentityPicUpload2", new XmlCallback() {
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
                    btn_next.setEnabled(true);
                    ryxLoadDialogBuilder.dismiss();
                }

                @Override
                public void onTradeFailed() {
                    btn_next.setEnabled(true);
                    ryxLoadDialogBuilder.dismiss();
                }
            });

        }
    }

    public void takephoto() {
        String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
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
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        requesDevicePermission(waring, 0x00012, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        if (ImageLoaderUtil.avaiableSdcard()) {
                            SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");
                            imgTempName = "/temp_" + format2.format(new Date()) + ".jpg";
                            PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.TEMP_IMAGENAME, imgTempName);
                            int selecteiv = PreferenceUtil.getInstance(UserAuthPhotoUploadActivity.this).getInt("selectiv", 1);
                            //身份证正面照和背面照，启用易道识别
                            if (1 == selecteiv) {
                                IDCardManager.getInstance().setFront(true);
                                IDCardManager.getInstance().setShowPhoto(false);
                                IDCardManager.getInstance().setShowLogo(false);
                                IDCardManager.getInstance().recognize(UserAuthPhotoUploadActivity.this,
                                        UserAuthPhotoUploadActivity.this);
//                                idIdentifyFrame.setFront(true);
                                newshowPicFromCamera();
                            } else if (2 == selecteiv) {
                                IDCardManager.getInstance().setFront(false);
                                IDCardManager.getInstance().setShowPhoto(false);
                                IDCardManager.getInstance().setShowLogo(false);
                                IDCardManager.getInstance().recognize(UserAuthPhotoUploadActivity.this,
                                        UserAuthPhotoUploadActivity.this);
//                                idIdentifyFrame.setFront(false);
                                newshowPicFromCamera();
                            }
                            //手持身份证照片，不用易道识别，还是用原本拍照功能
                            else if (3 == selecteiv) {
                                File f = new File(Environment.getExternalStorageDirectory(), imgTempName);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 针对7.0可能存在调用方面问题处理
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, UriUtils.fromFile(f, UserAuthPhotoUploadActivity.this));
                                intent.putExtra("scale", true);
                                startActivityForResult(intent, TAKE_PHOTO);
                            }
                        } else {
                            LogUtil.showToast(UserAuthPhotoUploadActivity.this, "请确保SD卡是否存在!");
                        }
                    }

                    @Override
                    public void requestFailed() {
                    }
                }

                ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    public void newshowPicFromCamera() {
        imgTempName = Environment.getExternalStorageDirectory() +
                PreferenceUtil.getInstance(getApplicationContext()).getString(RyxAppconfig.TEMP_IMAGENAME, "");
        selectiv = PreferenceUtil.getInstance(getApplicationContext())
                .getInt("selectiv", 1);
        switch (selectiv) {
            case 1: {
                if (!TextUtils.isEmpty(imgTempName)) {
                    PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_IDENTITY1, imgTempName);
                }
                break;
            }
            case 2: {
                if (!TextUtils.isEmpty(imgTempName)) {
                    PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_IDENTITY2, imgTempName);
                }
                break;
            }
            case 3: {
                if (!TextUtils.isEmpty(imgTempName)) {
                    PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_PROFILE, imgTempName);
                }
                break;
            }
        }
    }

    //识别完身份证之后，显示照片
    private void showPicsFromIdentify(Bitmap imagPhoto) {
        int selectediv = PreferenceUtil.getInstance(getApplicationContext()).getInt("selectiv", 1);
        if (selectediv == 1) {
            String identity_img1 = PreferenceUtil.getInstance(getApplicationContext()).getString(RyxAppconfig.IMAG_IDENTITY1, "");
            LogUtil.showLog("onResume=======identity_img1=", identity_img1 + "," + DateUtil.getDateTime(new Date()));
            Glide.with(UserAuthPhotoUploadActivity.this)
                    .load(BitmapUntils.getContent(imagPhoto))
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(UserAuthPhotoUploadActivity.this), PhoneinfoUtils.getWindowsHight(UserAuthPhotoUploadActivity.this)) {
                        @Override
                        public void onResourceReady(final Object resource, GlideAnimation glideAnimation) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myBitmap1 = (Bitmap) resource;
                                    if (myBitmap1 != null) {
                                        hasFisrtPic = true;
                                        iv_check_front.setVisibility(View.VISIBLE);
                                        Bitmap myBitmap1thumbnail = ThumbnailUtils.extractThumbnail(myBitmap1, iv_idcard_front.getWidth(), iv_idcard_front.getHeight());
                                        iv_idcard_front.setImageBitmap(myBitmap1thumbnail == null ? myBitmap1 : myBitmap1thumbnail);
                                    } else {
                                        LogUtil.showToast(UserAuthPhotoUploadActivity.this, "身份证正面照片加载失败!");
                                    }
                                }
                            });

                        }
                    });
            //将正面照片保存在本地
            saveIdphoto(imagPhoto, identity_img1);
        }
        if (selectediv == 2) {
            String identity_img2 = PreferenceUtil.getInstance(getApplicationContext()).getString(RyxAppconfig.IMAG_IDENTITY2, "");
            LogUtil.showLog("onResume=======identity_img2=", identity_img2 + "," + DateUtil.getDateTime(new Date()));
            Glide.with(UserAuthPhotoUploadActivity.this)
                    .load(BitmapUntils.getContent(imagPhoto))
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(UserAuthPhotoUploadActivity.this), PhoneinfoUtils.getWindowsHight(UserAuthPhotoUploadActivity.this)) {
                        @Override
                        public void onResourceReady(final Object resource, GlideAnimation glideAnimation) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myBitmap2 = (Bitmap) resource;
                                    if (myBitmap2 != null) {
                                        hasSecondPic = true;
                                        iv_check_back.setVisibility(View.VISIBLE);
                                        Bitmap myBitmap2thumbnail = ThumbnailUtils.extractThumbnail(myBitmap2, iv_idcard_back.getWidth(), iv_idcard_back.getHeight());
                                        iv_idcard_back.setImageBitmap(myBitmap2thumbnail == null ? myBitmap2 : myBitmap2thumbnail);
                                    } else {
                                        LogUtil.showToast(UserAuthPhotoUploadActivity.this, "身份证正面照片加载失败!");
                                    }
                                }
                            });
                        }
                    });
            //将反面照片保存在本地
            saveIdphoto(imagPhoto, identity_img2);
        }

    }

    //将身份证正反面照片，保存在手机本地
    private void saveIdphoto(Bitmap photoImg, String photoName) {
        // 判断是否可以对SDcard进行操作
        if (ImageLoaderUtil.avaiableSdcard()) {
            LogUtil.showLog("saveIdphoto-----", photoName);
            File file = new File(photoName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                photoImg.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                LogUtil.showLog("saveIdphoto---+++", "已经保存");
            } catch (FileNotFoundException e) {
                LogUtil.showLog("ryx2----Exception", e.getMessage() + "---FileNotFound");
                e.printStackTrace();
            } catch (IOException e) {
                LogUtil.showLog("ryx2----Exception", e.getMessage() + "---IO");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String identity_img1 = PreferenceUtil.getInstance(getApplicationContext()).getString(RyxAppconfig.IMAG_IDENTITY1, "");
        LogUtil.showLog("onResume1---",identity_img1+"---");
        if (!TextUtils.isEmpty(identity_img1)) {
            Glide.with(UserAuthPhotoUploadActivity.this)
                    .load(identity_img1)
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(UserAuthPhotoUploadActivity.this), PhoneinfoUtils.getWindowsHight(UserAuthPhotoUploadActivity.this)) {
                        @Override
                        public void onResourceReady(final Object resource, GlideAnimation glideAnimation) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myBitmap1 = (Bitmap) resource;
                                    LogUtil.showLog("onResume---myBitmap1",myBitmap1+"---");
                                    if (myBitmap1 != null) {
                                        hasFisrtPic = true;
                                        iv_check_front.setVisibility(View.VISIBLE);
                                        //控件加载完之后，再获取控件宽高
                                        iv_idcard_front.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                // Removing layout listener to avoid multiple calls
                                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                                    iv_idcard_front.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                                } else {
                                                    iv_idcard_front.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                                }
                                                Bitmap myBitmap1thumbnail = ThumbnailUtils.extractThumbnail(myBitmap1, iv_idcard_front.getWidth(), iv_idcard_front.getHeight());
                                                iv_idcard_front.setImageBitmap(myBitmap1thumbnail == null ? myBitmap1 : myBitmap1thumbnail);
                                            }
                                        });
                                    } else {
                                        LogUtil.showToast(UserAuthPhotoUploadActivity.this, "身份证正面照片加载失败!");
                                    }
                                }
                            });
                        }
                    });
        }
        String identity_img2 = PreferenceUtil.getInstance(getApplicationContext()).getString(RyxAppconfig.IMAG_IDENTITY2, "");
        LogUtil.showLog("onResume2---",identity_img2+"---");
        if (!TextUtils.isEmpty(identity_img2)) {
            Glide.with(UserAuthPhotoUploadActivity.this)
                    .load(identity_img2)
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(UserAuthPhotoUploadActivity.this), PhoneinfoUtils.getWindowsHight(UserAuthPhotoUploadActivity.this)) {
                        @Override
                        public void onResourceReady(final Object resource, GlideAnimation glideAnimation) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myBitmap2 = (Bitmap) resource;
                                    LogUtil.showLog("onResume---myBitmap2",myBitmap2+"---");
                                    if (myBitmap2 != null) {
                                        hasSecondPic = true;
                                        iv_check_back.setVisibility(View.VISIBLE);
                                        iv_idcard_back.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                            @Override
                                            public void onGlobalLayout() {
                                                // Removing layout listener to avoid multiple calls
                                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                                    iv_idcard_back.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                                } else {
                                                    iv_idcard_back.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                                }
                                                Bitmap myBitmap2thumbnail = ThumbnailUtils.extractThumbnail(myBitmap2, iv_idcard_back.getWidth(), iv_idcard_back.getHeight());
                                                iv_idcard_back.setImageBitmap(myBitmap2thumbnail == null ? myBitmap2 : myBitmap2thumbnail);
                                            }
                                        });
                                    } else {
                                        LogUtil.showToast(UserAuthPhotoUploadActivity.this, "身份证正面照片加载失败!");
                                    }
                                }
                            });
                        }
                    });
        }

        //手持身份证照片，显示
        String profile_img = PreferenceUtil.getInstance(getApplicationContext()).getString(RyxAppconfig.IMAG_PROFILE, "");
        if (!TextUtils.isEmpty(profile_img)) {
            LogUtil.showLog("onResume=======profile_img=" + profile_img + "," + DateUtil.getDateTime(new Date()));
            Glide.with(UserAuthPhotoUploadActivity.this)
                    .load(profile_img)
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(UserAuthPhotoUploadActivity.this), PhoneinfoUtils.getWindowsHight(UserAuthPhotoUploadActivity.this)) {
                        @Override
                        public void onResourceReady(final Object resource, GlideAnimation glideAnimation) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myBitmap3 = (Bitmap) resource;
                                    if (myBitmap3 != null) {
                                        hasThirdPic = true;
                                        iv_check_hold.setVisibility(View.VISIBLE);
                                        int imgwith = PreferenceUtil.getInstance(getApplicationContext()).getInt("profile_width",0);
                                        int imgHeight = PreferenceUtil.getInstance(getApplicationContext()).getInt("profile_height",0);
                                        if(imgwith==0||imgHeight==0){
                                            iv_idcard_hold.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                                @Override
                                                public void onGlobalLayout() {
                                                    // Removing layout listener to avoid multiple calls
                                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                                        iv_idcard_hold.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                                    } else {
                                                        iv_idcard_hold.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                                    }
                                                    LogUtil.showLog("onGlobalLayout---","onGlobalLayout");
                                                    PreferenceUtil.getInstance(getApplicationContext()).saveInt("profile_width",iv_idcard_hold.getWidth());
                                                    PreferenceUtil.getInstance(getApplicationContext()).saveInt("profile_height",iv_idcard_hold.getHeight());
                                                    Bitmap myBitmap3thumbnail = ThumbnailUtils.extractThumbnail(myBitmap3, iv_idcard_hold.getWidth(), iv_idcard_hold.getHeight());
                                                    iv_idcard_hold.setImageBitmap(myBitmap3thumbnail == null ? myBitmap3 : myBitmap3thumbnail);
                                                }});
                                        }else{
                                            Bitmap myBitmap3thumbnail = ThumbnailUtils.extractThumbnail(myBitmap3, imgwith, imgHeight);
                                            iv_idcard_hold.setImageBitmap(myBitmap3thumbnail == null ? myBitmap3 : myBitmap3thumbnail);
                                         }
                                    } else {
                                        LogUtil.showToast(UserAuthPhotoUploadActivity.this, "手持身份证照片加载失败!");
                                    }
                                }
                            });

                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    newshowPicFromCamera();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            restShareData();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EngineManager.getInstance().finishEngine();

    }

    /**
     * 初始化数据
     */
    private void restShareData() {
        LogUtil.showLog("restShareData====" + DateUtil.getDateTime(new Date()));
        PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.TEMP_IMAGENAME, "");
        PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_IDENTITY1, "");
        PreferenceUtil.getInstance(getApplicationContext()).saveString(RyxAppconfig.IMAG_IDENTITY2, "");
        PreferenceUtil.getInstance(getApplicationContext()).saveInt("profile_width", 0);
        PreferenceUtil.getInstance(getApplicationContext()).saveInt("profile_height", 0);

    }
}
