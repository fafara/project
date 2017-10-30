package com.ryx.payment.ruishua.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

public class UnitTransformUtil { 
    /** * 根据手机的分辨率从dp 的单位 转成为px(像素) */ 
    public static int dip2px(Context context, float dpValue) { 
            final float scale = context.getResources().getDisplayMetrics().density; 
            return (int) (dpValue * scale + 0.5f); 
    } 

    /** * 根据手机的分辨率从px(像素) 的单位 转成为dp */ 
    public static int px2dip(Context context, float pxValue) { 
            final float scale = context.getResources().getDisplayMetrics().density; 
            return (int) (pxValue / scale + 0.5f); 
    } 
    
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  
    
    public static int getDmDensityDpi(Context context) {  
    	DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();  
        // 设置DensityDpi  
    	return dm.densityDpi;
    }
    
    
    
    public static  String FormatFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) +"G";
        }
        return fileSizeString;
     }
}   
