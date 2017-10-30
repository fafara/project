package com.ryx.ryxpay.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by laomao on 16/5/9.
 */
public class AdapterUtil {
    public static int getSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();

        float density = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;


        int screenWidth = dm.widthPixels;      // 屏幕宽（像素，如：480px）
        int gridWidth = screenWidth;
        return  gridWidth / 4;

    }
}
