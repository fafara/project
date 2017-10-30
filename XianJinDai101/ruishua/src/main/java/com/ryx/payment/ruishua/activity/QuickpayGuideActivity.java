package com.ryx.payment.ruishua.activity;

import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_quickpay_guide)
public class QuickpayGuideActivity extends BaseActivity {
    @ViewById
    AutoLinearLayout quickpay_guide;
    int times=0;

    @AfterViews
    public void initView(){
        quickpay_guide.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                try {
                    ++times;
                    if(times==1){
                        quickpay_guide.setBackgroundResource(R.drawable.quickpay2);
                    }else if(times==2){
                        quickpay_guide.setBackgroundResource(R.drawable.quickpay3);
                    }else if(times==3){
                        quickpay_guide.setBackgroundResource(R.drawable.quickpay4);
                    }else  if(times==4){
                        quickpay_guide.setBackgroundResource(R.drawable.quickpay5);
                    }else{
                        finish();
                    }
                }catch (Exception e){
                    LogUtil.showLog(e.getLocalizedMessage());
                    finish();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceUtil.getInstance(QuickpayGuideActivity.this).saveInt("quickpayGuide",1);
    }
}
