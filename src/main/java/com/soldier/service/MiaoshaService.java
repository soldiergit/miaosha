package com.soldier.service;

import com.soldier.domain.Goods;
import com.soldier.domain.MiaoshaUser;
import com.soldier.domain.OrderInfo;
import com.soldier.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author soldier
 * @Date 20-4-21 上午9:20
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:秒杀操作service
 */
@Service
public class MiaoshaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    /**
     * 减少秒杀商品库存 下订单 写入秒杀订单（三步必须放入一个事务里提交）
     * @param miaoshaUser
     * @param goodsVo
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goodsVo) {

        //减库存 下订单 写入秒杀订单
        goodsService.reduceMiaoshaGoodsStock(goodsVo);

        //order_info maiosha_order
        return orderService.createOrder(miaoshaUser, goodsVo);
    }
}
