package com.ryx.ryxcredit.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryx.ryxcredit.R;

/**
 * Author：lijing on 16/6/24 17:10
 * Mail：lijing1-jn@ruiyinxin.com
 * Description：
 */

public class LeadingDotsViews extends LinearLayout implements OnDotsChangeListenter {


    public LeadingDotsViews(Context context) {
        super (context);
        initView (context);
    }

    public LeadingDotsViews(Context context, AttributeSet attrs) {
        super (context, attrs);
        initView (context);
    }

    public LeadingDotsViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        initView (context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LeadingDotsViews(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super (context, attrs, defStyleAttr, defStyleRes);
        initView (context);
    }

    void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService

                (Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate (R.layout.c_view_dots, this);
    }

    @Override
    public void change(int postion) {
        LinearLayout content = ((LinearLayout) getChildAt (0));
        if (postion > content.getChildCount () - 1) {
            throw new RuntimeException ("传递参数值大于最大点数");
        }
        for (int i = 0; i < content.getChildCount (); i++) {
            TextView tv = (TextView) content.getChildAt (i);
            tv.setEnabled (false);
            if (postion == i) {
                tv.setEnabled (true);
            }

        }
    }
}
