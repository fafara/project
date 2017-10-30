package com.ryx.payment.ruishua.bindcard;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.convenience.Cardno_;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_bind_debit_card)
public class BindDebitCardActivity extends BaseActivity {

    @ViewById(R.id.tv_userName)
    TextView tv_userName;
    @ViewById(R.id.edt_debitcardNo)
    EditText edt_debitcardNo;

    private int textwidth, textheight;
    private int SWIPING_CARD=50;
    private int ADDCARDINFO=1;
    String  cardid;
    String  realName;
    BankCardInfo  bankCardInfo;

    @AfterViews
    public void initViews(){
        tv_userName.setText(QtpayAppData.getInstance(BindDebitCardActivity.this).getRealName());

    }

    @Click(R.id.iv_swipecard)
    public void swipeCard(){
        startActivityForResult(new Intent(BindDebitCardActivity.this,Cardno_.class),SWIPING_CARD);
    }
    @Click(R.id.btn_next)
    public void goNext(){
        String cardNo = edt_debitcardNo.getText().toString().trim();
        if (!(cardNo.length()>=14)) {
            LogUtil.showToast(BindDebitCardActivity.this, getResources().getString(R.string.card_digit_error));
        }else {
            cardid = edt_debitcardNo.getText().toString().trim()+"";
            realName = QtpayAppData.getInstance(BindDebitCardActivity.this).getRealName();

            bankCardInfo = new BankCardInfo();
            bankCardInfo.setAccountNo(cardid);	// 卡号
            bankCardInfo.setName(realName);	// 姓名
            Intent intent = new Intent(BindDebitCardActivity.this,BindDebitCardInfoAddActivity_.class);
            intent.putExtra("bankCardInfo", bankCardInfo);
            intent.putExtra("usertype",QtpayAppData.getInstance(BindDebitCardActivity.this).getUserType());

            startActivityForResult(intent,ADDCARDINFO);
        }
    }
    @Click(R.id.btn_back)
    public void closeWindow(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDCARDINFO) {
                BindDebitCardActivity.this.finish();
            }
        }

        if (resultCode == RyxAppconfig.QT_RESULT_OK) {
            if (requestCode == SWIPING_CARD) {
                String signdata= data.getExtras().getString("result");
                edt_debitcardNo.setText(signdata);
                edt_debitcardNo.setSelection(signdata.length());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
