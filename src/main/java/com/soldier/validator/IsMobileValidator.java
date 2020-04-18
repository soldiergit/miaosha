package com.soldier.validator;

import com.soldier.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author soldier
 * @Date 20-4-18 下午5:43
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:实现参数校验器规则-判断手机号
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    // 判断被校验值是否允许为空
    private boolean required = false;

    /**
     * 初始化方法
     * @param constraintAnnotation 我们定义的IsMobile
     */
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        this.required = constraintAnnotation.required();

    }

    /**
     * 校验规则
     * @param s                             被校验的值
     * @param constraintValidatorContext    可以添加额外的错误消息，或者完全禁用默认的错误信息而使用完全自定义的错误信息
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 是否要校验
        if (this.required) {
            return ValidatorUtil.isMobile(s);
        } else {
            if (StringUtils.isEmpty(s)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
