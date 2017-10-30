package com.ryx.payment.ruishua.usercenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.view.FingerPaintView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@EActivity(R.layout.activity_black_sign)
public class BlackCheckSignActivity extends BaseActivity {
    @ViewById(R.id.black_fingerpaint)
    FingerPaintView fingerpaint;
    Bitmap bitmap;	// 签名照片
    private Intent intent;

    @AfterViews
    public void afterView(){
        intent=getIntent();
        initFingerView();
    }
@Click(R.id.black_iv_left)
public void blackIvLeftClick(){
    finish();
}
    public void initFingerView(){
        fingerpaint.setMenuText((MessageFormat.format(getResources().getString(R.string.agree_agreement), RyxAppdata.getInstance(this).getCurrentBranchName())),
                getCurrentTime(), getResources().getString(R.string.clear_signature)
                , getResources().getString(R.string.sign_an_agreement),
                R.drawable.bg_anniu_blank, R.drawable.bg_anniu_blue);
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            fingerpaint.setMenuText((MessageFormat.format(getResources().getString(R.string.agree_agreement),getResources().getString(R.string.app_name))),
//                    getCurrentTime(), getResources().getString(R.string.clear_signature)
//                    , getResources().getString(R.string.sign_an_agreement),
//                    R.drawable.bg_anniu_blank, R.drawable.bg_anniu_blue);
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            fingerpaint.setMenuText((MessageFormat.format(getResources().getString(R.string.agree_agreement),getResources().getString(R.string.app_name_ryx))),
//                    getCurrentTime(), getResources().getString(R.string.clear_signature)
//                    , getResources().getString(R.string.sign_an_agreement),
//                    R.drawable.bg_anniu_blank, R.drawable.bg_anniu_blue);
//        }
        fingerpaint.SetLeftClick(new FingerPaintView.FingerPaintClickListener() {

            @Override
            public void onClick(View iv) {
                if(fingerpaint.isSigned()){
                    fingerpaint.clearSign();    // 清除签名
                    fingerpaint.setSigned(false);
                }else{
                       LogUtil.showToast(BlackCheckSignActivity.this, "请先签名");
                }
            }
        });
        fingerpaint.SetRightClick(new FingerPaintView.FingerPaintClickListener() {

            @Override
            public void onClick(View iv) {
                // TODO Auto-generated method stub
                if(fingerpaint.isSigned()){

                    showLoading();
                    //设置为不可操作
                    fingerpaint.SetRightClickable(false);
                    new Thread() {
                        public void run() {
                            fingerpaint.setlockSignature(true);
                            bitmap = fingerpaint.SaveAsBitmap(); //保存图片
                            if(QtpayAppData.getInstance(BlackCheckSignActivity.this).isLogin()){
                                if(bitmap!=null){
                                    byte[] bitmapBytes = BitmapUntils.getBitmapByte(bitmap);
//									byte[] bitmap85Bytes = BitmapUntils.getContent85(bitmap);
                                    Message msg = new Message();
                                    msg.what=0x001;
                                    Bundle bundle=new Bundle();
//				                    bundle.putString("picSign", CryptoUtils.getInstance().EncodeDigest(bitmapBytes));
                                    bundle.putString("signPicAscii", BitmapUntils.changeBytesToHexString(bitmapBytes));
//				                    bundle.putString("imgviewSign", BitmapUntils.changeBytesToHexString(bitmap85Bytes));
                                    msg.setData(bundle);
                                    mhandler.sendMessage(msg);
                                }else{
                                    mhandler.sendEmptyMessage(0x002);
                                }
                            }else{
                                toAgainLogin(BlackCheckSignActivity.this.getApplicationContext(), RyxAppconfig.TOLOGINACT);
//                                startActivityForResult(new Intent(BlackCheckSignActivity.this
//                                        .getApplicationContext(), LoginActivity_.class), RyxAppconfig.TOLOGINACT);
                            }

                        }
                    }.start();

                }else{
                    LogUtil.showToast(BlackCheckSignActivity.this,"请先签名!");
                }
            }
        });
    }
    private Handler mhandler = new Handler(){

        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0x001:
                    cancleLoading();
                    Bundle dataBundle=	msg.getData();
//				String picSignData= dataBundle.getString("picSign","");
                    String signPicAsciiData= dataBundle.getString("signPicAscii","");

//				String imgviewSignData= dataBundle.getString("imgviewSign","");
//				intent.putExtra("picSign",picSignData);
                    intent.putExtra("signPicAscii",signPicAsciiData);
                    LogUtil.showLog("发送signPicAscii=="+signPicAsciiData);
//				intent.putExtra("imgviewSignData",imgviewSignData);

                    setResult(RESULT_OK,intent);
                    finish();
//				intent.setClass(BlackCheckSignActivity.this,BlackCheckMsgActivity.class);
//				startActivity(intent);
                    break;
                case 0x002:
                    cancleLoading();
                    LogUtil.showToast(BlackCheckSignActivity.this, "签名图片有误,请重新签名保存!");
                    fingerpaint.setSigned(true);
                    fingerpaint.SetRightClickable(true);
                    break;
            }
        }

    };
    public String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//设置日期格式
        return df.format(new Date());
    }
}
