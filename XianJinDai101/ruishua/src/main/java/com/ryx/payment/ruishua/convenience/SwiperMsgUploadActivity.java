package com.ryx.payment.ruishua.convenience;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.IDCardUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PhoneinfoUtils;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.payment.ruishua.utils.UriUtils;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.swiper.utils.CryptoUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_swiper_msg_upload)
public class SwiperMsgUploadActivity extends BaseActivity {
    @ViewById
    AutoLinearLayout swiper_photo_layout,swiper_msg_layout;
    @ViewById(R.id.iv_touxiang)
    ImageView iv_touxiang;
    @ViewById
    TextView tv_prompt,tv_idcardid;
    @ViewById
    Button swiper_check_nextbtn;
    @ViewById
    EditText et_pname,et_pid,et_phoneno,et_smscode;
    private String imgTempName = "";
    final int TAKE_PHOTO = 1;
    private Bitmap myBitmap;
    OrderInfo orderinfo;
    String cardInfo;
    String orderid="",cardpsw="",icdata="",serialnum="",cardtype="",translognumber="",uploadflag,account;
    @AfterViews
    public void initView(){
        setTitleLayout("资料补充",true,false);
        try {
            orderinfo = (OrderInfo) getIntent().getExtras().get("orderinfo");
            cardInfo= getIntent().getExtras().getString("cardInfo");
            orderid= getIntent().getExtras().getString("orderid");
            cardpsw= getIntent().getExtras().getString("cardpsw");
            icdata= getIntent().getExtras().getString("icdata");
            serialnum= getIntent().getExtras().getString("serialnum");
            cardtype= getIntent().getExtras().getString("cardtype");
            baseprovinceid= getIntent().getExtras().getString("baseprovinceid");
            translognumber= getIntent().getExtras().getString("translognumber");
            uploadflag= getIntent().getExtras().getString("uploadflag");
            account= getIntent().getExtras().getString("account");
            initQtPatParams();
            tv_idcardid.setText(StringUnit.cardJiaMi(account));
            swiper_check_nextbtn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    swiperMsgUpLoad();
                }
            });

//            1 上传交易卡照片；2 上传四要素及照片，3 只上传4要素信息；
            if("1".equals(uploadflag)){
                swiper_msg_layout.setVisibility(View.GONE);
                swiper_photo_layout.setVisibility(View.VISIBLE);
            }else if("2".equals(uploadflag)){
                swiper_msg_layout.setVisibility(View.VISIBLE);
                swiper_photo_layout.setVisibility(View.VISIBLE);
            }else if("3".equals(uploadflag)){
                swiper_msg_layout.setVisibility(View.VISIBLE);
                swiper_photo_layout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            swiper_msg_layout.setVisibility(View.GONE);
            swiper_photo_layout.setVisibility(View.GONE);
            swiper_check_nextbtn.setVisibility(View.GONE);
            LogUtil.showToast(SwiperMsgUploadActivity.this, "参数传递异常");
            e.printStackTrace();
        }
    }
    @Click(R.id.swiper_check_nextbtn2)
    public void swiper_check_nextbtn2()
    {
        Intent intent = new Intent(SwiperMsgUploadActivity.this, Swiper.class);
        intent.putExtra("orderinfo",orderinfo);
        intent.putExtra("cardInfo",cardInfo);
        intent.putExtra("orderid",orderid);
        intent.putExtra("cardpsw",cardpsw);
        intent.putExtra("icdata",icdata);
        intent.putExtra("serialnum",serialnum);
        intent.putExtra("cardtype",cardtype);
        intent.putExtra("baseprovinceid",baseprovinceid);
        intent.putExtra("translognumber",translognumber);
        LogUtil.showToast(SwiperMsgUploadActivity.this,"测试.");
        setResult(RyxAppconfig.QT_RESULT_OK, intent);
        finish();
    }

    private void swiperMsgUpLoad(){
                String smscode= et_smscode.getText().toString();
                if(checkInput()){
                    if((("2".equals(uploadflag)||"3".equals(uploadflag)))&&(TextUtils.isEmpty(smscode)||smscode.length()!=4)){
                        LogUtil.showToast(SwiperMsgUploadActivity.this,"请正确填写验证码!");
                        return;
                    }
                    if("3".equals(uploadflag)){
                        httpStripeCardUpload("");
                    }else{
                        showLoading();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] bitmapBytes = BitmapUntils.getContentbyCameraPix(myBitmap);
                                Message msg = new Message();
                                msg.what=0x901;
                                Bundle bundle=new Bundle();
                                bundle.putString("handpic",  BitmapUntils
                                        .changeBytesToHexString(bitmapBytes));
                                msg.setData(bundle);
                                mhandler.sendMessage(msg);
                            }
                        }).start();
                    }
                }
    }
    /**
     * 初始化网络请求参数
     */
    public void initQtPatParams() {
        // TODO Auto-generated method stub
        super.initQtPatParams();
        CryptoUtils.getInstance().setTransLogUpdate(true);// translogno 更新
        qtpayApplication = new Param("application");
    }
    Timer timer = new Timer();
    Handler timehandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what > 0) {
                tv_prompt.setTextColor(getResources().getColor(R.color.text_a));
                tv_prompt.setText(getResources().getString(R.string.resend)
                        + "(" + msg.what + ")");
                tv_prompt.setClickable(false);
            } else {
                timer.cancel();
                tv_prompt.setText(getResources().getString(
                        R.string.resend_verification_code));
                tv_prompt.setClickable(true);
                tv_prompt.setTextColor(getResources().getColor(R.color.secondblack));
            }
        };
    };
    @Click(R.id.tv_prompt)
    public void tv_promptClick(){
        if(checkInput()){
            et_smscode.setText("");
            sendSMS();
            timer = null;
            timer = new Timer();
            startCountdown();   // 开始倒计时
        }
    }


    /**
     * 发送验证码
     */
    private void sendSMS() {

        qtpayApplication.setValue("SendAdvancedVipSMS.Req");
        Param phoneParam=new Param("bankTel");
        String phoneNumber=et_phoneno.getText().toString();
        phoneParam.setValue(phoneNumber);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(phoneParam);
        httpsPost("SendAdvancedVipSMSTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                //获取验证码成功
                Toast.makeText(SwiperMsgUploadActivity.this, getResources().getString(R.string.sms_has_been_issued_please_note_that_check), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 开始倒计时60秒
     */
    public void startCountdown() {
        TimerTask task = new TimerTask() {
            int secondsRremaining = 60;

            public void run() {
                Message msg = new Message();
                msg.what = secondsRremaining--;
                timehandler.sendMessage(msg);
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    @Click(R.id.iv_touxiang)
    public void touxiangTakePhoto(){
        takePic();
    }
    /**
     * 拍照
     */
    public void takePic() {
        String   waring = MessageFormat.format(getResources().getString(R.string.camerawritefilewaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        requesDevicePermission(waring, 0x00011, new PermissionResult() {
            @Override
            public void requestSuccess() {
                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");
                    imgTempName = "/temp_" + format2.format(new Date()) + ".jpg";
                    PreferenceUtil.getInstance(getApplicationContext()).saveString(
                            "imgTempName", imgTempName);
                    File f = new File(Environment.getExternalStorageDirectory(),
                            imgTempName);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, UriUtils.fromFile(f,SwiperMsgUploadActivity.this));
                    intent.putExtra("scale", true);
                    startActivityForResult(intent, TAKE_PHOTO);
                } else {
                    LogUtil.showToast(SwiperMsgUploadActivity.this, "请检查SD卡是否正常!");
                }
            }

            @Override
            public void requestFailed() {

            }
        }, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    @Override
    protected void backUpImgOnclickListen() {
        setResult(RyxAppconfig.QT_RESULT_STRIPECARD_CANCEL );
        finish();
    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {

        switch (keycode) {
            case KeyEvent.KEYCODE_BACK: {
                setResult(RyxAppconfig.QT_RESULT_STRIPECARD_CANCEL );
                finish();
                return true;
            }
            default:
                return super.onKeyDown(keycode, event);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        LogUtil.showLog("requestCode=="+requestCode+",resultCode="+resultCode);
        // 检查数据来源；
        switch (requestCode) {
            case TAKE_PHOTO: // 相机返回
            {
                if (resultCode == RESULT_OK) {
                    LogUtil.showLog("相机拍照完毕==========================="+imgTempName);
                    showPicFromCamera(null);
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void showPicFromCamera(String filePath) {

        if(TextUtils.isEmpty(filePath)) {
            imgTempName = Environment.getExternalStorageDirectory() +
                    PreferenceUtil.getInstance(getApplicationContext()).getString("imgTempName", "");
        }else {
            imgTempName = filePath;
        }
        Glide.with(SwiperMsgUploadActivity.this)
        .load(imgTempName)
        .asBitmap()
        .fitCenter()
        .into(new SimpleTarget(PhoneinfoUtils.getWindowsWidth(SwiperMsgUploadActivity.this), PhoneinfoUtils.getWindowsHight(SwiperMsgUploadActivity.this)) {

            @Override
            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                myBitmap=(Bitmap) resource;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(myBitmap!=null){
                            iv_touxiang.setImageBitmap(myBitmap);
                        }else{
                            LogUtil.showToast(SwiperMsgUploadActivity.this, "拍照失败请重新拍照!");
                        }
                    }
                });
            }
        });
    };

    /**
     * 内容填写情况
     * @return
     */
    private boolean checkInput(){
        String pname=	et_pname.getText().toString();
        String pid=	et_pid.getText().toString();
        String phoneno=	et_phoneno.getText().toString();
            if("2".equals(uploadflag)||"3".equals(uploadflag)){
                // 2 上传四要素及照片
                if(TextUtils.isEmpty(pname)){
                    LogUtil.showToast(SwiperMsgUploadActivity.this, "请输入交易卡卡主姓名!");
                    return false;
                }
                if(!IDCardUtil.isIDCard(pid)){
                    LogUtil.showToast(SwiperMsgUploadActivity.this, "请正确输入交易卡卡主身份证号!");
                    return false;
                }
                if(TextUtils.isEmpty(phoneno)||phoneno.length()!=11){
                    LogUtil.showToast(SwiperMsgUploadActivity.this, "请正确输入交易卡银行预留手机号!");
                    return false;
                }

             }
        if("1".equals(uploadflag)||"2".equals(uploadflag)){
            //上传交易卡照片
            if(myBitmap==null){
                LogUtil.showToast(SwiperMsgUploadActivity.this, "请按照要求上传交易卡照片!");
                return false;
            }
        }
        return true;
    }

    /**
     * 上传资料
     */
    private void httpStripeCardUpload(String handpicData){
        //资料上传
        qtpayApplication.setValue("StripeCardUpload.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("orderId",orderid));
        qtpayParameterList.add(new Param("account",account));
        qtpayParameterList.add(new Param("uploadflag",uploadflag));
        if("1".equals(uploadflag)||"2".equals(uploadflag)){
            qtpayParameterList.add(new Param("cardImg",handpicData));
        }
        if("2".equals(uploadflag)||"3".equals(uploadflag)){
            qtpayParameterList.add(new Param("phoneNo",et_phoneno.getText().toString()));
            qtpayParameterList.add(new Param("customerPid",et_pid.getText().toString()));
            qtpayParameterList.add(new Param("name",et_pname.getText().toString()));
        }
        qtpayParameterList.add(new Param("smsCode",et_smscode.getText().toString()));
        httpsPost("StripeCardUploadTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                String jsonDataStr= payResult.getData();
                try {
                    JSONObject jsonObj = new JSONObject(jsonDataStr);
                    String code= JsonUtil.getValueFromJSONObject(jsonObj,"code");
                    if(RyxAppconfig.QTNET_SUCCESS.equals(code)){
                        Intent intent = new Intent(SwiperMsgUploadActivity.this, Swiper.class);
                        intent.putExtra("orderinfo",orderinfo);
                        intent.putExtra("cardInfo",cardInfo);
                        intent.putExtra("orderid",orderid);
                        intent.putExtra("cardpsw",cardpsw);
                        intent.putExtra("icdata",icdata);
                        intent.putExtra("serialnum",serialnum);
                        intent.putExtra("cardtype",cardtype);
                        intent.putExtra("baseprovinceid",baseprovinceid);
                        intent.putExtra("translognumber",translognumber);
                        LogUtil.showToast(SwiperMsgUploadActivity.this,"资料上传成功,请继续交易.");
                        setResult(RyxAppconfig.QT_RESULT_OK, intent);
                        finish();
                    }else{
                        String msg= JsonUtil.getValueFromJSONObject(jsonObj,"msg");
                        LogUtil.showToast(SwiperMsgUploadActivity.this,msg+"");
                    }
                }catch (Exception e){
                    LogUtil.showToast(SwiperMsgUploadActivity.this,"后台返回数据异常,请稍后再试.");
                }

            }
        });
    }
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0x901:
                    Bundle dataBundle=	msg.getData();
                    String handpicData= dataBundle.getString("handpic","");
                    cancleLoading();
                    httpStripeCardUpload(handpicData);
                    break;
            }
        }

    };
}
