package com.ryx.payment.ruishua.authenticate.creditcard;

import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.MessageFormat;
/**
 * Created by xiepingping on 2016/5/27.
 */
@EActivity(R.layout.activity_credit_card_check_fail)
public class CreditCardCheckFailActivity extends BaseActivity {

    @ViewById(R.id.tv_msg)
    TextView tv_msg;
    private String failmsg;

    @AfterViews
    public void initViews(){
        setTitleLayout(getResources().getString(R.string.senior_credit_card),true);
        if(getIntent()!=null)
            failmsg= getIntent().getStringExtra("RespDesc");
        if (!"".equals(failmsg)) {
            tv_msg.setText(MessageFormat.format(
                    getResources().getString(R.string.senior_txt), failmsg));
        }
    }
    @Click(R.id.btn_know)
    public void closeWindow(){
        finish();
    }
}
