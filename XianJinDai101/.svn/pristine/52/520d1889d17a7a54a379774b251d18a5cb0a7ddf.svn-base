package com.ryx.ryxcredit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.ryd.activity.MiddleActivity;
import com.ryx.ryxcredit.xjd.XJDMiddleActivity;

/**
 * 使用说明
 *
 * @author muxin
 * @time 2016-09-22 10:29
 */
public class InstructionActivity extends BaseActivity implements View.OnClickListener {

//    private RelativeLayout mHowActivateRl;
//    private RelativeLayout mHowBorrowRl;
//    private RelativeLayout mHowRepayment;
    private RelativeLayout mRl_ryd_instruction_how_use;
    private RelativeLayout mRl_rkd_instruction_how_use;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_ryd_activity_instruction);
        setTitleLayout(getResources().getString(R.string.instruction), true, false);
        initView();
    }

    private void initView() {
    /*    mHowActivateRl = (RelativeLayout) findViewById(R.id.rl_instruction_how_activate);
        mHowActivateRl.setOnClickListener(this);
        mHowBorrowRl = (RelativeLayout) findViewById(R.id.rl_instruction_how_borrow);
        mHowBorrowRl.setOnClickListener(this);
        mHowRepayment = (RelativeLayout) findViewById(R.id.rl_instruction_how_repayment);
        mHowRepayment.setOnClickListener(this);*/
        mRl_ryd_instruction_how_use = (RelativeLayout) findViewById(R.id.rl_ryd_instruction_how_use);
        mRl_ryd_instruction_how_use.setOnClickListener(this);
        mRl_rkd_instruction_how_use = (RelativeLayout) findViewById(R.id.rl_rkd_instruction_how_use);
        mRl_rkd_instruction_how_use.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        try {
           /* if (id == R.id.rl_instruction_how_activate) {
                //如何激活额度
                intent.putExtra("ccurl",CConstants.HOW_ACTIVATE);
//                intent.putExtra("ccurl", "file:///android_asset/www/f1/jihuoedu.html");
                intent.putExtra("title", getResources().getString(R.string.tv_how_activate));
            } else if (id == R.id.rl_instruction_how_borrow) {
                //如何借款
                intent.putExtra("ccurl", CConstants.HOW_BORROW);
//                intent.putExtra("ccurl", "file:///android_asset/www/f1/jiekuan.html");
                intent.putExtra("title", getResources().getString(R.string.tv_how_borrow));
            } else if (id == R.id.rl_instruction_how_repayment) {
                //如何还款
                intent.putExtra("ccurl", CConstants.HOW_REPAYMENT);
//                intent.putExtra("ccurl", "file:///android_asset/www/f1/huankuan.html");
                intent.putExtra("title", getResources().getString(R.string.tv_how_repayment));
            }*/
            if (id == R.id.rl_ryd_instruction_how_use) {
                //瑞易贷使用说明
                Intent rydIntent = new Intent(InstructionActivity.this, MiddleActivity.class);
                startActivity(rydIntent);
            } else if (id == R.id.rl_rkd_instruction_how_use) {
                //瑞卡贷使用说明
                Intent rkdIntent = new Intent(InstructionActivity.this, XJDMiddleActivity.class);
                startActivity(rkdIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
