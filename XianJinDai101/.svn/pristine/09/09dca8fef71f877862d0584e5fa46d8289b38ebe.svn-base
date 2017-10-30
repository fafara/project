package com.ryx.payment.ruishua.authenticate.creditcard;

import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
/**
 * Created by xiepingping on 2016/5/27.
 */
@EActivity(R.layout.activity_credit_senior_succed)
public class CreditCardAuthSuccedActivity extends BaseActivity {

    @ViewById(R.id.tv_msg)
    TextView tv_msg;

   private String  succedwords=null;
    @AfterViews
    public void initView(){
        setTitleLayout("信用卡认证",true,false);
        if (null != getIntent()) {
            succedwords = getIntent().getStringExtra("convenicepaysucced");
        }

        if (!(null==succedwords||succedwords.isEmpty())) {
            tv_msg.setText(succedwords);
        }
    }

    @Click(R.id.btn_know)
    public void closeWindow(){
        setResult(RyxAppconfig.CLOSE_ALL);
        finish();
    }
    @Override
    public void backUpImgOnclickListen(){
        setResult(RyxAppconfig.CLOSE_ALL);
        finish();
    }
}
