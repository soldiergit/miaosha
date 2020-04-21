package com.soldier.controller;

import com.soldier.domain.MiaoshaUser;
import com.soldier.domain.User;
import com.soldier.service.GoodsService;
import com.soldier.service.MiaoshaUserService;
import com.soldier.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author soldier
 * @Date 20-4-19 下午4:53
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private GoodsService goodsService;

    // 废弃，我们从自定义的参数解析器中获取秒杀用户信息
//    /**
//     * 商品列表页
//     * @param model         模型
//     * @param response      响应头
//     * @param cookieToken   从浏览器cookie中取值
//     * @param paramToken    有时候手机端不会把token放进cookie里，是直接放进参数里传递
//     */
//    @RequestMapping("/to_list")
//    public String list(Model model, HttpServletResponse response,
//                          @CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//                          @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String paramToken){
//
//        // 找不到token时，返回登录页面
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//
//        // 优先去参数里的token
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//
//        // 根据token获取用户信息
//        MiaoshaUser miaoshaUser = miaoshaUserService.getByToken(response, token);
//        model.addAttribute("miaoshaUser", miaoshaUser);
//
//        return "goods_list";
//    }

    /**
     * 因为我们专门webmvc配置了针对秒杀用户的参数解析器
     *  然后我们从WebMvcConfig中定义的参数解析器中获取用户信息
     * @param model         模型
     * @param miaoshaUser   秒杀用户信息
     */
    @RequestMapping("/to_list")
    public String list(MiaoshaUser miaoshaUser, Model model){

        if (miaoshaUser == null) {
            return "login";
        }

        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();

        model.addAttribute("miaoshaUser", miaoshaUser);
        model.addAttribute("goodsVoList", goodsVoList);

        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(@PathVariable("goodsId") Long goodsId, MiaoshaUser miaoshaUser, Model model){

        if (miaoshaUser == null) {
            return "login";
        }

        GoodsVo goodsVo = goodsService.selectGoodsVoByGoodsId(goodsId);

        // 秒杀开始时间
        long startTime = goodsVo.getStartDate().getTime();
        // 秒杀结束时间时间
        long endTime = goodsVo.getEndDate().getTime();
        // 系统当前
        long now = System.currentTimeMillis();
        // 秒杀状态 0-倒计时 1-进行中 2-已结束
        int miaoshaStatus = 0;
        // 距离活动开始剩余时间
        int remainSeconds = 0;

        if (now < startTime) {//秒杀未开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startTime - now)/1000);
        } else if (now > endTime) {//秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaUser", miaoshaUser);
        model.addAttribute("goodsVo", goodsVo);
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goods_detail";
    }
}
