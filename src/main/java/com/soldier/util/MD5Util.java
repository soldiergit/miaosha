package com.soldier.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author soldier
 * @Date 20-4-18 上午9:18
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:两次MD5,MD5工具类
 */
public class MD5Util {

    //固定salt
    private static final String salt = "1a2b3c4d";

    //md5加密
    public static String md5(String data) {
        return DigestUtils.md5Hex(data);
    }

    // 第一次md5:将用户输入的+固定salt 做一次md5
    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    // 第二次md5：将第一次的值转为数据库存储
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    // 执行两次MD5
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        return formPassToDBPass(inputPassToFormPass(inputPass), saltDB);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));
    }
}
