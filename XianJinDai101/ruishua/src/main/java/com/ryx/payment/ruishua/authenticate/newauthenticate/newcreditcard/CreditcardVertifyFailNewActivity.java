package com.ryx.payment.ruishua.authenticate.newauthenticate.newcreditcard;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.CreditDialog;
import com.ryx.payment.ruishua.net.XmlCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;



@EActivity(R.layout.activity_creditcard_vertify_fail)
public class CreditcardVertifyFailNewActivity extends BaseActivity {

    @ViewById(R.id.tv_text)
    TextView tv_text;
    @ViewById(R.id.tv_reason)
    TextView tv_reason;
    @ViewById(R.id.tv_msg)
    TextView tv_msg;
    HashMap<String, String> bankMap = new HashMap<String, String>();

    private Param qtpayflag;

    @AfterViews
    public void initViews() {
        setTitleLayout("信用卡认证", true, false);
        initQtPatParams();
        if (getIntent() != null&&getIntent().hasExtra("BankCardInfo")) {
            bankMap = (HashMap<String, String>) getIntent().getSerializableExtra("BankCardInfo");
            initMsg();
        }
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayflag = new Param("id");

    }

    public void initMsg() {
        if ("2".equals(bankMap.get("status"))) {
            tv_text.setText(getResources().getString(R.string.senior_txt210));// 未通过审核
            tv_msg.setText(getResources().getString(R.string.senior_txt217));// 未通过审核

        } else {
            tv_text.setText(getResources().getString(R.string.senior_txt215));// 重新认证
            tv_msg.setText(getResources().getString(R.string.senior_txt211));
        }
        tv_reason.setText(bankMap.get("rejectReason"));
    }

    @Click(R.id.btn_reauth)
    public void reauth() {
        startActivityForResult(new Intent(CreditcardVertifyFailNewActivity.this,
                CreditAddCardNumberAct_.class).putExtra("BankcardId",
                bankMap.get("id")), RyxAppconfig.WILL_BE_CLOSED);
    }

    public void deleteCard() {
        qtpayApplication.setValue("DeleteCreditCard.Req");
        if (!TextUtils.isEmpty(bankMap.get("id")))
            qtpayflag.setValue(bankMap.get("id"));
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayflag);
        httpsPost("DeleteCreditCard", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                finish();
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

    @Click(R.id.btn_delete)
    public void deleteBtnClicked() {
        CreditDialog dialog = new CreditDialog(R.style.SimpleDialogLight) {
            @Override
            public void positiveBtnClick() {
                deleteCard();
            }

            @Override
            public void nagtiveBtnClick() {
                closeDialog();
            }
        };
        dialog.setView(getSupportFragmentManager(), "确认删除该信用卡吗?", "", "确认", "取消");
    }
}
