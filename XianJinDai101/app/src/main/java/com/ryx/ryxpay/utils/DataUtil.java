package com.ryx.ryxpay.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XCC on 2016/5/9.
 */
public class DataUtil {
    /**
     * 验证是不是手机号
     * @param mobiles 参数
     * @return 验证结果
     */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

        Matcher m = p.matcher(mobiles);
        return m.matches();

    }
}
