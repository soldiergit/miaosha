package com.soldier.config;

import com.soldier.domain.MiaoshaUser;
import com.soldier.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author soldier
 * @Date 20-4-19 下午6:15
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:秒杀用户参数解析器
 */
@Service
public class MiaoshaUserArgumentResolvers implements HandlerMethodArgumentResolver {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    /**
     * 支持参数，如果参数类型是MiaoshaUser才会执行resolveArgument方法
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == MiaoshaUser.class;
    }

    /**
     * 解决争论
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        // 取出我们的request和response对象，注意接口名称
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        //
        String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);

        // 找不到token时，返回空，然后在controller在进行判断
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }

        // 优先去参数里的token
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;

        // 返回一个秒杀用户对象
        return miaoshaUserService.getByToken(response, token);
    }

    /**
     * 获取request中所有的cookie，遍历取值
     * @param request           请求头
     * @param cookieName        cookie的名称
     */
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
