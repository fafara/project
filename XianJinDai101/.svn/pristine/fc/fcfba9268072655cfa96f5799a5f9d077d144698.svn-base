package com.ryx.ryxcredit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.ryd.activity.RYDComProblemActivity;
import com.ryx.ryxcredit.utils.CConstants;
import com.ryx.ryxcredit.xjd.XJDComProblemActivity;

public class MiddleProblemActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mRl_ryd_problem;
    private RelativeLayout mRl_rkd_problem;
    private RelativeLayout mRl_Sharing_quota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle_problem);
        setTitleLayout(getResources().getString(R.string.problem), true, false);
        initView();
    }


    private void initView() {
        mRl_ryd_problem = (RelativeLayout) findViewById(R.id.rl_ryd_problem);
        mRl_ryd_problem.setOnClickListener(this);
        mRl_rkd_problem = (RelativeLayout) findViewById(R.id.rl_rkd_problem);
        mRl_rkd_problem.setOnClickListener(this);
        mRl_Sharing_quota = (RelativeLayout) findViewById(R.id.rl_Sharing_quota);
        mRl_Sharing_quota.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       int id = v.getId();
        if (id ==R.id.rl_ryd_problem){
            Intent intent = new Intent(MiddleProblemActivity.this,RYDComProblemActivity.class);
            startActivity(intent);

        }else if (id==R.id.rl_rkd_problem){
            Intent intent = new Intent(MiddleProblemActivity.this,XJDComProblemActivity.class);
            startActivity(intent);
        }else if (id==R.id.rl_Sharing_quota){
            Intent intent = null;
            try {
                intent = new Intent(MiddleProblemActivity.this, Class.forName(getApplicationContext().getPackageName()+".activity.HtmlMessageActivity_"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //关于瑞卡贷
                intent.putExtra("ccurl", CConstants.SHARKING_QUOTA);
                intent.putExtra("title", getResources().getString(R.string.tv_sharing_quota));
            startActivity(intent);
        }

    }

}
