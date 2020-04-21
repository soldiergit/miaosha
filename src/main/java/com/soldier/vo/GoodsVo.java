package com.soldier.vo;

import com.soldier.domain.Goods;

import java.util.Date;

/**
 * @Author soldier
 * @Date 20-4-20 下午6:05
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:商品vo对象，包含商品信息和秒杀信息
 */
public class GoodsVo extends Goods {

    //秒杀价
    private Double miaoshaPrice;
    //库存数量
    private Integer stockCount;
    //秒杀开始时间
    private Date startDate;
    //秒杀结束时间
    private Date endDate;

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
