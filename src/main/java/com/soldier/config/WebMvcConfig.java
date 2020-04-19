package com.soldier.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @Author soldier
 * @Date 20-4-19 下午6:06
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:自定义webMvc配置
 * 因为每次用户查看商品列表或者商品详情时都需要获取token，然后读取用户信息，业务方法比较冗余
 * 这样子我们的controller每次要获取秒杀用户信息时都会调用这里的参数解析器
 *
 *  Spring Boot2.0的版本WebMvcConfigurerAdapter已过时了
 *  新的实现是：
 *      1、implements WebMvcConfigurer
 *      2、extends WebMvcConfigurationSupport 推荐
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    // 注入秒杀用户参数解析器
    @Autowired
    private MiaoshaUserArgumentResolvers miaoshaUserArgumentResolvers;

    /**
     * 添加参数解析器
     * @param argumentResolvers 参数解析器
     */
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(miaoshaUserArgumentResolvers);
    }
}
