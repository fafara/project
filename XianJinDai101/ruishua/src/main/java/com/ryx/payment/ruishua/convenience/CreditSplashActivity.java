package com.ryx.payment.ruishua.convenience;

import android.widget.ImageView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.utils.GlideUtils;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 小额贷临时闪屏图
 */
@EActivity(R.layout.activity_credit_splash)
public class CreditSplashActivity extends BaseActivity {
@ViewById
    ImageView iw_splach;
    @AfterViews
    public void initView(){
        setTitleLayout("CC贷",true,false);
        GlideUtils.getInstance().load(this, R.drawable.sp_credit, iw_splach);
    }
    @Click(R.id.iw_splach)
    public void IwSplachOnclick(){
        LogUtil.showToast(CreditSplashActivity.this,"你暂时无法使用贷款业务,额度陆续开放中");
    }
}
