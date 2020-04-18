package com.soldier.redis.prefix;

import com.soldier.redis.BasePrefix;

/**
 * @Author soldier
 * @Date 20-4-18 上午8:23
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:对于订单的redis-key
 */
public class OrderKey extends BasePrefix {

    // 不让外部篡改
    private OrderKey(String prefix) {
        super(prefix);
    }

    // 创建一个UserKey：过期时间为0， 前缀为className+prefix=OrderKey:id
    public static OrderKey getById = new OrderKey("id");

    // 创建一个UserKey：过期时间为0， 前缀为className+prefix=OrderKey:name
    public static OrderKey getByName = new OrderKey("name");

}
