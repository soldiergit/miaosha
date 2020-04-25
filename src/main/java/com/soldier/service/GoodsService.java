package com.soldier.service;

import com.soldier.dao.GoodsDao;
import com.soldier.domain.MiaoshaGoods;
import com.soldier.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author soldier
 * @Date 20-4-20 下午6:12
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:商品service
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo selectGoodsVoByGoodsId(Long goodsId) {
        return goodsDao.selectGoodsVoByGoodsId(goodsId);
    }

    // 减少秒杀商品库存
    public boolean reduceMiaoshaGoodsStock(GoodsVo goodsVo) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVo.getId());
        int ret = goodsDao.reduceMiaoshaGoodsStock(miaoshaGoods);
        return ret>0;
    }
}
