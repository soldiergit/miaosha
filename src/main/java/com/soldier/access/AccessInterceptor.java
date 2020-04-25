package com.soldier.access;

import com.alibaba.fastjson.JSON;
import com.soldier.domain.MiaoshaUser;
import com.soldier.redis.prefix.AccessKey;
import com.soldier.result.CodeMsg;
import com.soldier.result.Result;
import com.soldier.service.MiaoshaUserService;
import com.soldier.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @Author soldier
 * @Date 20-4-25 下午2:08
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:接口限流防刷注解（@AccessLimit）拦截器
 */
@Service
public class AccessInterceptor  extends HandlerInterceptorAdapter {
	
	@Autowired
	private MiaoshaUserService userService;
	
	@Autowired
	private RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(handler instanceof HandlerMethod) {
			// 获取用户信息
			MiaoshaUser user = getUser(request, response);
			UserContext.setUser(user);
			HandlerMethod hm = (HandlerMethod)handler;
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if(accessLimit == null) {
				return true;
			}
			// 读取注解设置的参数
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			// 如果需要登录
			if(needLogin) {
				if(user == null) {
					render(response, CodeMsg.SESSION_ERROR);
					return false;
				}
				key += "_" + user.getId();
			}else {
				//do nothing
			}
			// 过期时间为用户设置的seconds 即多少秒内
			AccessKey accessKey = AccessKey.withExpire(seconds);
			// 获取这个用户seconds秒内刷了多少次接口
			Integer count = redisService.get(accessKey, key, Integer.class);
			// 第一次访问接口
	    	if(count  == null) {
	    		// seconds秒了访问了一次该接口
				redisService.set(accessKey, key, 1);
	    	}else if(count < maxCount) {
	    		// 不是第一次访问 访问次数加1
				redisService.incr(accessKey, key);
	    	}else {
	    		// 超出限制时报错 阻止执行接口
	    		render(response, CodeMsg.ACCESS_LIMIT_REACHED);
	    		return false;
	    	}
		}
		return true;
	}

	/**
	 * 报错，将错误信息写出去
	 * @param response	响应对象
	 * @param cm		错误信息
	 */
	private void render(HttpServletResponse response, CodeMsg cm)throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String str  = JSON.toJSONString(Result.error(cm));
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	// 获取用户
	private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return userService.getByToken(response, token);
	}


	/**
	 * 获取request中所有的cookie，遍历取值
	 * @param request           请求头
	 * @param cookieName        cookie的名称
	 */
	private String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[]  cookies = request.getCookies();
		if(cookies == null || cookies.length <= 0){
			return null;
		}
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookieName)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
}
