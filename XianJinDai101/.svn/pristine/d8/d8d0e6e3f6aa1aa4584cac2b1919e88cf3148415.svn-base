package com.ryx.payment.ruishua.authenticate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.view.FingerPaintView;
import com.ryx.swiper.utils.CryptoUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * xiepingping
 */
@EActivity(R.layout.activity_user_authenticate_signature_activity)
public class UserAuthenticateSignatureActivity extends BaseActivity {

    @ViewById(R.id.view_sign)
    FingerPaintView signView;

    private Bitmap sign_bitmap;
    private final int UPLOADSIGN_SUCCESS = 1;
    private Param qtpayMerchantId = new Param("merchantId", "0");
    private Param qtpayOrderId = new Param("orderId", "IDSIGN");
    Param qtpayLongitude; // 经度
    Param qtpayLatitude; // 纬度

    @AfterViews
    public void initData() {
        initQtPatParams();
        initView();
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application", "UserSignatureUpload.Req");
        qtpayLongitude = new Param(
                "longitude",
                baselongitude+"");
        qtpayLatitude = new Param(
                "latitude",
                baselatitude+"");
    }

    private void initView() {
        signView.setMenuText((MessageFormat.format(
                    getResources().getString(R.string.agree_agreement),
                    RyxAppdata.getInstance(this).getCurrentBranchName())),
                    getCurrentTime(),
                    getResources().getString(R.string.clear_signature),
                    getResources().getString(R.string.agree),
                    R.drawable.button_blank_bg, R.drawable.blue_button_bg);
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            signView.setMenuText((MessageFormat.format(
//                    getResources().getString(R.string.agree_agreement),
//                    getResources().getString(R.string.app_name))),
//                    getCurrentTime(),
//                    getResources().getString(R.string.clear_signature),
//                    getResources().getString(R.string.agree),
//                    R.drawable.button_blank_bg, R.drawable.blue_button_bg);
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            signView.setMenuText((MessageFormat.format(
//                    getResources().getString(R.string.agree_agreement),
//                    getResources().getString(R.string.app_name_ryx))),
//                    getCurrentTime(),
//                    getResources().getString(R.string.clear_signature),
//                    getResources().getString(R.string.agree),
//                    R.drawable.button_blank_bg, R.drawable.blue_button_bg);
//        }

        signView.SetLeftClick(new FingerPaintView.FingerPaintClickListener() {
            @Override
            public void onClick(View iv) {
                if (signView.isSigned()) {
                    signView.clearSign(); // 清除签名
                    signView.setSigned(false);
                } else {
                    LogUtil.showToast(UserAuthenticateSignatureActivity.this, "请先签名!");
                }
            }
        });
        signView.SetRightClick(new FingerPaintView.FingerPaintClickListener() {
            @Override
            public void onClick(View iv) {
                if (!signView.isSigned()) {
                    LogUtil.showToast(UserAuthenticateSignatureActivity.this, "请先签名！");
                    return;
                }
                //设置为不可操作
                signView.SetRightClickable(false);
                //没有登录提示
                if (!QtpayAppData.getInstance(
                        UserAuthenticateSignatureActivity.this).isLogin()) {
                    LogUtil.showToast(UserAuthenticateSignatureActivity.this,
                            getResources().getString(R.string.please_login_first));
                    return;
                }
                showLoading();
                new Thread() {
                    public void run() {
                        signView.setlockSignature(true);
                        sign_bitmap = signView.SaveAsBitmap(); // 保存图片
                        //保存图片失败提示
                        if (sign_bitmap == null) {
                            LogUtil.showToast(UserAuthenticateSignatureActivity.this, getResources()
                                    .getString(R.string.sign_save_fail));
                            cancleLoading();
                            return;
                        }
                        byte[] bitmapBytes = BitmapUntils
                                .getBitmapByte(sign_bitmap);
                        qtpayParameterList.add(new Param(
                                "signPicAscii",
                                BitmapUntils
                                        .changeBytesToHexString(bitmapBytes)));
                        qtpayParameterList.add(new Param(
                                "picSign",
                                CryptoUtils
                                        .getInstance()
                                        .EncodeDigest(
                                                bitmapBytes)));
                        cancleLoading();
                        mhandler.sendEmptyMessage(UPLOADSIGN_SUCCESS);
                    }

                }.start();
            }
        });
    }

    public String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//设置日期格式
        return df.format(new Date());
    }

    private Handler mhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOADSIGN_SUCCESS:
                    doRequest();
                    break;
                case After_REQUEST_SUCCESS:
                    UploadUserInfoRequest();
                    break;
                case REQUEST_Failure:
                    //设置为可操作
                    signView.SetRightClickable(true);
                    break;
            }
        }
    };


    private void doRequest() {
        qtpayApplication = new Param("application", "UserSignatureUpload.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayLongitude); // 经度
        qtpayParameterList.add(qtpayLatitude); // 纬度
        qtpayParameterList.add(qtpayMerchantId);
        qtpayParameterList.add(qtpayOrderId);
        httpsPost("UserSignatureUpload", new XmlCallback() {

            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                mhandler.sendEmptyMessage(After_REQUEST_SUCCESS);
            }

            @Override
            public void onTradeFailed() {
                mhandler.sendEmptyMessage(REQUEST_Failure);
            }

            @Override
            public void onOtherState() {
                mhandler.sendEmptyMessage(REQUEST_Failure);
            }
        });
    }

    private void UploadUserInfoRequest() {
        qtpayApplication = new Param("application", "UserUpdateInfo.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("certType", "01"));
        qtpayParameterList.add(new Param("userType", "00"));
        qtpayParameterList.add(new Param("email", "test@163.com"));
        qtpayParameterList.add(new Param("merchantName", ""));
        qtpayParameterList.add(new Param("merchantAddres", ""));
        qtpayParameterList.add(new Param("realName", PreferenceUtil.getInstance(getApplicationContext()).getString("realname", "")));
        qtpayParameterList.add(new Param("certPid", PreferenceUtil.getInstance(getApplicationContext()).getString("idcardnum", "")));
        httpsPost("UserUpdateInfo", new XmlCallback() {

            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                QtpayAppData.getInstance(UserAuthenticateSignatureActivity.this)
                        .setRealName(PreferenceUtil.getInstance(getApplicationContext()).getString("realname", ""));
                QtpayAppData.getInstance(UserAuthenticateSignatureActivity.this)
                        .setAuthenFlag(payResult.getAuthenFlag());
                QtpayAppData.getInstance(UserAuthenticateSignatureActivity.this)
                        .setUserType(payResult.getUserType());
                Intent intent = new Intent(UserAuthenticateSignatureActivity.this,
                        UserAuthPhotoUploadActivity_.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onTradeFailed() {
                mhandler.sendEmptyMessage(REQUEST_Failure);
            }

            @Override
            public void onOtherState() {
                mhandler.sendEmptyMessage(REQUEST_Failure);
            }
        });
    }

    @Click(R.id.btn_close)
    public void closeWindow() {
        finish();
    }

}
