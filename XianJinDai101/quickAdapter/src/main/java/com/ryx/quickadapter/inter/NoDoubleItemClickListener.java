package com.ryx.quickadapter.inter;

import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

/**
 * 防止重复item点击监听
 *
 * @author muxin
 * @time 2016-10-10 17:17
 */
public abstract class NoDoubleItemClickListener implements AdapterView.OnItemClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleItemClick(adapterView, view, position, id);
        }
    }

    protected abstract void onNoDoubleItemClick(AdapterView<?> adapterView, View view, int position, long id);
}
