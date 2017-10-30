package com.ryx.payment.ruishua.recharge;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.convenience.CreateOrder_;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_repayment)
public class RepaymentActivity extends BaseActivity {
    private BankCardInfo bankCardInfo;
    private OrderInfo orderinfo;
    private String titleStr;
    @ViewById
    ImageView repayment_bankimgview;
    @ViewById
    TextView repayment_banknametextview, tv_bankCardnumber, username;
    @ViewById
    EditText repayment_money;

    @AfterViews
    public void afterView() {
        orderinfo = (OrderInfo) getIntent().getExtras().getSerializable("orderinfo");
        bankCardInfo = (BankCardInfo) getIntent().getExtras().get("bankCardInfo");
        BanksUtils.selectIcoidToImgView(RepaymentActivity.this,bankCardInfo.getBankId(), repayment_bankimgview);
        //因后台暂不做改动,此处暂时改为本地匹配
//        repayment_banknametextview.setText(bankCardInfo.getBankName());
        repayment_banknametextview.setText(BanksUtils.selectshortname(bankCardInfo.getBankId(), bankCardInfo.getBankName()));
        tv_bankCardnumber.setText(StringUnit.cardJiaMi(bankCardInfo.getAccountNo()));
        LogUtil.showLog("afterView",bankCardInfo.getName()+"----");
        username.setText(StringUnit.realNameJiaMi(StringUnit.lengthLimit(5, bankCardInfo.getName())));
        switch (orderinfo.getId()) {
            case RyxAppconfig.CREDITCARD_REPAYMENT:
                setTitleLayout("信用卡还款", true);
                repayment_money.setHint("请输入还款金额");
                setRightImgMessage("信用卡还款", RyxAppconfig.Notes_Credit);
                break;
            case RyxAppconfig.TRANSFER_REPAYMENT:
                setTitleLayout("银行卡转账",true);
                repayment_money.setHint("请输入转账金额");
                setRightImgMessage("银行卡转账", RyxAppconfig.Notes_Transfer);
                break;
        }
    }

    @Click(R.id.bt_next)
    public void btnNext() {
        if (repayment_money.getText().toString().length() == 0) {
            LogUtil.showToast(RepaymentActivity.this, "金额不能为空！");
            return;
        }
        repayment_money.setText(MoneyEncoder.EncodeFormat(repayment_money.getText().toString()));

        if (repayment_money.getText().toString().equals("￥0.00")) {
            LogUtil.showToast(RepaymentActivity.this, "金额不能为零！");
            return;
        }

        if (repayment_money.getText().toString().replace(",", "").replace("￥", "").length() - repayment_money.getText().toString().replace(",", "").replace("￥", "").indexOf(".") > 3) {
            LogUtil.showToast(RepaymentActivity.this, "金额单位过小！");
            return;
        }

        if (repayment_money.getText().toString().replace(",", "").replace("￥", "").length() > 9) {
            LogUtil.showToast(RepaymentActivity.this, "金额超限！");
            return;
        }

        Intent bintent = new Intent(RepaymentActivity.this, CreateOrder_.class);
        orderinfo.setOrderAmt(repayment_money.getText().toString());

        orderinfo.setOrderDesc(bankCardInfo.getAccountNo());
        orderinfo.setOrderRemark(bankCardInfo.getName() + ",,,," + bankCardInfo.getRepaydate() + "|");
        bintent.putExtra("orderinfo", orderinfo);
        bintent.putExtra("bankCardInfo", bankCardInfo);
        startActivityForResult(bintent, RyxAppconfig.WILL_BE_CLOSED);
    }

}
