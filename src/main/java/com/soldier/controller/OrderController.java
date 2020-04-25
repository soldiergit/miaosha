package com.soldier.controller;

import com.soldier.domain.MiaoshaUser;
import com.soldier.domain.OrderInfo;
import com.soldier.result.CodeMsg;
import com.soldier.result.Result;
import com.soldier.service.GoodsService;
import com.soldier.service.MiaoshaUserService;
import com.soldier.service.OrderService;
import com.soldier.service.RedisService;
import com.soldier.vo.GoodsVo;
import com.soldier.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author soldier
 * @Date 20-4-21 上午9:08
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private MiaoshaUserService userService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user,
									  @RequestParam("orderId") long orderId) {
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.selectOrderById(orderId);
    	if(order == null) {
    		return Result.error(CodeMsg.ORDER_NOT_EXIST);
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVo goods = goodsService.selectGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrderInfo(order);
    	vo.setGoodsVo(goods);
    	return Result.success(vo);
    }
    
}
