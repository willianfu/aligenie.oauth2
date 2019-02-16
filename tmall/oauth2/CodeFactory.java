package com.jiawei.tmall.oauth2;

import org.junit.Test;

/**
 * @author uthor : willian fu
 * @date : 2019-02-14.
 *
 */
public class CodeFactory {
    /**
     * 获取随机code
     * @return code
     */
    public static String getCode(){
        //生成长度40的随机code
        String code = getRandomString(40);
        return code;
    }

    /**
     * 获取随机token
     * @return token
     */
    public static String getToken(){
        //生成长度42的随机token
        String token = getRandomString(42);
        return token;
    }
    /**
     * 字符串抽取随机
     * @param count
     * @return 随机数
     */
    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }
    //字符串模板
    private static String string = "abcdefghijk123456789lmnopqrstuvwxyz";

    /**
     * 生成随机字符串
     * @param length
     * @return 随机字符串
     */
    private static String getRandomString(int length){
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len-1)));
        }
        return sb.toString();
    }
    @Test
    public void getCodeTest(){
        System.out.println(getCode());
    }
}
