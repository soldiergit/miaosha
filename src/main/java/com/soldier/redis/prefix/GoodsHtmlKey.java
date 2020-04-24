package com.soldier.redis.prefix;

import com.soldier.redis.BasePrefix;

/**
 * @Author soldier
 * @Date 20-4-24 下午3:36
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:(秒杀)商品列表页面实现页面缓存的redis-key
 */
public class GoodsHtmlKey extends BasePrefix {

    // 不让外部篡改
    private GoodsHtmlKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    // 创建一个GoodsHtmlKey：过期时间为60秒， 前缀为className+prefix=GoodsHtmlKey:gl
    public static GoodsHtmlKey getGoodsList = new GoodsHtmlKey(60, "gl");

    // 创建一个GoodsHtmlKey：过期时间为60秒， 前缀为className+prefix=GoodsHtmlKey:gd
    public static GoodsHtmlKey getGoodsDetail = new GoodsHtmlKey(60, "gd");
}
