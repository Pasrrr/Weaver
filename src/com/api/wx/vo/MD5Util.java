package com.api.wx.vo;

/**
 *
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密解密工具类
 * @author admin
 *
 */
public class MD5Util {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String s = "123456Ab";
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + string2MD5(s));

        System.out.println("可解密的加密算法，加密后：" + convertMD5(s));
        System.out.println("解密后：" + convertMD5(convertMD5(s)));
    }

    /**
     * 使用 MD5算法加密生成32位md5码
     * @param str
     * @return
     */
    public static String string2MD5(String str) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    str.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * 	可逆的加密解密算法,执行一次加密,两次解密
     * @param str
     * @return
     */
    public static String convertMD5(String str){

        char[] a = str.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

}
