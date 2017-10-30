package com.ryx.payment.ruishua.bindcard;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.Cardno_;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
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
 * 添加银行卡
 *
 * @author muxin
 * @time 2016-09-14 8:46
 */
@EActivity(R.layout.activity_bank_card_add)
public class BankCardAddActivity extends BaseActivity implements DataCallBack {

    private static final int SWIPING_CARD = 50;
    private static final int ADDCARDINFO = 1;
    @ViewById(R.id.btn_next)
    Button mNextBtn;
    @ViewById(R.id.tv_userName)
    TextView mUserNameTv;
    @ViewById(R.id.edt_debitcardNo)
    EditText mDebitCardNoEt;
    @ViewById(R.id.iv_swipecard)
    ImageView mSwipeCardIv;
    private String cardId;
    private String realName;
    private BankCardInfo bankCardInfo;
    private Param qtpayCardNo;
    private String bankId;
    private String cardType;
    private String bankName;
    private String shortBankName;
    private String cardTypeTxt;

    @AfterViews
    public void initView() {
        initQtPatParams();
        setTitleLayout("添加银行卡", true);
        setRightImgMessage("支持绑定银行卡列表", RyxAppconfig.NOTES_SUPPORTBANKLIST);
        mUserNameTv.setText(QtpayAppData.getInstance(this).getRealName());
//        initCustomeLayout();
    }

    @Click(R.id.iv_swipecard)
    public void swipeCard() {
        showBottomDialog();
    }

    //打开银行卡识别功能
    public void identyCard(View view ) {
        disabledTimerView(view);
        String warning = "请在\"设置\"里，打开\"" + RyxAppdata.getInstance(this).getCurrentBranchName() + "\"的拍照权限，否则您将无法拍照";
        requesDevicePermission(warning, requestCode, photo_permissionResult, Manifest.permission.CAMERA);
    }

    private PermissionResult photo_permissionResult = new PermissionResult() {
        @Override
        public void requestSuccess() {
            BankManager.getInstance().showLogo(false);
            BankManager.getInstance().setShowPhoto(false);
            BankManager.getInstance().recognize(BankCardAddActivity.this, BankCardAddActivity.this);
        }

        @Override
        public void requestFailed() {

        }
    };
    private BottomSheetDialog mBottomSheetDialog;

    private void showBottomDialog() {
        mBottomSheetDialog = new BottomSheetDialog(BankCardAddActivity.this, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(BankCardAddActivity.this).inflate(R.layout.dialog__bottom_get_cardno, null);
        android.widget.Button photoBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_photo);
        android.widget.Button swipeBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_swipe);
        android.widget.Button cancelBtn = (android.widget.Button) boottomView.findViewById(R.id.btn_cancel);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                identyCard(v);
            }
        });
        swipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                startActivityForResult(new Intent(BankCardAddActivity.this, Cardno_.class), SWIPING_CARD);
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

    //识别到了银行卡片信息
    @Override
    public void onBankCardDetected(boolean success) {
        BankManager.getInstance().stopRecognize();
        if (success) {
            EXBankCardInfo cardInfo = BankManager.getInstance().getCardInfo();
            LogUtil.showLog("cardInfo", cardInfo.strValid + "----");
            if (cardInfo != null) {
                mDebitCardNoEt.setText(cardInfo.strNumbers);
            }
        }
    }

    @Override
    public void onCameraDenied() {

    }

    @Click(R.id.btn_next)
    public void btnClick() {
        String cardNo = mDebitCardNoEt.getText().toString().trim();
        if (!(cardNo.length() >= 14)) {
            LogUtil.showToast(this, getResources().getString(R.string.card_digit_error));
        } else {
            cardId = mDebitCardNoEt.getText().toString().trim() + "";
            realName = QtpayAppData.getInstance(this).getRealName();
            bankCardInfo = new BankCardInfo();
            bankCardInfo.setAccountNo(cardId);
            bankCardInfo.setName(realName);
            queryCardType();
        }
    }


    /**
     * 查询卡类型
     */
    private void queryCardType() {
        qtpayApplication = new Param("application", "CardType.Req");
        qtpayCardNo = new Param("cardNo");
        qtpayCardNo.setValue(mDebitCardNoEt.getText().toString().trim());
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardNo);
        httpsPost("getCardType", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    LogUtil.showLog("muxin", "result:" + payResult.getData());
                    JSONObject jsonObject = new JSONObject(payResult.getData());
//                    String codeStr=  jsonObject.getString("code");
//                    if(RyxAppconfig.QTNET_SUCCESS.equals(codeStr)){
//                    JSONObject result = jsonObject.getJSONObject("result").getJSONObject("cardinfo");
                    cardType = JsonUtil.getValueFromJSONObject(jsonObject,"cardtype");
                    bankName = JsonUtil.getValueFromJSONObject(jsonObject,"bankname").replace("\n", "");
                    //三位数（bankId字段取值hissuers字段）
                    bankId = JsonUtil.getValueFromJSONObject(jsonObject,"hissuers");
                    shortBankName = bankName;
//                    shortBankName = BanksUtils.selectshortname(bankId, bankName);
                    //新增字段
                    //发卡行总行代码
                    String hissuers = JsonUtil.getValueFromJSONObject(jsonObject,"hissuers");
                    //是否让客户选择支行信息 0不选1是选
                    String needbranch = JsonUtil.getValueFromJSONObject(jsonObject,"needbranch");
                    String branchid2 = JsonUtil.getValueFromJSONObject(jsonObject,"branchid2");

                    String kjzf_status =JsonUtil.getValueFromJSONObject(jsonObject,"kjzf_status");
                    String needvaliddate = JsonUtil.getValueFromJSONObject(jsonObject,"needvaliddate");
                    String needcvn = JsonUtil.getValueFromJSONObject(jsonObject,"needcvn");

                    Intent intent = null;
                    if (cardType.equals("01")) {//借记卡
                        cardTypeTxt = "储蓄卡";
                        intent = new Intent(BankCardAddActivity.this, DebitCardInfoAddActivity_.class);
                        intent.putExtra("hissuers", hissuers);
                        intent.putExtra("branchid2", branchid2);
                        intent.putExtra("needbranch", needbranch);
                        intent.putExtra("bankCardInfo", bankCardInfo);
                        intent.putExtra("bankid", bankId);
                        intent.putExtra("bankname", shortBankName);
                        intent.putExtra("cardtype", cardTypeTxt);
                        intent.putExtra("usertype", QtpayAppData.getInstance(BankCardAddActivity.this).getUserType());
                        startActivityForResult(intent, ADDCARDINFO);
                    } else if (cardType.equals("03")) {//信用卡
                        cardTypeTxt = "信用卡";
                        //跳转信用卡
                        intent = new Intent(BankCardAddActivity.this, MyCreditCardActivity_.class);
                        intent.putExtra("hissuers", hissuers);
                        intent.putExtra("branchid2", branchid2);
                        intent.putExtra("needbranch", needbranch);
                        intent.putExtra("bankCardInfo", bankCardInfo);
                        intent.putExtra("bankid", bankId);
                        intent.putExtra("bankname", shortBankName);
                        intent.putExtra("cardtype", cardTypeTxt);
                        intent.putExtra("usertype", QtpayAppData.getInstance(BankCardAddActivity.this).getUserType());
                        intent.putExtra("kjzf_status", kjzf_status);
                        intent.putExtra("needvaliddate", needvaliddate);
                        intent.putExtra("needcvn", needcvn);
                        startActivityForResult(intent, ADDCARDINFO);
                    } else {
                        cardTypeTxt = "未知";
                        LogUtil.showToast(BankCardAddActivity.this, "暂不支持绑定当前类型卡!");
                    }

//                    }else{
//                        String msgStr=  jsonObject.getString("msg");
//                        LogUtil.showToast(BankCardAddActivity.this,msgStr);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.showToast(BankCardAddActivity.this, "数据解析异常！");
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDCARDINFO) {
                BankCardAddActivity.this.finish();
            }
        }
        if (resultCode == RyxAppconfig.QT_RESULT_OK) {
            if (requestCode == SWIPING_CARD) {
                String signData = data.getExtras().getString("result");
                mDebitCardNoEt.setText(signData);
                mDebitCardNoEt.setSelection(signData.length());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
