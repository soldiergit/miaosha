package com.soldier.exception;

import com.soldier.result.CodeMsg;
import com.soldier.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author soldier
 * @Date 20-4-18 下午5:57
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:全局异常信息处理
 *  ControllerAdvice:是一个增强的 Controller,可以实现三个方面的功能：全局异常处理、全局数据绑定、全局数据预处理
 *  ExceptionHandler：注解用来指明异常的处理类型
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {

        if (e instanceof GlobalException) {

            GlobalException globalException = (GlobalException)e;

            // 将错误信息传过去，再格式化输出到前端(这里的codeMsg已经在MiaoshaUserService赋值)
            return Result.error(globalException.getCodeMsg());

        } else if (e instanceof BindException) {
            BindException bindException = (BindException)e;

            List<ObjectError> errors = bindException.getAllErrors();
            ObjectError error = errors.get(0);

            String message = error.getDefaultMessage();

            // 将错误信息传过去，再格式化输出到前端
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(message));

        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
