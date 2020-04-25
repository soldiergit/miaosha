package com.soldier.service;

import com.soldier.domain.Goods;
import com.soldier.domain.MiaoshaOrder;
import com.soldier.domain.MiaoshaUser;
import com.soldier.domain.OrderInfo;
import com.soldier.redis.prefix.MiaoshaKey;
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

    @Autowired
    private RedisService redisService;

    /**
     * 减少秒杀商品库存 下订单 写入秒杀订单（三步必须放入一个事务里提交）
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goodsVo) {

        //减库存
        boolean success = goodsService.reduceMiaoshaGoodsStock(goodsVo);

        if (success) {
            // 下订单 写入秒杀订单order_info maiosha_order
            return orderService.createOrder(miaoshaUser, goodsVo);
        } else {
            // 减库存失败
            setGoodsOver(goodsVo.getId());
            return null;
        }
    }

    /**
     * 判断用户是否秒杀到商品，即判断订单是否存在
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * @param userId    秒杀用户id
     * @param goodsId   商品id
     */
    public long getMiaoshaResult(Long userId, long goodsId) {

        MiaoshaOrder miaoshaOrder = orderService.selectMiaoshaOrderByMiaoshaUserIdAndGoodsId(userId, goodsId);

        //成功
        if (miaoshaOrder != null) {
            return miaoshaOrder.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            // 没有商品了，秒杀失败
            if (isOver) {
                return -1;
            } else {
                // 订单还没生产，继续排队中
                return 0;
            }
        }
    }

    /**
     * 将秒杀完了的商品存储到redis中
     */
    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    /**
     * 判断该商品是否存在被秒杀完的记录
     */
    private boolean getGoodsOver(Long goodsId) {
        return redisService.get(MiaoshaKey.isGoodsOver, ""+goodsId, Boolean.class);
    }
}
