package com.jiawei.utils;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author : willian fu
 * @date : 2019-02-14.
 */
public class UrlUtil {
    private final static String ENCODE = "utf-8";
    /**
     * URL 解码
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * URL 编码
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Test
    public void getURLDecoderStringTest(){
        String url="https%3A%2F%2Fopen.bot.tmall.com%2Foauth%2Fcallback%3FskillId%3D11111111%26token%3DXXXXXXXXXX";
        System.out.println(getURLDecoderString(url));
    }
}
