package com.ryx.ryxcredit.utils;

import com.alibaba.fastjson.JSON;

/**
 * Author：lijing on 16/6/22 13:48
 * Mail：lijing1-jn@ruiyinxin.com
 * Description：JSON转换工具类
 */
public class CJSONUtils {

    private static CJSONUtils instance=new CJSONUtils ();
    public  synchronized  static  CJSONUtils getInstance(){

        return instance;
    }
    /**
     * json字符串转换成json对象
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T parseJSONObject(String jsonStr,Class<T> clazz){
        return JSON.parseObject (jsonStr,clazz);
    }

    /**
     * json对象转换成json字符串
     * @param result
     * @param <T>
     * @return
     */
    public <T> String toJSONString(T result){

       return JSON.toJSONString (result);
    }
}
