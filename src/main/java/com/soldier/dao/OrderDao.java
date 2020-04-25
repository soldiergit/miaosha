package com.soldier.dao;

import com.soldier.domain.MiaoshaOrder;
import com.soldier.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @Author soldier
 * @Date 20-4-21 上午9:29
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Mapper
public interface OrderDao {

    MiaoshaOrder selectMiaoshaOrderByMiaoshaUserIdAndGoodsId(
            @Param("userId") Long miaoshaUserId,
            @Param("goodsId") Long goodsId);

//    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    //SelectKey返回的id会放到对象里
    void insert(OrderInfo orderInfo);

    void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
