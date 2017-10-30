package com.ryx.ryxcredit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.drawable.CPromoteRectangleDrawable;

/**
 * Created by DIY on 2016/6/28.
 */
public class PromoteQuotaActivity extends BaseActivity {
    Handler handler=new Handler();
    //基本信息
    private TextView baseInfoTv;
    //公积金
    private TextView gongjijinTv;
    //社保
    private TextView  shebaoTv;
    //纳税
    private TextView taxesTv;
    //央行信证
    private  TextView yanghangxinzhengTv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_promote_quota);
        baseInfoTv= (TextView) findViewById(R.id.c_promote_quota_base_info_tv);
        gongjijinTv= (TextView) findViewById(R.id.c_promote_quota_gongjijin_tv);
        shebaoTv= (TextView) findViewById(R.id.c_promote_quota_shebao_tv);
        taxesTv= (TextView) findViewById(R.id.c_promote_quota_nashui_tv);
        yanghangxinzhengTv= (TextView) findViewById(R.id.c_promote_quota_yanghangxinzheng_tv);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int measuredHeight = baseInfoTv.getMeasuredHeight();
                CPromoteRectangleDrawable rectangleDrawable1 = new CPromoteRectangleDrawable(measuredHeight, getResources().getColor(R.color.red));
                CPromoteRectangleDrawable rectangleDrawable2 = new CPromoteRectangleDrawable(measuredHeight, getResources().getColor(R.color.red_second));
                CPromoteRectangleDrawable rectangleDrawable3 = new CPromoteRectangleDrawable(measuredHeight, getResources().getColor(R.color.orange));
                CPromoteRectangleDrawable rectangleDrawable4 = new CPromoteRectangleDrawable(measuredHeight, getResources().getColor(R.color.blue_second));
                CPromoteRectangleDrawable rectangleDrawable5 = new CPromoteRectangleDrawable(measuredHeight, getResources().getColor(R.color.red_second));


                baseInfoTv.setBackgroundDrawable(rectangleDrawable1);
                gongjijinTv.setBackgroundDrawable(rectangleDrawable2);
                shebaoTv.setBackgroundDrawable(rectangleDrawable3);
                taxesTv.setBackgroundDrawable(rectangleDrawable4);
                yanghangxinzhengTv.setBackgroundDrawable(rectangleDrawable5);
            }
        },100);
       setTitleLayout(getResources().getString(R.string.c_promote_quota),true,false);
    }

    /**
     * 基本信息
     * @param view
     */
    public void baseInfoAction(View view){
      Intent intent=  new Intent(this,BaseInfoActivity.class);
       startActivityForResult(intent,01);
    }
}
