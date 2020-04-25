package com.soldier.access;

import com.soldier.domain.MiaoshaUser;

/**
 * @Author soldier
 * @Date 20-4-25 下午2:08
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
public class UserContext {

	// 绑定当前线程 保证线程安全
	private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();
	
	public static void setUser(MiaoshaUser user) {
		userHolder.set(user);
	}
	
	public static MiaoshaUser getUser() {
		return userHolder.get();
	}

}
