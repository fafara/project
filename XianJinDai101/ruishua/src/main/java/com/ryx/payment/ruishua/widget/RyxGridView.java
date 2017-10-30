package com.ryx.payment.ruishua.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by xucc on 2016/3/30.
 */
public class RyxGridView extends GridView {
    public RyxGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RyxGridView(Context context) {
        super(context);
    }

    public RyxGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
