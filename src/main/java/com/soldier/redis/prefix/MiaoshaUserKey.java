package com.soldier.redis.prefix;

import com.soldier.redis.BasePrefix;

/**
 * @Author soldier
 * @Date 20-4-18 下午6:35
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:对于秒杀用户的redis-key
 */
public class MiaoshaUserKey extends BasePrefix {

    // 两天有效期
    public static final int TOKEN_EXPIRE_SECONDS = 3600 * 24 * 2;

    // 不让外部篡改
    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    // 创建一个UserKey：过期时间为0， 前缀为className+prefix=MiaoshaUserKey:token
    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE_SECONDS, "tk");

    // 创建一个UserKey：过期时间为0， 前缀为className+prefix=MiaoshaUserKey:id
    public static MiaoshaUserKey getById = new MiaoshaUserKey(0, "id");
}
