package com.soldier.rabbitmq;

import com.soldier.domain.MiaoshaUser;

/**
 * @Author soldier
 * @Date 20-4-24 下午6:33
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:什么用户秒杀什么商品
 */
public class MiaoshaMessage {
    private MiaoshaUser miaoshaUser;
    private Long goodsId;

    public MiaoshaUser getMiaoshaUser() {
        return miaoshaUser;
    }

    public void setMiaoshaUser(MiaoshaUser miaoshaUser) {
        this.miaoshaUser = miaoshaUser;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
