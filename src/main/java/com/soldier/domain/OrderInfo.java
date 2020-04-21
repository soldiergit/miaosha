package com.soldier.domain;

import java.util.Date;

/**
 * @Author soldier
 * @Date 20-4-20 下午5:57
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:订单表
 */
public class OrderInfo {
    //订单id
    private Long id;
    //用户id
    private Long userId;
    //商品id
    private Long goodsId;
    //收货地址id
    private Long deliveryAddrId;
    //冗余过来的商品名称
    private String goodsName;
    //商品数量
    private Integer goodsCount;
    //商品单价
    private Double goodsPrice;
    //操作平台：1pc, 2android, 3ios
    private Integer orderChannel;
    //订单状态， 0新建未支付， 1已支付， 2已发货， 3已收货， 4已退款， 5已完成
    private Integer status;
    //订单创建的时间
    private Date createDate;
    //支付时间
    private Date payDate;

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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getDeliveryAddrId() {
        return deliveryAddrId;
    }

    public void setDeliveryAddrId(Long deliveryAddrId) {
        this.deliveryAddrId = deliveryAddrId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(Integer orderChannel) {
        this.orderChannel = orderChannel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
}
