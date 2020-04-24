package com.soldier.util;

import com.alibaba.fastjson.JSON;

/**
 * @Author soldier
 * @Date 20-4-24 下午3:53
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
public class JsonUtil {

    /**
     * 实体转字符串
     */
    public static  <T> String beanToStr(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return ""+value;
        } else if (clazz == String.class) {
            return (String)value;
        } else if (clazz == long.class || clazz == Long.class) {
            return ""+value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 字符串转实体
     */
    public static  <T> T strToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }
}
