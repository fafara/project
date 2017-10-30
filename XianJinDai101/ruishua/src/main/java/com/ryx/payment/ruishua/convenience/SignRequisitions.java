package com.ryx.payment.ruishua.convenience;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bean.TradeDetailInfo;
import com.ryx.payment.ruishua.bean.TradeSignSlipInfo;
import com.ryx.payment.ruishua.dialog.ICSuccesDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ryxhttp.OkHttpUtils;
import com.ryx.payment.ryxhttp.callback.BitmapCallback;
import com.ryx.swiper.utils.MoneyEncoder;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

import okhttp3.Call;

/**
 * 签购单电子收据
 */
@EActivity(R.layout.activity_sign_requisitions)
public class SignRequisitions extends BaseActivity {
    TradeSignSlipInfo tradeSlipInfo;
    private int fromtype; // 用来判断是哪个页面跳转过来的
        @ViewById
        Button btn_save;
    @ViewById
    ImageView iv_signPic;
    @ViewById
    AutoLinearLayout sign_autoLinearLayout;
    ICSuccesDialog dialog_icsucces;
    String showunplugin;
    private byte[] signdata; // 用来接受传过来的图片字节数组
    Bitmap bitmap = null;
    TradeDetailInfo tradeDetailInfo;
    @ViewById
    TextView tv_Val_hostMerchantName,tv_Val_acqMerchantId,tv_Val_acqTermId,
            tv_Val_bankNo,tv_Val_account,tv_Val_acqBranchName,tv_Val_tradeType,tv_Val_valid,
            tv_Val_batchNo,tv_Val_hostLogNo,tv_Val_hostAuthCode,tv_Val_operatorNum,tv_Val_date,
            tv_Val_orderId,tv_Val_hostRefNo,tv_Val_amount,tv_Val_allaccount;

    @AfterViews
    public void afterViews(){
        try {
            setTitleLayout("签购单",true,false);
            fromtype = getIntent().getExtras().getInt("fromtype");
          Object detaiObject=  getIntent().getExtras().get("tradeDetailInfo");
            if(detaiObject!=null){
                //查看传递数据
                tradeDetailInfo = (TradeDetailInfo)detaiObject ;
            }
            Object tradeSlipObject=  getIntent().getExtras().get("tradeSlipInfo");
            if(tradeSlipObject!=null){
                //生成签购单传递数据
                tradeSlipInfo = (TradeSignSlipInfo)tradeSlipObject ;
            }
            showunplugin = getIntent().getStringExtra("showunplugin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ("show".equals(showunplugin)) {
            dialog_icsucces = new ICSuccesDialog(SignRequisitions.this,
                    R.style.mydialog);
            dialog_icsucces.show();
        }

        switch (fromtype) {
            case RyxAppconfig.CREATE_SIGNREQUISITIONS: // 生成签购单
                signdata = (byte[]) getIntent().getExtras().get("signarray");

                if (null != signdata) {
                    try {
                        bitmap = byteArrayToImage(signdata);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        iv_signPic.setImageBitmap(bitmap);
                    }
                }

                bandView();

                break;
            case RyxAppconfig.VIEW_SIGNREQUISITIONS: // 查看明细
                initQtPatParams();
                getSignSlip();

                break;
        }
    }

    private void bandView() {
        tv_Val_hostMerchantName.setText(DataUtil.getRightValue(tradeSlipInfo.getHostMerchantName()));
        tv_Val_acqMerchantId
                .setText(DataUtil.getRightValue(tradeSlipInfo.getAcqMerchantId()));
        tv_Val_acqTermId.setText(DataUtil.getRightValue(tradeSlipInfo.getAcqTermId()));
        tv_Val_account.setText(DataUtil.getRightValue(tradeSlipInfo.getAccount()));
        tv_Val_bankNo.setText(DataUtil.getRightValue(tradeSlipInfo.getBankName()).trim());
        tv_Val_acqBranchName.setText(DataUtil.getRightValue(tradeSlipInfo.getAcqBranchName()));
        tv_Val_tradeType.setText(DataUtil.getRightValue(tradeSlipInfo.getTradeType()));
       String val_valid= DataUtil.getRightValue(tradeSlipInfo.getValid());
        if(!TextUtils.isEmpty(val_valid)){
            tv_Val_valid.setText(val_valid);
        }
        tv_Val_batchNo.setText(DataUtil.getRightValue(tradeSlipInfo.getBatchNo()));
        tv_Val_hostLogNo.setText(DataUtil.getRightValue(tradeSlipInfo.getHostLogNo()));
       String hostAuthCode= DataUtil.getRightValue(tradeSlipInfo.getHostAuthCode());
        if(!TextUtils.isEmpty(hostAuthCode)){
            tv_Val_hostAuthCode.setText(hostAuthCode);
        }
        tv_Val_operatorNum.setText(DataUtil.getRightValue(tradeSlipInfo.getOperatorNum()));
      String dates = DataUtil.getRightValue(tradeSlipInfo.getDate());
        if(!TextUtils.isEmpty(dates)){
            tv_Val_date.setText(dates);
        }
        tv_Val_orderId.setText(DataUtil.getRightValue(tradeSlipInfo.getOrderId()));
        tv_Val_hostRefNo.setText(DataUtil.getRightValue(tradeSlipInfo.getHostRefNo()));
        tv_Val_amount.setText(MoneyEncoder.decodeFormat(
                DataUtil.getRightValue(tradeSlipInfo.getAmount())).replace("￥",
                "RMB:"));
        tv_Val_allaccount.setText(MoneyEncoder
                .decodeFormat(DataUtil.getRightValue(tradeSlipInfo.getAmount()))
                .replace("￥", "RMB:").replace(",", ""));

        if (tradeDetailInfo!=null&& null != tradeDetailInfo.getSignPic()
                && tradeDetailInfo.getSignPic().length() != 0) {

          String  signstring =DataUtil.getRightValue( tradeDetailInfo.getSignPic());
            if(TextUtils.isEmpty(signstring)||!signstring.contains("http")){
                return;
            }
            LogUtil.showLog("ryx","uitl=="+signstring);
//            Glide.with(this).load("https://mposprepo.ruiyinxin.com:4430/00800118/A000662000/A000662329/DFIMAGE/1471937015099A000662329.png").asBitmap().into(iv_signPic);
            OkHttpUtils.get().url(signstring).build().execute(new BitmapCallback() {
                @Override
                public void onError(Call call, Exception e) {
                        LogUtil.showLog("uitl=="+e.getLocalizedMessage());
                }
                @Override
                public void onResponse(Bitmap response) {
                    iv_signPic.setImageBitmap(response);
                }
            });
        }
    }
        @Click(R.id.btn_save)
    public void btn_saveClick(){
            dirpermissionCheck();
    }
    private Bitmap byteArrayToImage(byte[] byteArrayIn) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayIn, 0,
                byteArrayIn.length, options);
        if (bitmap != null)
            return bitmap;
        return null;

    }


    public void getSignSlip() {
        qtpayApplication = new Param("application", "GetSignSalesSlipInfo.Req");
        qtpayAttributeList.add(qtpayApplication);
        Param qtpayOrderno = new Param("orderId", DataUtil.getRightValue(tradeDetailInfo.getOrderId()) );

        Param qtpayFlag = new Param("flag", "0");

        qtpayParameterList.add(qtpayOrderno);
        qtpayParameterList.add(qtpayFlag);

        try {
            Thread.sleep(50); // 歇一会，等以上操作完成再启动线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       httpsPost("GetSignSalesSlipInfoTag", new XmlCallback() {
           @Override
           public void onTradeSuccess(RyxPayResult payResult) {
               getSaleSlipFromJson(payResult.getData());
               bandView();
           }
       });
    }

    /**
     * 从json数据中解析签购单的信息
     */
    public void getSaleSlipFromJson(String jsonstring) {

        if (jsonstring != null && jsonstring.length() > 0) {
            String toastmsg = "";

            try {
                JSONObject jsonObj = new JSONObject(jsonstring);
                JSONArray keys = jsonObj.getJSONArray("resultBean");
                tradeSlipInfo = new TradeSignSlipInfo();
                tradeSlipInfo.setHostMerchantName(JsonUtil
                        .getValueFromJSONObject(keys.getJSONObject(0),
                                "hostMerchantName"));
                tradeSlipInfo.setAcqMerchantId(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "acqMerchantId"));
                tradeSlipInfo.setAcqTermId(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "acqTermId"));
                tradeSlipInfo.setOperatorNum(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "operatorNum"));
                tradeSlipInfo.setBankNo(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "bankNo"));
                tradeSlipInfo.setBankName(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "bankName"));
                tradeSlipInfo.setAccount(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "account"));
                tradeSlipInfo.setAcqBranchName(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "acqBranchName"));
                tradeSlipInfo.setTradeType(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "tradeType"));
                tradeSlipInfo.setValid(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "valid"));
                tradeSlipInfo.setBatchNo(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "batchNo"));
                tradeSlipInfo.setHostLogNo(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "hostLogNo"));
                tradeSlipInfo.setHostAuthCode(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "hostAuthCode"));
                tradeSlipInfo.setDate(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "date"));
                tradeSlipInfo.setOrderId(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "orderId"));
                tradeSlipInfo.setHostRefNo(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "hostRefNo"));
                tradeSlipInfo.setAmount(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "amount"));
                tradeSlipInfo.setTradeType2(JsonUtil.getValueFromJSONObject(
                        keys.getJSONObject(0), "tradeType2"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

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
//                        if(RyxAppconfig.BRANCH.equals("01")){
//                            stringBuffer.append("ruishua/");
//                        }else if(RyxAppconfig.BRANCH.equals("02")){
//                            stringBuffer.append("ruiyinxin/");
//                        }
                        stringBuffer.append(RyxAppdata.getInstance(SignRequisitions.this).getCurrentFileBranchRootName());
                        stringBuffer.append("PaySign");
                        String ss = BitmapUntils.SaveAsFile(sign_autoLinearLayout,
                                stringBuffer.toString());
                        if(!TextUtils.isEmpty(ss)){
                            LogUtil.showToast(SignRequisitions.this, ss);
                        }
                    }

                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(SignRequisitions.this, finalWaring);
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        setResult(RyxAppconfig.CLOSE_AT_SWIPER);
    }
    @Override
    public void  backUpImgOnclickListen(){
        Intent resultIntent=new Intent();
        resultIntent.putExtra("swiperresult","01");
        setResult(RyxAppconfig.CLOSE_AT_SWIPER,resultIntent);
        finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent resultIntent=new Intent();
            resultIntent.putExtra("swiperresult","01");
            setResult(RyxAppconfig.CLOSE_AT_SWIPER,resultIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
