package com.ryx.payment.ruishua.authenticate.newauthenticate.newcreditcard;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
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


@EActivity(R.layout.activity_creditcardnumber_add)
public class CreditAddCardNumberAct extends BaseActivity  implements DataCallBack {
    @ViewById
    TextView tv_userName;
    @ViewById
    EditText edt_debitcardNo;
    @ViewById
    ImageView iv_indentify_card;

    Param qtpayflag;
    Param qtpayAccountNo;
    private String resultSwipeStr = "";
    private String expirydate;
    private String bankcardid = "";

    private String cardId;
    private String realName;
    private BankCardInfo bankCardInfo;
    private Param qtpayCardNo;
    private String bankId;
    private String cardType;
    private String bankName;
    private String shortBankName;
    private String cardTypeTxt;
    private static final int ADDCARDINFO = 0x999;
    private static final int CREDITFINISHED=0x1000;


    @AfterViews
    public void initViews() {
        setTitleLayout("添加信用卡", true, true);
        tv_userName.setText(QtpayAppData.getInstance(this).getRealName());
        bankcardid = getIntent().getStringExtra("BankcardId");
        initQtPatParams();
    }

    @Click(R.id.tilerightImg)
    public void showHelp(){
        Intent intent = new Intent(CreditAddCardNumberAct.this,HtmlMessageActivity_.class);
        intent.putExtra("title","信用卡认证及升级规则");
        intent.putExtra("urlkey",RyxAppconfig.Notes_Senior);
        startActivity(intent);
    }

    @Click(R.id.btn_next)
    public void goNext() {
        String cardNo = edt_debitcardNo.getText().toString().trim();
        if (!(cardNo.length() >= 14)) {
            LogUtil.showToast(this, getResources().getString(R.string.card_digit_error));
        } else {
            cardId = edt_debitcardNo.getText().toString().trim() + "";
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
        final String cardNo = edt_debitcardNo.getText().toString().trim();
        qtpayCardNo.setValue(cardNo);
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardNo);
        httpsPost("getCardType", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    LogUtil.showLog("muxin", "result:" + payResult.getData());
                    JSONObject jsonObject = new JSONObject(payResult.getData());
                    cardType = jsonObject.getString("cardtype");
                    bankName = jsonObject.getString("bankname").replace("\n", "");
                    //三位数（bankId字段取值hissuers字段）
                    bankId = jsonObject.getString("hissuers");
                    shortBankName = bankName;
                    //新增字段
                    //发卡行总行代码
                    String hissuers = jsonObject.getString("hissuers");
                    //是否让客户选择支行信息 0不选1是选
                    String needbranch = jsonObject.getString("needbranch");
                    String branchid2 = jsonObject.getString("branchid2");
                    Intent intent = null;
                    if (cardType.equals("01")) {//借记卡
                        cardTypeTxt = "储蓄卡";
                        LogUtil.showToast(CreditAddCardNumberAct.this, "请绑定信用卡!");
                    } else if (cardType.equals("03")) {//信用卡
                        cardTypeTxt = "信用卡";
                        //跳转信用卡
                        intent = new Intent(CreditAddCardNumberAct.this, CreditAddPhoneMsgAct_.class);
                        intent.putExtra("hissuers", hissuers);
                        intent.putExtra("branchid2", branchid2);
                        intent.putExtra("needbranch", needbranch);
                        intent.putExtra("bankCardInfo", bankCardInfo);
                        intent.putExtra("bankid", bankId);
                        intent.putExtra("bankname", shortBankName);
                        intent.putExtra("cardtype", cardTypeTxt);
                        intent.putExtra("cardNo", cardNo);
                        intent.putExtra("bankcardId",bankcardid);
                        intent.putExtra("usertype", QtpayAppData.getInstance(CreditAddCardNumberAct.this).getUserType());
                        startActivityForResult(intent,ADDCARDINFO);
                    } else {
                        cardTypeTxt = "未知";
                        LogUtil.showToast(CreditAddCardNumberAct.this, "暂不支持绑定当前类型卡!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.showToast(CreditAddCardNumberAct.this, "数据解析异常！");
                }
            }
        });
    }

    @Click(R.id.iv_indentify_card)
    public void identyCard(View view){
        disabledTimerView(view);
        String warning = "请在\"设置\"里，打开\"" + RyxAppdata.getInstance(this).getCurrentBranchName() + "\"的拍照权限，否则您将无法拍照";
        requesDevicePermission(warning, requestCode, photo_permissionResult, Manifest.permission.CAMERA);
    }

    private PermissionResult photo_permissionResult = new PermissionResult() {
        @Override
        public void requestSuccess() {
            BankManager.getInstance().showLogo(false);
            BankManager.getInstance().setShowPhoto(false);
            BankManager.getInstance().recognize(CreditAddCardNumberAct.this, CreditAddCardNumberAct.this);
        }

        @Override
        public void requestFailed() {

        }
    };

    //识别到了银行卡片信息
    @Override
    public void onBankCardDetected(boolean success) {
        BankManager.getInstance().stopRecognize();
        if (success) {
            EXBankCardInfo cardInfo = BankManager.getInstance().getCardInfo();
            LogUtil.showLog("cardInfo", cardInfo.strValid + "----");
            if (cardInfo != null) {
                edt_debitcardNo.setText(cardInfo.strNumbers);
            }
        }
    }

    @Override
    public void onCameraDenied() {

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==ADDCARDINFO&&resultCode==CREDITFINISHED){
            finish();
        }else if(resultCode== RyxAppconfig.CLOSE_ALL){
            setResult(RyxAppconfig.CLOSE_ALL);
            finish();
        }
    }
}
