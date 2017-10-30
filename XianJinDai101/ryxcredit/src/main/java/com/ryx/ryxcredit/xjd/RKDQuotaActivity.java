package com.ryx.ryxcredit.xjd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

public class RKDQuotaActivity extends BaseActivity implements View.OnClickListener {
    private AutoLinearLayout quota_work;
    private AutoLinearLayout quota_boss;
    private String xjd_user_level;
    private String xjd_user_info_level;
    private String trade_product_url;
    private String authorise_info_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rkdquota);
        setTitleLayout(getResources().getString(R.string.c_quota_activation), true, false);
        getIntentData();
        initView();
    }

    private void initView() {
        quota_work = (AutoLinearLayout) findViewById(R.id.rkd_quota_work);
        quota_work.setOnClickListener(this);
        quota_boss = (AutoLinearLayout) findViewById(R.id.rkd_quota_boss);
        quota_boss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.rkd_quota_work){
            Intent intent = new Intent(RKDQuotaActivity.this, BaseInfoSuccesActivity.class);
            intent.putExtra("user_level", xjd_user_level);
            intent.putExtra("user_info_level", xjd_user_info_level);
            intent.putExtra("product_id", RyxcreditConfig.xjd_procudtId);
            intent.putExtra("trade_product_url",trade_product_url);
            intent.putExtra("authorise_info_url",authorise_info_url);
            startActivityForResult(intent,100);
        }else if (id==R.id.rkd_quota_boss){
            Intent intent = new Intent(RKDQuotaActivity.this, RYDBossBaseInfoActivity.class);
            intent.putExtra("user_level", xjd_user_level);
            intent.putExtra("user_info_level", xjd_user_info_level);
            intent.putExtra("product_id", RyxcreditConfig.xjd_procudtId);
            intent.putExtra("trade_product_url",trade_product_url);
            intent.putExtra("authorise_info_url",authorise_info_url);
            startActivityForResult(intent,101);
        }
    }
    //可用额度，
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("user_level"))
                xjd_user_level = intent.getStringExtra("user_level");
            if (intent.hasExtra("user_info_level"))
                xjd_user_info_level = intent.getStringExtra("user_info_level");
            if (intent.hasExtra("trade_product_url"))
                trade_product_url = intent.getStringExtra("trade_product_url");
            if (intent.hasExtra("authorise_info_url"))
                authorise_info_url = intent.getStringExtra("authorise_info_url");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                /*Intent intent = new Intent(RKDQuotaActivity.this,CreditActivity.class);
                startActivity(intent);*/
                RKDQuotaActivity.this.finish();
                break;
            case 101:
                /*Intent intent = new Intent(RKDQuotaActivity.this,CreditActivity.class);
                startActivity(intent);*/
                RKDQuotaActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
