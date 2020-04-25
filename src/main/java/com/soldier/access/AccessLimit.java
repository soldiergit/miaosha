package com.soldier.access;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author soldier
 * @Date 20-4-25 下午2:08
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:接口的限流防刷
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
	// 多少秒
	int seconds();
	// 一秒允许执行多少次
	int maxCount();
	// 是否需要登录
	boolean needLogin() default true;
}
