package com.ryx.ryxcredit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.utils.CConstants;


/**
 * 常见问题
 *
 * @author muxin
 * @time 2016-09-21 18:31
 */
public class ComProblemActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout mAboutRqdRl;
    private RelativeLayout mEduRl;
    private RelativeLayout mBorrowRl;
    private RelativeLayout mRepaymtnRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_com_problem_activity);
        setTitleLayout("常见问题", true, false);
        initView();
    }

    private void initView() {
        mAboutRqdRl = (RelativeLayout) findViewById(R.id.rl_problem_about_rqd);
        mEduRl = (RelativeLayout) findViewById(R.id.rl_problem_edu);
        mBorrowRl = (RelativeLayout) findViewById(R.id.rl_problem_borrow);
        mRepaymtnRl = (RelativeLayout) findViewById(R.id.rl_problem_repayment);
        mAboutRqdRl.setOnClickListener(this);
        mEduRl.setOnClickListener(this);
        mBorrowRl.setOnClickListener(this);
        mRepaymtnRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        try {
            Intent intent = new Intent(ComProblemActivity.this, Class.forName(getApplicationContext().getPackageName()+".activity.HtmlMessageActivity_"));
            if (id == R.id.rl_problem_about_rqd) {
                //关于瑞卡贷
                intent.putExtra("ccurl", CConstants.ABOUT_PRODUCT);
//                intent.putExtra("ccurl", "file:///android_asset/www/f2/about.html");
                intent.putExtra("title", getResources().getString(R.string.tv_about_rqd));
            } else if (id == R.id.rl_problem_borrow) {
                //借款问题
                intent.putExtra("ccurl", CConstants.CREDIT_PRO);
//                intent.putExtra("ccurl", "file:///android_asset/www/f2/jiekuan.html");
                intent.putExtra("title", getResources().getString(R.string.tv_borrow_problem));
            } else if (id == R.id.rl_problem_edu) {
                //额度问题
                intent.putExtra("ccurl", CConstants.EDU_PRO);
                intent.putExtra("title", getResources().getString(R.string.tv_edu_problem));
//                intent.putExtra("ccurl", "file:///android_asset/www/f2/edu.html");
            } else if (id == R.id.rl_problem_repayment) {
                //还款问题
                intent.putExtra("ccurl", CConstants.REPAY_PRO);
                intent.putExtra("title", getResources().getString(R.string.tv_repayment_problem));
//                intent.putExtra("ccurl", "file:///android_asset/www/f2/huankuan.html");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
