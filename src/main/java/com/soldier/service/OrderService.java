package com.soldier.service;

import com.soldier.dao.GoodsDao;
import com.soldier.dao.OrderDao;
import com.soldier.domain.MiaoshaOrder;
import com.soldier.domain.MiaoshaUser;
import com.soldier.domain.OrderInfo;
import com.soldier.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author soldier
 * @Date 20-4-21 上午9:13
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:订单service,包括基本订单和秒杀订单
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public MiaoshaOrder selectMiaoshaOrderByMiaoshaUserIdAndGoodsId(Long miaoshaUserId, Long goodsId) {
        return orderDao.selectMiaoshaOrderByMiaoshaUserIdAndGoodsId(miaoshaUserId, goodsId);
    }

    /**
     * 新增订单和新增秒杀订单 两步必须放入一个事务里提交
     */
    @Transactional
    public OrderInfo createOrder(MiaoshaUser miaoshaUser, GoodsVo goodsVo) {

        //基本订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(miaoshaUser.getId());
        long orderId = orderDao.insert(orderInfo);

        //秒杀订单
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(miaoshaUser.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);

        return orderInfo;
    }
}
