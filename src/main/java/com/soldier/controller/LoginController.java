package com.soldier.controller;

import com.soldier.result.CodeMsg;
import com.soldier.result.Result;
import com.soldier.service.MiaoshaUserService;
import com.soldier.util.ValidatorUtil;
import com.soldier.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author soldier
 * @Date 20-4-18 上午9:34
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    /**
     *  @Valid表单验证
     */
    @RequestMapping("/do_login")
    @ResponseBody
//    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
    public Result<Boolean> doLogin(@Valid LoginVo loginVo){
        logger.info(loginVo.toString());

        //登录,没有抛出异常就证明没事
//        miaoshaUserService.login(response, loginVo);
        miaoshaUserService.login(loginVo);

        return Result.success(true);
    }
}
