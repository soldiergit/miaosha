package com.soldier.domain;

import com.soldier.vo.GoodsVo;

/**
 * @Author soldier
 * @Date 20-4-25 上午9:06
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:商品详情vo对象
 */
public class GoodsDetailVo {

    //秒杀状态
    private int miaoshaStatus = 0;
    //剩余时间
    private int remainSeconds = 0;
    //商品vo对象
    private GoodsVo goodsVo;
    //秒杀用户
    private MiaoshaUser miaoshaUser;

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public MiaoshaUser getMiaoshaUser() {
        return miaoshaUser;
    }

    public void setMiaoshaUser(MiaoshaUser miaoshaUser) {
        this.miaoshaUser = miaoshaUser;
    }
}
