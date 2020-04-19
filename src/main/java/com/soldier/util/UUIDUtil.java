package com.soldier.util;

import java.util.UUID;

/**
 * @Author soldier
 * @Date 20-4-20 上午12:43
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:uuid工具类
 */
public class UUIDUtil {

    public static String uuid() {
        // 通过用 newChar 字符替换字符串中出现的所有 oldChar 字符，并返回替换后的新字符串
        return UUID.randomUUID().toString().replace("-", "");
    }
}
