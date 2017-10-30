package com.ryx.swiper.utils;

/**
 * Created by laomao on 15/12/2.
 */
/**
 * 类说明： 字符串操作类
 *
 */
public class StringUtils {
    /**
     * 判断给定字符串是否空白串。<br>
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串<br>
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isBlank(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查中文名输 入是否正确
     *
     * @param value
     * @return
     */
    public boolean checkChineseName(String value, int length) {
        return value.matches("^[\u4e00-\u9fa5]+$") && value.length() <= length;
    }


    /**
     * 检查中文名输 入是否正确
     *
     * @param value
     * @return
     */
    public static boolean checkChineseName(String value) {
        return value.matches("^[\u4e00-\u9fa5]+$");
    }

}
