package com.ryx.ryxcredit.xjd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.utils.CConstants;
//瑞卡贷使用说明
public class XJDMiddleActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mHowActivateRl;
   private RelativeLayout mHowBorrowRl;
    private RelativeLayout mHowRepayment;

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle_rkd);
        setTitleLayout(getResources().getString(R.string.rkd_instruction), true, false);
        initView();
        }

private void initView() {
        mHowActivateRl = (RelativeLayout) findViewById(R.id.rl_instruction_how_activate);
        mHowActivateRl.setOnClickListener(this);
        mHowBorrowRl = (RelativeLayout) findViewById(R.id.rl_instruction_how_borrow);
        mHowBorrowRl.setOnClickListener(this);
        mHowRepayment = (RelativeLayout) findViewById(R.id.rl_instruction_how_repayment);
        mHowRepayment.setOnClickListener(this);
        }

@Override
public void onClick(View v) {
        int id = v.getId();
        try {
        Intent intent = new Intent(XJDMiddleActivity.this, Class.forName(getApplicationContext().getPackageName()+".activity.HtmlMessageActivity_"));
        if (id == R.id.rl_instruction_how_activate) {
        //如何激活额度
        intent.putExtra("ccurl", CConstants.RKD_HOW_ACTIVATE);
//                intent.putExtra("ccurl", "file:///android_asset/www/f1/jihuoedu.html");
        intent.putExtra("title", getResources().getString(R.string.tv_middle_rkd_how_activate));
        } else if (id == R.id.rl_instruction_how_borrow) {
        //如何借款
        intent.putExtra("ccurl", CConstants.RKD_HOW_BORROW);
//                intent.putExtra("ccurl", "file:///android_asset/www/f1/jiekuan.html");
        intent.putExtra("title", getResources().getString(R.string.tv_middle_rkd_how_borrow));
        } else if (id == R.id.rl_instruction_how_repayment) {
        //如何还款
        intent.putExtra("ccurl", CConstants.RKD_HOW_REPAYMENT);
//                intent.putExtra("ccurl", "file:///android_asset/www/f1/huankuan.html");
        intent.putExtra("title", getResources().getString(R.string.tv_middle_rkd_how_repayment));
        }
        startActivity(intent);
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
}
