package com.soldier.domain;

/**
 * @Author soldier
 * @Date 20-4-20 下午5:57
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:秒杀订单表
 */
public class MiaoshaOrder {
    //秒杀订单id
    private Long id;
    //用户id
    private Long userId;
    //订单id
    private Long orderId;
    //商品id
    private Long goodsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
