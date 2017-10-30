package com.ryx.ryxcredit.newactivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.views.OnDotsChangeListenter;

/**
 * Created by RYX on 2017/5/20.
 */

public class LeadingDotsSuccessViews extends LinearLayout implements OnDotsChangeListenter {

    private TextView tv_1,tv_2,tv_3,tv_4,tv_5;
    private int dotNum = 5;

    public LeadingDotsSuccessViews(Context context) {
        super (context);
        initView (context);
    }

    public LeadingDotsSuccessViews(Context context, AttributeSet attrs) {
        super (context, attrs);
        initView (context);
    }

    public LeadingDotsSuccessViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        initView (context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LeadingDotsSuccessViews(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super (context, attrs, defStyleAttr, defStyleRes);
        initView (context);
    }

    void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
       View view = inflater.inflate (R.layout.c_new_view_dots_jhsuccess, this);
        tv_1 = (TextView)view.findViewById(R.id.tv_1);
        tv_2 = (TextView)view.findViewById(R.id.tv_2);
        tv_3 = (TextView)view.findViewById(R.id.tv_3);
        tv_4 = (TextView)view.findViewById(R.id.tv_4);
        tv_5 = (TextView)view.findViewById(R.id.tv_5);
    }

    public void setDotsNum(int pos){
        dotNum = pos;
        if(pos==3){
            tv_4.setVisibility(View.GONE);
            tv_5.setVisibility(View.GONE);
        }else if(pos==4){
            tv_5.setVisibility(View.GONE);
        } else if (pos==5) {
            tv_5.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void change(int postion) {
        LinearLayout content = ((LinearLayout) getChildAt (0));
        if (postion > content.getChildCount () - 1) {
            throw new RuntimeException ("传递参数值大于最大点数");
        }
        int num = dotNum>content.getChildCount()?content.getChildCount():dotNum;
        for (int i = 0; i < num; i++) {
            TextView tv = (TextView) content.getChildAt (i);
            tv.setEnabled (false);
            if (postion == i) {
                tv.setEnabled (true);
            }

        }
    }
}
