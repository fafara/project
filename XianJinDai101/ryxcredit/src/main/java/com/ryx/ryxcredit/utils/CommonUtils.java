package com.ryx.ryxcredit.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：lijing on 16/6/27 17:32
 * Mail：lijing1-jn@ruiyinxin.com
 * Description：
 */

public class CommonUtils {
   private static  char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /**
     * 人民币格式转换
     * @param param
     * @return
     */
    public static  String changeRenminbiFormat(int param){
        String result=param+"";
        StringBuffer sb=  new StringBuffer ();
        int i=0;
        for(int index=result.length ()-1;index>=0;index--){
            if (i!=0&&i%3==0){
                sb.append (",");
            }
            sb.append (result.substring (index,index+1));
            i++;
        }
        sb.reverse ();
        return  sb.toString ()+".00";
    }

    public static final String md5(String s) {
        if(TextUtils.isEmpty(s))
            return "";
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes("UTF-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }
            return new String(str);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static final String md52(byte[] b) {
        if(b==null)
            return "";
        try {
            byte[] strTemp = b;
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }
            return new String(str);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * params+phone+token+appUser，按照key对应 获取签名值
     * @param phone
     * @param params
     * @param token
     * @param appUser
     * @param transLogNo
     * @return
     */
    public static final String getSign(String phone,String params,String token,String appUser,long transLogNo)
    {
        if(TextUtils.isEmpty(params+phone+token+appUser))
            return "";
        StringBuffer buffer = new StringBuffer();
        String mdE=  params+phone+token+appUser;
        String sign=md5(mdE);
        return sign;
    }
    /**
     * params+phone+token+appUser，按照key对应
     * @param phone
     * @param params
     * @param token
     * @param appUser
     * @return
     */
    public static final String createFinalResult(String phone,String params,String token,String appUser,long transLogo){
        if(TextUtils.isEmpty(params+phone+token+appUser))
            return "";
        StringBuffer buffer = new StringBuffer();
        String mdE=  params+phone+token+appUser;
        String sign=md5(mdE);
        CLogUtil.showLog(mdE);
        Map result=   new HashMap<>();
        buffer.append("{");

        buffer.append("\"sign\":");
        buffer.append("\"");
        buffer.append(sign);
        buffer.append("\",");

        buffer.append("\"appUser\":");
        buffer.append("\"");
        buffer.append(appUser);
        buffer.append("\",");

        buffer.append("\"phone\":");
        buffer.append("\"");
        buffer.append(phone);
        buffer.append("\",");




        buffer.append("\"transTime\":");
        buffer.append("\"");
        buffer.append(CDateUtil.DateToStr(new Date()));
        buffer.append("\",");

        buffer.append("\"transLogNo\":");
        buffer.append("\"");
        buffer.append( String.format("%06d", transLogo) );
        buffer.append("\",");


        buffer.append("\"token\":");
        buffer.append("\"");
        buffer.append(token);
        buffer.append("\",");

        buffer.append("\"params\":");
        buffer.append("\""+params+"\"");

        buffer.append("}");
        return  buffer.toString();
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int scale2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        Log.d("testL","---"+scale+"   "+ context.getResources().getDisplayMetrics().densityDpi);

        return (int) (dpValue * scale + 0.5f);
    }

    public static int getWidth(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
      return width;
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static String byte2Str(byte[] bytes){
        if(bytes==null)
            return "";
        int j = bytes.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; ++i) {
            byte byte0 = bytes[i];
            str[k++] = hexDigits[byte0 >>> 4 & 15];
            str[k++] = hexDigits[byte0 & 15];
        }
        return new String(str);
    }
}
