package com.soldier.service;

import com.soldier.domain.Goods;
import com.soldier.domain.MiaoshaOrder;
import com.soldier.domain.MiaoshaUser;
import com.soldier.domain.OrderInfo;
import com.soldier.redis.prefix.MiaoshaKey;
import com.soldier.util.MD5Util;
import com.soldier.util.UUIDUtil;
import com.soldier.util.VerifyCodeUtil;
import com.soldier.vo.GoodsVo;
import com.soldier.vo.VerifyCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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
            return getGoodsOverStatus(goodsId);
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
    private long getGoodsOverStatus(Long goodsId) {
        Boolean isOver = redisService.get(MiaoshaKey.isGoodsOver, "" + goodsId, Boolean.class);
        // 如果redis中也不存在值，表明订单还没生产
        if (isOver == null) {
            return 0;
        }
        // 没有商品了，秒杀失败
        else if (isOver) {
            return -1;
        } else {
            // 订单还没生产，继续排队中
            return 0;
        }
    }

    /**
     * 生成验证码图片，并将结果保存到redis中
     */
    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        // 生成验证码
        VerifyCodeVo verifyCode = VerifyCodeUtil.createVerifyCode();
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, verifyCode.getCodeResult());
        //输出图片
        return verifyCode.getBufferedImage();
    }

    /**
     * 校验 验证码，利用redis
     */
    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        if(user == null || goodsId <=0) {
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        // 验证成功 删除验证码
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
        return true;
    }

    /**
     * 生成秒杀地址,保存到redis中
     */
    public String createMiaoshaPath(MiaoshaUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ goodsId, str);
        return str;
    }

    /**
     * 校验秒杀地址，读取redis
     * @param user      秒杀用户
     * @param goodsId   商品id
     * @param path      要校验地址
     */
    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
        if(user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ goodsId, String.class);
        return path.equals(pathOld);
    }
}
