package com.soldier.service;

import com.soldier.dao.MiaoshaUserDao;
import com.soldier.domain.MiaoshaUser;
import com.soldier.exception.GlobalException;
import com.soldier.redis.prefix.MiaoshaUserKey;
import com.soldier.result.CodeMsg;
import com.soldier.util.MD5Util;
import com.soldier.util.UUIDUtil;
import com.soldier.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author soldier
 * @Date 20-4-18 上午10:39
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private MiaoshaUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;

    /**
     * 查询数据库
     */
    public MiaoshaUser getById(long id) {
        return miaoshaUserDao.getById(id);
    }

    /**
     * 登录操作
     */
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            // 直接抛出我们定义的全局异常，去那边处理
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //数据库的密码
        String dbPass = miaoshaUser.getPassword();
        //数据库的盐
        String saltDB = miaoshaUser.getSalt();
        //生成两次加密的密码
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        // 对比
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, miaoshaUser);
        return true;
    }

    /**
     * 根据token读取秒杀用户信息
     */
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    /**
     * 添加cookie到response里
     */
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser miaoshaUser) {
        // 拼接上cookie作为key，将秒杀用户信息保存到redis中 key=MiaoshaUserKey:tokenxxxxx value:miaoshaUser
        redisService.set(MiaoshaUserKey.token, token, miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        // 设置生命周期为MiaoshaUser的redis key的生命周期
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        // 正常一个cookie只能由创建它的应用获得，下面方法可以让这个cookie在同一应用服务器内共享
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
