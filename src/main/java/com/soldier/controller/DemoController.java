package com.soldier.controller;

import com.soldier.domain.User;
import com.soldier.redis.prefix.UserKey;
import com.soldier.result.Result;
import com.soldier.service.RedisService;
import com.soldier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Author soldier
 * @Date 20-4-16 上午10:10
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:返回一个视图
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "soldier");
        return "hello";
    }

    @RequestMapping("/select/{id}")
    public String select(@PathVariable int id, Model model) {
        User user = userService.selectById(id);
        model.addAttribute("name", user == null ? "不存在" : user.getName());
        return "hello";
    }

    @RequestMapping("/db/tx")
    public String tx() {
        userService.tx();
        return "hello";
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById, ""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User(3, "kugua");
        boolean set = redisService.set(UserKey.getById, "" + 1, user);
        return Result.success(set);
    }
}
