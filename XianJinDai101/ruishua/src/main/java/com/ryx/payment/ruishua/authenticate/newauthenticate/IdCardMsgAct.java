package com.ryx.payment.ruishua.authenticate.newauthenticate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/*import com.hisign.CTID.facelivedetection.CTIDLiveDetectActivity;
import com.hisign.CTID.utilty.ToolsUtilty;*/
import com.livedetect.LiveDetectActivity;
import com.livedetect.utils.StringUtils;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.authenticate.newauthenticate.newcreditcard.CreditAddCardNumberAct_;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.UserAuthResultDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.IDCardUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.swiper.utils.CryptoUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.text.MessageFormat;

/**
 * 身份证信息提交
 */
@EActivity(R.layout.activity_id_card_msg)
public class IdCardMsgAct extends BaseActivity {
    private final int START_LIVEDETECT = 0;
    private final int START_CTIDLIVEDETECT = 1;
    UserAuthResultDialog userAuthSuccessDialog;
    String cardnum,userName,validDate,livePic,livePicMd5,idCardFront,idCardBack,idcardFrontMd5,idCardBackbyteMd5;
    @ViewById
    EditText edt_userName,idcard_number_et,validDateStart_et;
//    private int beforevalidDateLength;
    private final int ADDCARD = 22;
    MediaPlayer mediaPlayerfail;
    @AfterViews
    public void initView() {
        setTitleLayout("实名认证", true, false);
        initDataView();
        userAuthSuccessDialog=new UserAuthResultDialog(IdCardMsgAct.this);
        initQtPatParams();

    }
    //调用后台接口判断走的采集流程
    private void initPidType() {
        qtpayApplication.setValue("PidType.Req");
        qtpayParameterList.add(new Param("idCardNum",idcard_number_et.getText().toString()));
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("PidTypeTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {

                //判断接口返回数据结果
                String data=payResult.getData();
                try {
                    JSONObject dataJsonObj=new JSONObject(data);
                    JSONObject resultObj= JsonUtil.getJSONObjectFromJsonObject(dataJsonObj,"result");
                    final String type=JsonUtil.getValueFromJSONObject(resultObj,"type");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            type（1--请求江苏法华OCR；0--请求海鑫OCR,默认都是走海鑫的）
                            if(Integer.parseInt(type)==1){
                              //  initCTIDLive();
                                //startCTIDLiveDetect();
                            }else{
                                startHaiXinLiveDetect();
                            }

                        }
                    });

                }catch (Exception e){
                    startHaiXinLiveDetect();
                }
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                startHaiXinLiveDetect();
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                startHaiXinLiveDetect();
            }
        });
    }

/*    private void initCTIDLive(){
        mediaPlayerfail = MediaPlayer.create(getApplicationContext(),
                ToolsUtilty.getResIdByTypeAndName(getApplicationContext(),
                        "raw", "ctidfail"));
    }*/

    private void initDataView() {
//        validDateStart_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                LogUtil.showLog("beforeTextChanged=="+start+"=="+count+"=="+after);
//                if(TextUtils.isEmpty(s)){
//                    beforevalidDateLength=0;
//                }else{
//                    beforevalidDateLength=s.length();
//                }
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                LogUtil.showLog("onTextChanged=="+start+"=="+before+"=="+count);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                LogUtil.showLog("afterTextChanged=="+s.toString());
//                StringBuffer stringBuffer=new StringBuffer();
//                if(!TextUtils.isEmpty(s)){
//                    if(s.length()<beforevalidDateLength){
//                        return;
//                    }
//
//                    String currentStr=s.toString().replace("/","").replace("-","").replace(".","");
//                        if(currentStr.length()<4){
//                            return;
//                        }
//                    if(currentStr.length()>=4){
//                        stringBuffer.append(currentStr.substring(0,4));
//                        stringBuffer.append("/");
//                        if(currentStr.length()<6){
//                            stringBuffer.append(currentStr.substring(4,currentStr.length()));
//                        }
//                    }
//
//
//                    if(currentStr.length()>=6){
//                        stringBuffer.append(currentStr.substring(4,6));
//                        stringBuffer.append("/");
//                        if(currentStr.length()<8){
//                            stringBuffer.append(currentStr.substring(6,currentStr.length()));
//                        }
//                    }
//                        if(currentStr.length()>=8){
//                            stringBuffer.append(currentStr.substring(6,8));
//                            stringBuffer.append("-");
//                            if(currentStr.length()<12){
//                                stringBuffer.append(currentStr.substring(8,currentStr.length()));
//                            }
//                        }
//                        if(currentStr.length()>=12){
//                            stringBuffer.append(currentStr.substring(8,12));
//                            stringBuffer.append("/");
//                            if(currentStr.length()<14){
//                                stringBuffer.append(currentStr.substring(12,currentStr.length()));
//                            }
//                        }
//                        if(currentStr.length()>=14){
//                            stringBuffer.append(currentStr.substring(12,14));
//                            stringBuffer.append("/");
//                            if(currentStr.length()<16){
//                                stringBuffer.append(currentStr.substring(14,currentStr.length()));
//                            }
//                        }
//                        if(currentStr.length()>=16){
//                            stringBuffer.append(currentStr.substring(14,16));
//                        }
//                        if(!stringBuffer.toString().equals(s.toString())){
//                            validDateStart_et.setText(stringBuffer.toString());
//                            validDateStart_et.setSelection(stringBuffer.length());
//                        }
//                }
//            }
//        });


        Intent getintent=getIntent();
        cardnum= getintent.getStringExtra("cardnum");
        userName= getintent.getStringExtra("userName");
        validDate= getintent.getStringExtra("validDate");
        idCardFront= getintent.getStringExtra("idCardFront");
        idCardBack= getintent.getStringExtra("idCardBack");
        idcardFrontMd5= getintent.getStringExtra("idcardFrontMd5");
        idCardBackbyteMd5= getintent.getStringExtra("idCardBackbyteMd5");

        edt_userName.setText(userName);
        edt_userName.setSelection(userName.length());
        idcard_number_et.setText(cardnum);
        LogUtil.showLog("validDate=="+validDate);
        try {
        if(validDate.contains("长期")){
            String  startDate= DateUtil.StrToDateStr(validDate.substring(0,8),"yyyyMMdd","yyyy/MM/dd");
            validDateStart_et.setText(startDate+"-"+"长期");
        }else{
           String start1= DateUtil.StrToDateStr(validDate.substring(0,8),"yyyyMMdd","yyyy/MM/dd");
           String end1= DateUtil.StrToDateStr(validDate.substring(8),"yyyyMMdd","yyyy/MM/dd");
            LogUtil.showLog("start1=="+start1+",end1=="+end1);
            validDateStart_et.setText(start1+"-"+end1);
        }
        }catch (Exception e){
            validDateStart_et.setText("");
        }
    }

    @Click(R.id.btn_next)
    public void btnNext() {
        if(disabledTimerAnyView()){
            return;
        }
       String edt_userNameVar= edt_userName.getText().toString();
        if(TextUtils.isEmpty(edt_userNameVar)){
            LogUtil.showToast(IdCardMsgAct.this,"请正确输入用户名");
            return;
        }
        String idcard_number_etVar= idcard_number_et.getText().toString();
        if(!IDCardUtil.isIDCard(idcard_number_etVar)){
            LogUtil.showToast(IdCardMsgAct.this,"请正确输入身份证号");
            return;
        }
//        String validDateStart_etVar= validDateStart_et.getText().toString();
//        if(TextUtils.isEmpty(validDateStart_etVar)||validDateStart_etVar.length()!=21){
//            LogUtil.showToast(IdCardMsgAct.this,"请正确输入有效期");
//            return;
//        }
        takephoto();

    }


    public void takephoto() {
        String waring = MessageFormat.format(getResources().getString(com.ryx.ryxcredit.R.string.camerawaringmsg), RyxAppdata.getInstance(IdCardMsgAct.this).getCurrentBranchName());
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
        String waring = MessageFormat.format(getResources().getString(com.ryx.ryxcredit.R.string.dirwaringmsg), RyxAppdata.getInstance(IdCardMsgAct.this).getCurrentBranchName());
        requesDevicePermission(waring, 0x00012, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                            initPidType();
                        }
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 海鑫活体采集
     */
    private void startHaiXinLiveDetect(){
        Intent intent = new Intent(IdCardMsgAct.this,
                LiveDetectActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, START_LIVEDETECT);
    }

    /**
     *江苏法华活体采集
     */
/*    private void startCTIDLiveDetect() {
        Intent intent = new Intent();
        intent.setClass(IdCardMsgAct.this, CTIDLiveDetectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mIdCodStr", "");
            bundle.putByteArray("randomNomber", null);
        intent.putExtras(bundle);
        startActivityForResult(intent, START_CTIDLIVEDETECT);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        LogUtil.showLog("onActivityResult----", " 109 requestCode = " + requestCode + " resultCode = " + resultCode + "---" + intent);
        if (requestCode == START_LIVEDETECT) {
            switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
                case RESULT_OK:
                    if (null != intent) {
                        Bundle result = intent.getBundleExtra("result");
                        if (null != result) {
                            //只有失败时返回  失败动作  l代表静止凝视动作  s代表摇头动作，n代表点头动作
                            String mMove = result.getString("mMove");
                            //只有失败时返回  失败的原因: 0代表默认失败提示，1代表无人脸，2代表多人脸，3代表活检动作失败，4代表错误动作的攻击，5代表超时，6代表图片加密失败，7代表3D检测失败，8代表肤色检测失败
                            String mRezion = result.getString("mRezion");
                            //活检是否通过 true代表检测通过，false代表检测失败
                            boolean isLivePassed = result.getBoolean("check_pass");
                            if (StringUtils.isNotNull(mMove)) {

                            }
                            if (StringUtils.isNotNull(mRezion)) {//10为 初始化 失败 ，11为授权过期
                            }
                            byte[] picbyte = result.getByteArray("pic_result");
                            //识别成功
                            if (isLivePassed) {
                                byte[] compressionPic= BitmapUntils.getContent(70,picbyte);
//                                imgview.setImageBitmap(BitmapFactory.decodeByteArray(compressionPic, 0, compressionPic.length));
                                //图片的byte[]的形式，格式为jpg。失败时返回值为null
                                livePic=   BitmapUntils.bytesToHexString(compressionPic);
                                livePicMd5= CryptoUtils.getInstance().EncodeDigest(compressionPic);
                                httpCustomerAuthByLive();
                            } else {
                                showLivedetectFailWindow("照片采集失败","请按照采集提示进行展示对应动作");
                            }
                        }
                    }
                    break;
            }
        }else if(ADDCARD==requestCode){
            if(resultCode==RyxAppconfig.CLOSE_ALL){
                setResult(RyxAppconfig.CLOSE_ALL);
            }else{
                setResult(RyxAppconfig.CLOSE_NEWAUTHRESULT_RSP);
            }
            finish();
        }else  if (requestCode == START_CTIDLIVEDETECT){

//            // randomNomber = new byte[1];
            if (resultCode == Activity.RESULT_OK && intent != null) {

                Bundle result = intent.getBundleExtra("result");
                boolean check_pass = result.getBoolean("check_pass");

                String mBadReason = result.getString("mBadReason","");

                if (check_pass) {
                    byte[] pic_thumbnail = result.getByteArray("pic_thumbnail");
                    byte[] pic_result = result.getByteArray("encryption");
                     String pic_encryption = result.getString("encryption");
                    // for (byte b: pic_result) {
                    //
                    // Log.d(TAG32, b+"");
                    // }
                    // Log.i("mBadReason", "mBadReason=");
                    if (pic_thumbnail != null) {
                        byte[] compressionPic= BitmapUntils.getContent(100,pic_thumbnail);
//                                imgview.setImageBitmap(BitmapFactory.decodeByteArray(compressionPic, 0, compressionPic.length));
                        //图片的byte[]的形式，格式为jpg。失败时返回值为null
                        livePic=   BitmapUntils.bytesToHexString(compressionPic);
                        livePicMd5= CryptoUtils.getInstance().EncodeDigest(compressionPic);
                        httpCustomerAuthByLive();
                    } else {
                        showLivedetectFailWindow("照片采集失败","请按照采集提示进行展示对应动作");
                    }

                } else {

                    if (mBadReason.equalsIgnoreCase("001")) {
                        showLivedetectFailWindow("照片采集失败","请确保人脸始终在屏幕中");
                    } else if (mBadReason.equalsIgnoreCase("002")) {
                        showLivedetectFailWindow("照片采集失败","请确保屏幕中只有一张脸");
                    } else if (mBadReason.equalsIgnoreCase("003")) {
                        showLivedetectFailWindow("照片采集失败","您的动作不符合");
                    } else if (mBadReason.equalsIgnoreCase("004")) {
                        showLivedetectFailWindow("照片采集失败","您的照片损坏太大");
                    } else if (mBadReason.equalsIgnoreCase("005")) {
                        showLivedetectFailWindow("照片采集失败","您周围的环境光线过暗");
                    } else if (mBadReason.equalsIgnoreCase("006")) {
                        showLivedetectFailWindow("照片采集失败","您周围的环境光线过亮");
                    } else if (mBadReason.equalsIgnoreCase("007")) {
                        showLivedetectFailWindow("照片采集失败","活检受到攻击");
                    } else if (mBadReason.equalsIgnoreCase("008")) {
                        showLivedetectFailWindow("照片采集失败","采集超时");
                    } else if (mBadReason.equalsIgnoreCase("009")) {
                        showLivedetectFailWindow("照片采集失败","采集中获取随机数失败");
                    } else if (mBadReason.equalsIgnoreCase("101")) {
                        showLivedetectFailWindow("照片采集失败","请您保持静止不动");
                    } else if (mBadReason.equalsIgnoreCase("010")) {
                        showLivedetectFailWindow("照片采集失败","采集获取认证证书失败");
                    } else {
                        showLivedetectFailWindow("照片采集失败","请按照采集提示进行展示对应动作");
                    }
                }

            }

        }
    }

    /**
     *实名认证接口
     */
    private void httpCustomerAuthByLive() {
        initQtPatParams();
        qtpayApplication.setValue("CustomerAuthByLive.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("validDateStart",validDate.substring(0,8)));
        qtpayParameterList.add(new Param("validDateEnd",validDate.substring(8)));
        qtpayParameterList.add(new Param("username",edt_userName.getText().toString()));
        qtpayParameterList.add(new Param("idCardNum",idcard_number_et.getText().toString()));
        qtpayParameterList.add(new Param("customerPidType","01"));
        qtpayParameterList.add(new Param("customerType","00"));
        qtpayParameterList.add(new Param("mobileNo","00"));

        qtpayParameterList.add(new Param("idCardFrount",idCardFront));

        qtpayParameterList.add(new Param("idCardReverse",idCardBack));
        qtpayParameterList.add(new Param("livePic",livePic));

        qtpayParameterList.add(new Param("pidImgSign",idcardFrontMd5));
        qtpayParameterList.add(new Param("pidAntiImgSign",idCardBackbyteMd5));
        qtpayParameterList.add(new Param("picSign",livePicMd5));

//        LogUtil.showLog("idCardFront=="+idCardFront);
//        LogUtil.showLog("idCardReverse=="+idCardBack);
//        LogUtil.showLog("livePic=="+livePic);
//        LogUtil.showLog("pidImgSign=="+idcardFrontMd5);
//        LogUtil.showLog("pidAntiImgSign=="+idCardBackbyteMd5);
//        LogUtil.showLog("picSign=="+livePicMd5);
    httpsPost("CustomerAuthByLiveTag", new XmlCallback() {
        @Override
        public void onTradeSuccess(RyxPayResult payResult) {
            String data=payResult.getData();
            try {
                JSONObject jsonObject=  new JSONObject(data);
                String code= JsonUtil.getValueFromJSONObject(jsonObject,"code");
                String msg= JsonUtil.getValueFromJSONObject(jsonObject,"msg");
                if(RyxAppconfig.QTNET_SUCCESS.equals(code)){
                   JSONObject resultObj= JsonUtil.getJSONObjectFromJsonObject(jsonObject,"result");
                    String authenflag=JsonUtil.getValueFromJSONObject(resultObj,"authenflag");
                    String username=JsonUtil.getValueFromJSONObject(resultObj,"username");
                    RyxAppdata.getInstance(IdCardMsgAct.this).setRealName(username);
                    RyxAppdata.getInstance(IdCardMsgAct.this).setAuthenFlag(Integer.parseInt(authenflag));
                  showCustomerAuthSuccessWindow("恭喜你! 认证通过!","完成高级认证可以大幅增加交易额度，赶快认证吧！");
                }else if("1000".equals(code)){
                    showCustomerAuthFailWindow("对不起! 认证失败!",msg);
                }else if("9999".equals(code)){
                    showCustomerAuthProcessWindow("正在人工审核中...",msg);
                }
            }catch (Exception e){
                LogUtil.showToast(IdCardMsgAct.this,"数据错误，请稍后重试!");
            }
        }
        @Override
        public void onOtherState() {
            super.onOtherState();

        }
    });

    }


    /**
     *活体采集失败提示
     * @param title
     * @param content
     */
    private void showLivedetectFailWindow(String title,String content) {
        userAuthSuccessDialog.showDefinedDialog(R.drawable.dialog_authresult_fail, title, content, "重新采集", new UserAuthResultDialog.UserAuthResultDialogBtnListen() {
            @Override
            public void btnOkClick(View view) {
                userAuthSuccessDialog.dismiss();
                takephoto();
            }

            @Override
            public void btnReturnClick(View view) {
                userAuthSuccessDialog.dismiss();
            }
        },"取消");
    }
    private void showCustomerAuthSuccessWindow(String title,String content){
        userAuthSuccessDialog.showDefinedDialog(R.drawable.dialog_userauth_success_img, title, content, "立即认证", new UserAuthResultDialog.UserAuthResultDialogBtnListen() {
            @Override
            public void btnOkClick(View view) {
                userAuthSuccessDialog.dismiss();
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Intent intent = new Intent(IdCardMsgAct.this,
                               CreditAddCardNumberAct_.class);
                       intent.putExtra("BankcardId", "");
                       startActivityForResult(intent, ADDCARD);
                   }
               });
            }

            @Override
            public void btnReturnClick(View view) {
                userAuthSuccessDialog.dismiss();
                setResult(RyxAppconfig.CLOSE_ALL);
                finish();
            }
        },"返回首页");
    }

    /**
     * 实名认证失败重新认证
     * @param title
     * @param content
     */
    private void showCustomerAuthFailWindow(String title,String content){
        userAuthSuccessDialog.showDefinedDialog(R.drawable.dialog_authresult_fail, title, content, "重新认证", new UserAuthResultDialog.UserAuthResultDialogBtnListen() {
            @Override
            public void btnOkClick(View view) {
                userAuthSuccessDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }

            @Override
            public void btnReturnClick(View view) {
                userAuthSuccessDialog.dismiss();
                setResult(RyxAppconfig.CLOSE_ALL);
                finish();
            }
        },"返回首页");
    }
    /**
     * 实名认证处理中
     * @param title
     * @param content
     */
    private void showCustomerAuthProcessWindow(String title,String content){
        userAuthSuccessDialog.showDefinedDialog(R.drawable.dialog_authresult_processing, title, content, "好的", new UserAuthResultDialog.UserAuthResultDialogBtnListen() {
            @Override
            public void btnOkClick(View view) {
                userAuthSuccessDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RyxAppconfig.CLOSE_NEWAUTHRESULT_RSP);
                        finish();
                    }
                });
            }

            @Override
            public void btnReturnClick(View view) {

            }
        },"");
    }

}