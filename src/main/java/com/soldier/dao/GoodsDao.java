package com.soldier.dao;

import com.soldier.domain.Goods;
import com.soldier.domain.MiaoshaGoods;
import com.soldier.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author soldier
 * @Date 20-4-20 下午6:12
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Mapper
public interface GoodsDao {

    //获取所有秒杀商品，及其基本信息
    List<GoodsVo> listGoodsVo();

    //根据商品id查询
    GoodsVo selectGoodsVoByGoodsId(@Param("goodsId") Long goodsId);

    // 减少基本商品库存
    void reduceGoodsStock(Goods goods);

    // 减少秒杀商品库存
    void reduceMiaoshaGoodsStock(MiaoshaGoods miaoshaGoods);
}
