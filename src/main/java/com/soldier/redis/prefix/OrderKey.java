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
    private OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
