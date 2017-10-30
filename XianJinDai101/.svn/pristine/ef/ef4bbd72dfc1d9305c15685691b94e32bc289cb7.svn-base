package com.ryx.ryxcredit.utils;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
/**
 * Created by laomao on 15/12/2.
 */

public class CMoneyEncoder {

    public static boolean IsEncoded(String str)
    {
        if(TextUtils.isEmpty(str))
            return false;
        int nCount = 0,nStart = 0;
        String s1 = "";
        char s2;

        if(str.indexOf("¥") != 0)
        {
            return false;
        }
        else
        {
            nStart = str.indexOf(".");
            if(nStart != -1)
            {
                s1 = str.substring(1,nStart);
            }
            else
            {
                s1 = str.substring(1,str.length());
            }

            //check from one bit to anther
            for(nCount = 0;nCount<(s1.length()-s1.length()%3);nCount++)
            {
                s2 = s1.charAt(nCount);
                if(nCount == 0)
                {
                    if(!(s2>='0'&&s2<='9'))
                    {
                        return false;
                    }
                }
                else
                {
                    if(nCount%3 != 0)
                    {
                        if(!(s2>='0'&&s2<='9'))
                        {
                            return false;
                        }
                    }
                    else
                    {
                        if(s2 != ',')
                        {
                            return false;
                        }
                    }
                }
            }

            //last number
            while(nCount < s1.length())
            {
                s2 = s1.charAt(nCount);
                if(!(s2>='0'&&s2<='9'))
                {
                    return false;
                }
                nCount++;
            }
        }

        return true;
    }

    public static boolean isCN(String str){
        if(TextUtils.isEmpty(str))
            return false;
        try {
            byte [] bytes = str.getBytes("UTF-8");
            if(bytes.length == str.length()){
                return false;
            }else{
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static String decodeFormat(String str)
    {
        if(TextUtils.isEmpty(str))
            return "";
        String s1 = str;
        String s2 = "";

        if(str == null || "".equals(str))
        {
            s2 = "0.00";
        }
        else
        {
            if(str.length() == 0)
            {
                s2 = "0.00";
            }
            else if(str.length() == 1)
            {
                s2 = "0.0";
                s2 += str;
            }
            else if(str.length() == 2)
            {
                s2 = "0.";
                s2 += str;
            }
            else
            {
                s2 = s1.substring(0,s1.length()-2);
                s2 += ".";
                s2 += s1.substring(s1.length()-2,s1.length());
            }
        }
        return EncodeFormat(s2);
    }

    public static String encodeToPost(String str)
    {
        if(TextUtils.isEmpty(str))
            return "";
        int nStart = 0,nLength =0;
        String s1 = str;
        String s2 = "";

        //delete ¥
        nStart = s1.indexOf("¥");
        while(nStart != -1)
        {
            s2 = s1.substring(0,nStart);
            s2 += s1.substring(nStart+1,s1.length());
            s1 = s2;
            nStart = s1.indexOf("¥");
        }

        //delete ,
        nStart = s1.indexOf(",");
        while(nStart != -1)
        {
            s2 = s1.substring(0,nStart);
            s2 += s1.substring(nStart+1,s1.length());
            s1 = s2;
            nStart = s1.indexOf(",");
        }

        //delete .
        nStart = s1.indexOf(".");
        while(nStart != -1)
        {
            s2 = s1.substring(0,nStart);
            s2 += s1.substring(nStart+1,s1.length());
            s1 = s2;
            nStart = s1.indexOf(",");
        }

        nLength = s1.length();
        s2 = "";
        while(nLength < 12)
        {
            s2 += "0";
            nLength ++;
        }
        s2 += s1;
        return s2;
    }

    public static String encodeForSwiper(String str){
        if(TextUtils.isEmpty(str))
            return "";
        int nCount = 0;
        String s1 = encodeToPost(str);
        String s2 = "";

        nCount = s1.length();
        while(nCount > 3){
            s2 = s1.substring(0, 1);
            if("0".equals(s2)){
                s1 = s1.substring(1, s1.length());
                nCount --;
            }
            else{
                break;
            }
        }
        return s1;
    }

    public static String CleanFormat(String str)
    {
        if(TextUtils.isEmpty(str))
            return "";
        int nCount = 0,nStart = 0;
        String s1 = str;
        String s2 = "";

        //delete ¥
        nStart = s1.indexOf("¥");
        while(nStart != -1)
        {
            s2 = s1.substring(0,nStart);
            s2 += s1.substring(nStart+1,s1.length());
            s1 = s2;
            nStart = s1.indexOf("¥");
        }

        //delete ,
        nStart = s1.indexOf(",");
        while(nStart != -1)
        {
            s2 = s1.substring(0,nStart);
            s2 += s1.substring(nStart+1,s1.length());
            s1 = s2;
            nStart = s1.indexOf(",");
        }

        //delete 0
        nStart = s1.indexOf("0");
        nCount = s1.indexOf(".");
        if(nCount == nStart+1)
        {
            //do nothing
        }
        else
        {
            while(nStart == 0)
            {
                s1 = s1.substring(1,s1.length());
                nStart = s1.indexOf("0");
                nCount = s1.indexOf(".");
                if(nCount == nStart+1)
                {
                    //do nothing
                    break;
                }
            }
        }

        return s1;
    }

    public static String EncodeFormat(String str)
    {
        if(TextUtils.isEmpty(str))
            return "";
        int nCount = 0,nStart = 0,nLength =0;
        String s1 = str;
        String s2 = "";


        //delete ¥
        nStart = s1.indexOf("¥");
        while(nStart != -1)
        {
            s2 = s1.substring(0,nStart);
            s2 += s1.substring(nStart+1,s1.length());
            s1 = s2;
            nStart = s1.indexOf("¥");
        }

        //delete ,
        nStart = s1.indexOf(",");
        while(nStart != -1)
        {
            s2 = s1.substring(0,nStart);
            s2 += s1.substring(nStart+1,s1.length());
            s1 = s2;
            nStart = s1.indexOf(",");
        }

        //delete 0
        nStart = s1.indexOf("0");
        nCount = s1.indexOf(".");
        if(nCount != -1){
            if(nCount == nStart+1)
            {
                //do nothing
            }
            else
            {
                while(nStart == 0)
                {
                    s1 = s1.substring(1,s1.length());
                    nStart = s1.indexOf("0");
                    nCount = s1.indexOf(".");
                    if(nCount == nStart+1)
                    {
                        //do nothing
                        break;
                    }
                }
            }
        }
        else{
            while(nStart == 0 && s1.length()>1)
            {
                s1 = s1.substring(1,s1.length());
                nStart = s1.indexOf("0");
            }
        }


        s2 = s1;

        nStart = 0;
        if(s1.indexOf(".") == -1)
        {
            nLength = s2.length();
            //not find out
            s1="¥";
            nCount = nLength%3;
            s1 += s2.substring(nStart,nCount);
            nStart = nCount;

            nCount = nLength/3;
            for(int i=0;i<nCount;i++)
            {
                if(nLength%3 != 0)
                {
                    s1 += ",";
                }
                else
                {
                    if(i!=0)
                    {
                        s1 += ",";
                    }
                }
                s1 += s2.substring(nStart,nStart+3);
                nStart += 3;
            }
            s1 += ".00";
        }
        else
        {
            nLength = s1.indexOf(".");
            s1="¥";
            nCount = nLength%3;
            s1 += s2.substring(nStart,nCount);
            nStart = nCount;

            nCount = nLength/3;
            for(int i=0;i<nCount;i++)
            {
                if(nLength%3 != 0)
                {
                    s1 += ",";
                }
                else
                {
                    if(i!=0)
                    {
                        s1 += ",";
                    }
                }
                s1 += s2.substring(nStart,nStart+3);
                nStart += 3;
            }

            s1 += s2.substring(nLength,s2.length());

            if(s1.indexOf(".")==s1.length()-2)
            {
                s1 += "0";
            }
        }


        if(s1.indexOf(".")==1){
            s1=s1.replace("¥", "¥0");
        }
        if(s1.indexOf(".")==s1.length()-1){
            s1 += "00";
        }

        return s1;
        //Toast.makeText(getApplicationContext(), ""+s1, 0).show();
    }

    public static String EncodeFormat2(String str) {
        boolean nCount = false;
        boolean nStart = false;
        boolean nLength = false;
        String s1 = str;
        String s2 = "";

        int var8;
        for(var8 = str.indexOf("￥"); var8 != -1; var8 = s2.indexOf("￥")) {
            s2 = s1.substring(0, var8);
            s2 = s2 + s1.substring(var8 + 1, s1.length());
            s1 = s2;
        }

        for(var8 = s1.indexOf(","); var8 != -1; var8 = s2.indexOf(",")) {
            s2 = s1.substring(0, var8);
            s2 = s2 + s1.substring(var8 + 1, s1.length());
            s1 = s2;
        }

        var8 = s1.indexOf("0");
        int var7 = s1.indexOf(".");
        if(var7 != -1) {
            if(var7 != var8 + 1) {
                while(var8 == 0) {
                    s1 = s1.substring(1, s1.length());
                    var8 = s1.indexOf("0");
                    var7 = s1.indexOf(".");
                    if(var7 == var8 + 1) {
                        break;
                    }
                }
            }
        } else {
            while(var8 == 0 && s1.length() > 1) {
                s1 = s1.substring(1, s1.length());
                var8 = s1.indexOf("0");
            }
        }

        s2 = s1;
        byte var10 = 0;
        int i;
        int var9;
        if(s1.indexOf(".") == -1) {
            var9 = s1.length();
            s1 = "￥";
            var7 = var9 % 3;
            s1 = s1 + s2.substring(var10, var7);
            var8 = var7;
            var7 = var9 / 3;

            for(i = 0; i < var7; ++i) {
                if(var9 % 3 != 0) {
                    s1 = s1 + ",";
                } else if(i != 0) {
                    s1 = s1 + ",";
                }

                s1 = s1 + s2.substring(var8, var8 + 3);
                var8 += 3;
            }

            s1 = s1 + ".00";
        } else {
            var9 = s1.indexOf(".");
            s1 = "￥";
            var7 = var9 % 3;
            s1 = s1 + s2.substring(var10, var7);
            var8 = var7;
            var7 = var9 / 3;

            for(i = 0; i < var7; ++i) {
                if(var9 % 3 != 0) {
                    s1 = s1 + ",";
                } else if(i != 0) {
                    s1 = s1 + ",";
                }

                s1 = s1 + s2.substring(var8, var8 + 3);
                var8 += 3;
            }

            s1 = s1 + s2.substring(var9, s2.length());
            if(s1.indexOf(".") == s1.length() - 2) {
                s1 = s1 + "0";
            }
        }

        if(s1.indexOf(".") == 1) {
            s1 = s1.replace("￥", "￥0");
        }

        if(s1.indexOf(".") == s1.length() - 1) {
            s1 = s1 + "00";
        }

        return s1;
    }

}

