package com.soldier.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author soldier
 * @Date 20-4-18 上午10:28
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:正则校验
 */
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{4,8}$");

    public static boolean isMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) return false;
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();

    }

}
