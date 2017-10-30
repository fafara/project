package com.ryx.ryxcredit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;


/**
 * 瑞商贷协议
 *
 * @author muxin
 * @time 2016-10-19 14:21
 */
public class AgreementActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mDaiKouRl;
    private RelativeLayout mServiceRl;
    private RelativeLayout mBorrowRl;
    private TextView tv_brcontractName;
    private String daikou_url;
    private String borrow_url;
    private String money_manage_url;
    private String productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_agreement_activity);
        setTitleLayout("协议", true, false);
        borrow_url = getIntent().getStringExtra("contract_url");//借款协议
        daikou_url = getIntent().getStringExtra("daikou_url");//委托代扣协议
        productId = getIntent().getStringExtra("productId");//产品编码
        money_manage_url = getIntent().getStringExtra("money_manage_url");//信用咨询及管理服务协议
        initView();
        if(!TextUtils.isEmpty(daikou_url)){
            mDaiKouRl.setVisibility(View.VISIBLE);
        }else{
            mDaiKouRl.setVisibility(View.GONE);
        }

    }

    private void initView() {
        mDaiKouRl = (RelativeLayout) findViewById(R.id.rl_agreement_daikou);
        mServiceRl = (RelativeLayout) findViewById(R.id.rl_agreement_service);
        mBorrowRl = (RelativeLayout) findViewById(R.id.rl_agreement_borrow);
        tv_brcontractName =  (TextView)findViewById(R.id.tv_brcontractName);
        if(RyxcreditConfig.rkd_procudtid.equals(productId)){
            tv_brcontractName.setText("瑞易贷借款协议");
        }else{
            tv_brcontractName.setText("瑞卡贷借款协议");
        }
        mDaiKouRl.setOnClickListener(this);
        mServiceRl.setOnClickListener(this);
        mBorrowRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;
        try {
            intent = new Intent(AgreementActivity.this, Class.forName(getApplicationContext().getPackageName() + ".activity.HtmlMessageActivity_"));
            if (id == R.id.rl_agreement_daikou) {
                intent.putExtra("ccurl", daikou_url);
                intent.putExtra("title", "委托扣款服务协议");
            } else if (id == R.id.rl_agreement_service) {
                intent.putExtra("ccurl", money_manage_url);
                intent.putExtra("title", "信用咨询及管理服务协议");
            } else if (id == R.id.rl_agreement_borrow) {
                intent.putExtra("ccurl", borrow_url);
                if(RyxcreditConfig.rkd_procudtid.equals(productId)){
                    intent.putExtra("title", "瑞易贷借款协议");
                }else{
                    intent.putExtra("title", "瑞卡贷借款协议");
                }

            }
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
