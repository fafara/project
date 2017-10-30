package com.ryx.ryxcredit.ryd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

public class QuotaActivity extends BaseActivity implements View.OnClickListener {
    private AutoLinearLayout quota_work;
    private AutoLinearLayout quota_boss;
    private String rkd_user_level;
    private String rkd_user_info_level;
    private String service_authorize_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quota);
        setTitleLayout(getResources().getString(R.string.c_quota_activation), true, false);
        getIntentData();
        initView();
    }

    private void initView() {
        quota_work = (AutoLinearLayout) findViewById(R.id.quota_work);
        quota_work.setOnClickListener(this);
        quota_boss = (AutoLinearLayout) findViewById(R.id.quota_boss);
        quota_boss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.quota_work){
            Intent intent = new Intent(QuotaActivity.this, RYDBaseInfoSuccesActivity.class);
            intent.putExtra("user_level", rkd_user_level);
            intent.putExtra("user_info_level", rkd_user_info_level);
            intent.putExtra("product_id", RyxcreditConfig.rkd_procudtid);
            intent.putExtra("service_authorize_url",service_authorize_url);
            startActivity(intent);
        }else if (id==R.id.quota_boss){
            Intent intent = new Intent(QuotaActivity.this, RYDBaseInfoSuccesActivity.class);
            intent.putExtra("user_level", rkd_user_level);
            intent.putExtra("user_info_level", rkd_user_info_level);
            intent.putExtra("product_id", RyxcreditConfig.rkd_procudtid);
            intent.putExtra("service_authorize_url",service_authorize_url);
            startActivity(intent);
        }
    }
    //可用额度，
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("rkd_user_level"))
                rkd_user_level = intent.getStringExtra("rkd_user_level");
            if (intent.hasExtra("rkd_user_info_level"))
                rkd_user_info_level = intent.getStringExtra("rkd_user_info_level");
            if (intent.hasExtra("service_authorize_url"))
                service_authorize_url = intent.getStringExtra("service_authorize_url");
        }
    }
}
