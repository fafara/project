package com.ryx.ryxcredit.utils;


import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Creaed by Administrator on 2016/11/24.
 */

public class CNummberUtil {

    public static double parseDouble(String doubleStr,double defaultNum) {
        if(TextUtils.isEmpty(doubleStr))
         return defaultNum;
        double num = defaultNum;
        try{
            num = Double.parseDouble(doubleStr);
        }catch(NumberFormatException e){
            e.printStackTrace();

        }
        return num;
    }
    public static int parseInt(String intStr,int defaultNum) {
        if(TextUtils.isEmpty(intStr))
            return defaultNum;
        int num = defaultNum;
        try{
            num = Integer.parseInt(intStr);
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        return num;
    }
    public static int parseIntRadix(String intStr,int radix,int defaultNum) {
        if(TextUtils.isEmpty(intStr))
            return defaultNum;
        int num = defaultNum;
        try{
            num = Integer.parseInt(intStr,radix);
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        return num;
    }

    public static float parseFloat(String floatStr,float defaultNum) {
        if(TextUtils.isEmpty(floatStr))
            return defaultNum;
        float num = defaultNum;
        try{
            num = Float.parseFloat(floatStr);
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        return num;
    }

    public static BigDecimal getStrFromBigDecimal(String bigDecimalStr){
        BigDecimal bd;
        if (bigDecimalStr == null && "".equalsIgnoreCase(bigDecimalStr))
            return null;
            try {
                bd = new BigDecimal(bigDecimalStr);
                //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
                bd = bd.setScale(9, BigDecimal.ROUND_HALF_UP);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return null;
            }
            return bd;
    }
    //string类型转换为小数点两位
    public static String getStrFromDouble(String bigDecimalStr ){
        String Str;
        if(bigDecimalStr!=null&&!"".equalsIgnoreCase(bigDecimalStr))
            return null;
        try {
        if (bigDecimalStr != null && !"".equalsIgnoreCase(bigDecimalStr)) {
            String ss = new String(bigDecimalStr);
            double i = Double.parseDouble(ss);
            i = i * 100;
            DecimalFormat df = new DecimalFormat("#0.00");
             Str = String.valueOf( df.format(i));

        }else{
            return "0";
        }
        }catch (Exception e){
            return null;
        }
        return Str;
    }

}
