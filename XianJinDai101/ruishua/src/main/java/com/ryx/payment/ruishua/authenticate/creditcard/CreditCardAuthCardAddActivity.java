package com.ryx.payment.ruishua.authenticate.creditcard;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.Cardno_;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import exocr.bankcard.BankManager;
import exocr.bankcard.DataCallBack;
import exocr.bankcard.EXBankCardInfo;

/**
 * 高级认证第一步卡号验证
 */
@EActivity(R.layout.activity_credit_card_auth_card_add)
public class CreditCardAuthCardAddActivity extends BaseActivity implements DataCallBack {

    @ViewById(R.id.et_cardno)
    EditText  et_cardno;

    @ViewById(R.id.credit_card_swiperimg)
    ImageView credit_card_swiperimg;

    @ViewById(R.id.btn_next)
    Button btn_next;
    private String bankcardid = "";
    private int SWIPING_CARD = 50;
    private String cardno = "", expirydate = "";
    @AfterViews
    public void initView(){
        setTitleLayout("信用卡认证", true, false);
        bankcardid = getIntent().getStringExtra("BankcardId");
        initQtPatParams();


    }
    @Click(R.id.credit_card_swiperimg)
    public void  creditCardSwiperImg(){
        showBottomDialog();
    }

    private BottomSheetDialog mBottomSheetDialog;

    private void showBottomDialog() {
        mBottomSheetDialog = new BottomSheetDialog(CreditCardAuthCardAddActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(CreditCardAuthCardAddActivity.this).inflate(R.layout.dialog__bottom_get_cardno, null);
        android.widget.Button photoBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_photo);
        android.widget.Button swipeBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_swipe);
        android.widget.Button cancelBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_cancel);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mBottomSheetDialog.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                identifyCard(v);
                            }
                        });
                    }
                }).start();
            }
        });
        swipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                requestCode = 1;
                String  warning = "请在\"设置\"里，打开\""+ RyxAppdata.getInstance(CreditCardAuthCardAddActivity.this).getCurrentBranchName()+"\"的定位和蓝牙权限，否则您将无法连接蓝牙设备";
                requesDevicePermission(warning, requestCode, permissionResult, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION});
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
    public void identifyCard(View view) {
        disabledTimerView(view);
        String warning  = "请在\"设置\"里，打开\""+RyxAppdata.getInstance(this).getCurrentBranchName()+"\"的拍照权限，否则您将无法拍照";
        requesDevicePermission(warning, requestCode, photo_permissionResult, Manifest.permission.CAMERA);

    }
    private PermissionResult photo_permissionResult = new PermissionResult() {
        @Override
        public void requestSuccess() {
            BankManager.getInstance().setShowPhoto(false);
            BankManager.getInstance().showLogo(false);
            BankManager.getInstance().recognize(CreditCardAuthCardAddActivity.this,CreditCardAuthCardAddActivity.this);

        }

        @Override
        public void requestFailed() {

        }
    };


    //识别到了银行卡片信息
    @Override
    public void onBankCardDetected(boolean success) {
        BankManager.getInstance().stopRecognize();
        if (success){
            EXBankCardInfo cardInfo = BankManager.getInstance().getCardInfo();
            if(cardInfo!=null){
                et_cardno.setText(cardInfo.strNumbers);
                et_cardno.setSelection(et_cardno.length());
                if(!"0/0".equals(cardInfo.strValid)){
                    expirydate=cardInfo.strValid;
                }
            }
        }
    }

    @Override
    public void onCameraDenied() {

    }
    private PermissionResult permissionResult = new PermissionResult() {
        @Override
        public void requestSuccess() {
            //跳转刷卡页面
            startActivityForResult(new Intent(CreditCardAuthCardAddActivity.this,
                            Cardno_.class).putExtra("CARDNOType", "SENIOR"),
                    SWIPING_CARD);
        }
        @Override
        public void requestFailed() {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RyxAppconfig.QT_RESULT_OK) {
            cardno = data.getExtras().getString("result");
            expirydate = data.getExtras().getString("expirydate");
            et_cardno.setText(cardno);
            et_cardno.setSelection(cardno.length());
        }
    }
    @Click(R.id.btn_next)
    public void nextStep() {
        final String cardNumber=et_cardno.getText().toString();
        if(TextUtils.isEmpty(cardNumber)){
            LogUtil.showToast(CreditCardAuthCardAddActivity.this,"请输入卡号或刷卡获取!");
            return;

        }
        qtpayApplication = new Param("application", "CardType.Req");
        Param qtpayCardNo = new Param("cardNo");
        qtpayCardNo.setValue(cardNumber.trim());
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardNo);
        qtpayParameterList.add(new Param("bindType","vip"));
        httpsPost("getCardType", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    JSONObject jsonObject = new JSONObject(payResult.getData());
                String cardType = jsonObject.getString("cardtype");

                    String kjzf_status = jsonObject.getString("kjzf_status");
//                    String needvaliddate = jsonObject.getString("needvaliddate");
                    String needcvn = jsonObject.getString("needcvn");

                    if(cardType.equals("03")){
                    //信用卡
                        Intent intent=new Intent(CreditCardAuthCardAddActivity.this,CreditCardAuthStep2Activity_.class);
                        intent.putExtra("cardNumber",cardNumber);
                        intent.putExtra("expirydate",expirydate);
                        intent.putExtra("kjzf_status",kjzf_status);
                        intent.putExtra("needcvn",needcvn);
                        startActivityForResult(intent,0x001);
                    }else{
                        LogUtil.showToast(CreditCardAuthCardAddActivity.this, "该卡不是信用卡,请更换卡号!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.showToast(CreditCardAuthCardAddActivity.this, "数据解析异常！");
                }
            }
        });


    }
}
