package com.soldier.vo;

import com.soldier.domain.OrderInfo;

/**
 * @Author soldier
 * @Date 20-4-25 上午9:06
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:订单详情vo对象
 */
public class OrderDetailVo {
    private GoodsVo goodsVo;
    private OrderInfo orderInfo;

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
