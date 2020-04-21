package com.soldier.controller;

import com.soldier.domain.MiaoshaOrder;
import com.soldier.domain.MiaoshaUser;
import com.soldier.domain.OrderInfo;
import com.soldier.result.CodeMsg;
import com.soldier.service.GoodsService;
import com.soldier.service.MiaoshaService;
import com.soldier.service.MiaoshaUserService;
import com.soldier.service.OrderService;
import com.soldier.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author soldier
 * @Date 20-4-21 上午9:08
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Controller
@RequestMapping("/miaosha")
public class MIaoshaController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String miaosha(@RequestParam("goodsId") Long goodsId, MiaoshaUser miaoshaUser, Model model){

        if (miaoshaUser == null) {
            return "login";
        }

        //判断库存
        GoodsVo goodsVo = goodsService.selectGoodsVoByGoodsId(goodsId);
        Integer stockCount = goodsVo.getStockCount();
        if (stockCount <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER);
            return "miaosha_fail";
        }

        //判断是否已经秒杀到了
        MiaoshaOrder miaoshaOrder = orderService.selectMiaoshaOrderByMiaoshaUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (miaoshaOrder != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA);
            return "miaosha_fail";
        }

        //减库存 下订单 写入秒杀订单（三步必须放入一个事务里提交）
        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser, goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goodsVo", goodsVo);

        return "order_detail";
    }
}
