package com.ryx.payment.ruishua.authenticate.creditcard;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.ryx.swiper.utils.CryptoUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import exocr.bankcard.BankManager;
import exocr.bankcard.DataCallBack;
import exocr.bankcard.EXBankCardInfo;

/**
 * Created by xiepingping on 2016/5/27.
 */
@EActivity(R.layout.activity_credit_card_auth_step3)
public class CreditCardAuthStep3Activity extends BaseActivity implements DataCallBack {

    @ViewById(R.id.iv_check_front)
    ImageView check_front;
    @ViewById(R.id.iv_creditcard_front)
    ImageView iv_creditcard_front;
    @ViewById(R.id.tileleftImg)
    ImageView tileleftImg;
    @ViewById
    TextView tv_warn;


    private String cardNo = "", cardEndDate = "", bankcardid = "";
    private String imgTempName = "", imgCardName = "";
    private final int TAKE_PHOTO = 11;
    private Bitmap tempBitmap;

    private Param qtpayflag;
    private Param qtpayAccountNo;
    private String bankTel;

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA
    };
    String warning = "";
    private boolean canPhoto = false;//是否可以调用系统自带的拍照功能，默认不可以
    private BottomSheetDialog mBottomSheetDialog;

    private String gotoTakePhoto = "";

    @AfterViews
    public void initViews() {
        cardNo = getIntent().getStringExtra("CARDNO");
        cardEndDate = getIntent().getStringExtra("CARDDATE");
        bankcardid = getIntent().getStringExtra("BankcardId");
        bankTel = getIntent().getStringExtra("bankTel");
        setTitleLayout(getResources().getString(R.string.senior_credit_card), true, false);

    }


    //识别到了银行卡片信息
    @Override
    public void onBankCardDetected(boolean success) {
        canPhoto = false;
        BankManager.getInstance().stopRecognize();
        if (success) {
            EXBankCardInfo cardInfo = BankManager.getInstance().getCardInfo();
            if (cardInfo != null) {
                Bitmap cardImg = cardInfo.fullImage;
                showPicFromCamera(cardImg);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canPhoto) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    tv_warn.setText(Html.fromHtml(getResources().getString(R.string.credit_warn)));
                    tv_warn.setVisibility(View.VISIBLE);
                }
            }, 500);
        } else {
            tv_warn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCameraDenied() {


    }

    @Click(R.id.tv_warn)
    public void takephoto() {
        String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        requesDevicePermission(waring, 0x00011, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        requestPhotoStrorage();
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.CAMERA);

    }

    private void requestPhotoStrorage() {
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        requesDevicePermission(waring, 0x00012, new PermissionResult() {
                    @Override
                    public void requestSuccess() {

                        if (ImageLoaderUtil.avaiableSdcard()) {
                            canPhoto = false;
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
                            imgTempName = "/temp_" + format.format(new Date()) + ".jpg";
                            PreferenceUtil.getInstance(getApplicationContext()).saveString(
                                    RyxAppconfig.IMAG_SENIER_TEMP, imgTempName);
                            File f = new File(Environment.getExternalStorageDirectory(),
                                    imgTempName);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
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

    /**
     * 设置View2秒内不能重复点击
     *
     * @param v
     */
    public void disabledTimerView(final View v) {
        v.setClickable(false);
        v.setEnabled(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                v.setClickable(true);
                v.setEnabled(true);
            }
        }, 2000);
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");

        qtpayflag = new Param("flag", "01");
        qtpayAccountNo = new Param("accountNo");

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

    @Click(R.id.iv_creditcard_front)
    public void takePic(View view) {
        requestCode = 2;
        disabledTimerView(view);
        requestCode = 2;
        warning = "请在\"设置\"里，打开\"" + RyxAppdata.getInstance(this).getCurrentBranchName() + "\"的拍照权限，否则您将无法拍照";
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
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        requesDevicePermission(waring, 0x00012, new PermissionResult() {
                    @Override
                    public void requestSuccess() {

                        if (ImageLoaderUtil.avaiableSdcard()) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
                            imgTempName = "/temp_" + format.format(new Date()) + ".jpg";
                            PreferenceUtil.getInstance(getApplicationContext()).saveString(
                                    RyxAppconfig.IMAG_SENIER_TEMP, imgTempName);
                            File f = new File(Environment.getExternalStorageDirectory(),
                                    imgTempName);
                            canPhoto = true;
                            BankManager.getInstance().setShowPhoto(false);
                            BankManager.getInstance().showLogo(false);
                            BankManager.getInstance().recognize(CreditCardAuthStep3Activity.this, CreditCardAuthStep3Activity.this);
                        }
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    @Click(R.id.btn_next)
    public void goNext() {
        if (null == tempBitmap) {
            LogUtil.showToast(CreditCardAuthStep3Activity.this, "请先添加信用卡正面照片");
        } else {
            initQtPatParams();
            uploadcardpic();
        }
    }

    private void uploadcardpic() {
        PreferenceUtil.getInstance(getApplicationContext()).saveString(
                RyxAppconfig.IMAG_SENIER_TEMP, "");
        PreferenceUtil.getInstance(getApplicationContext()).saveString(
                RyxAppconfig.IMAG_SENIER_CREDIT, "");
        qtpayAccountNo.setValue(cardNo);
        qtpayApplication = new Param("application", "UploadAdvancedVip.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayflag);
        qtpayParameterList.add(qtpayAccountNo);

        qtpayParameterList.add(new Param("CreditCardDate", cardEndDate));

        qtpayParameterList.add(new Param("CreditCardTime", cardEndDate));

        qtpayParameterList.add(new Param("id", bankcardid));
        qtpayParameterList.add(new Param("pinyin", ""));//暂时识别不到用户姓名
        qtpayParameterList.add(new Param("MergeFlag", "1"));
        qtpayParameterList.add(new Param("bankTel", bankTel));
        qtpayParameterList.add(new Param("addFlag", "".equals(bankcardid) ? "0" : "1"));
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                qtpayParameterList.add(new Param("picSign", CryptoUtils.getInstance()
                        .EncodeDigest(BitmapUntils.getContent85(tempBitmap))));

                qtpayParameterList.add(new Param("signPicAscii", BitmapUntils
                        .changeBytesToHexString(BitmapUntils.getContent85(tempBitmap))));
                uploadHandler.sendEmptyMessage(1);
            }
        }).start();

    }

    private Handler uploadHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            cancleLoading();
            httpsPost("UploadAdvancedVip", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    startActivityForResult(new Intent(CreditCardAuthStep3Activity.this, CreditCardAuthSuccedActivity_.class), RyxAppconfig.WILL_BE_CLOSED);
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
    };

    public void showPicFromCamera(Bitmap bitmap) {

        imgTempName = Environment.getExternalStorageDirectory()
                + PreferenceUtil.getInstance(getApplicationContext())
                .getString(RyxAppconfig.IMAG_SENIER_TEMP, "");

        LogUtil.showLog("imgTempName=" + imgTempName);

        check_front.setVisibility(View.VISIBLE);
        tempBitmap = null;
        imgCardName = imgTempName;
        Glide.with(CreditCardAuthStep3Activity.this)
                .load(BitmapUntils.getContent(bitmap))
                .asBitmap()
                .fitCenter()
                .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(CreditCardAuthStep3Activity.this), PhoneinfoUtils.getWindowsHight(CreditCardAuthStep3Activity.this)) {

                    @Override
                    public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                        tempBitmap = (Bitmap) resource;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (tempBitmap != null) {
                                    String[] name = imgCardName.split("/");
                                    imgCardName = name[name.length - 1];
                                    imgCardName = "IMOB_" + imgCardName.substring(1, imgCardName.length());
                                    imgCardName = RyxAppconfig.imageCachePath + imgCardName;

                                    PreferenceUtil.getInstance(getApplicationContext()).saveString(
                                            RyxAppconfig.IMAG_SENIER_CREDIT, imgCardName);
//                                        ImageLoaderUtil.savePic(tempBitmap, imgCardName);
                                    iv_creditcard_front.setImageBitmap(tempBitmap);
                                } else {
                                    LogUtil.showToast(CreditCardAuthStep3Activity.this, "拍照失败请重新拍照!");
                                }
                            }
                        });

                    }
                });

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

    public void showPicFromCamera() {

        imgTempName = Environment.getExternalStorageDirectory()
                + PreferenceUtil.getInstance(getApplicationContext())
                .getString(RyxAppconfig.IMAG_SENIER_TEMP, "");
        LogUtil.showLog("imgTempName=" + imgTempName);
        check_front.setVisibility(View.VISIBLE);
        tempBitmap = null;
        imgCardName = imgTempName;

        Glide.with(CreditCardAuthStep3Activity.this)
                .load(imgCardName)
                .asBitmap()
                .fitCenter()
                .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(CreditCardAuthStep3Activity.this), PhoneinfoUtils.getWindowsHight(CreditCardAuthStep3Activity.this)) {

                    @Override
                    public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                        tempBitmap = (Bitmap) resource;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (tempBitmap != null) {
                                    String[] name = imgCardName.split("/");
                                    imgCardName = name[name.length - 1];
                                    imgCardName = "IMOB_" + imgCardName.substring(1, imgCardName.length());
                                    imgCardName = RyxAppconfig.imageCachePath + imgCardName;

                                    PreferenceUtil.getInstance(getApplicationContext()).saveString(
                                            RyxAppconfig.IMAG_SENIER_CREDIT, imgCardName);
                                    iv_creditcard_front.setImageBitmap(tempBitmap);
                                } else {
                                    LogUtil.showToast(CreditCardAuthStep3Activity.this, "拍照失败请重新拍照!");
                                }
                            }
                        });

                    }
                });
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