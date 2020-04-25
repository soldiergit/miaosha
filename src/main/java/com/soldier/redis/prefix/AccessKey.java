package com.soldier.redis.prefix;

import com.soldier.redis.BasePrefix;

/**
 * @Author soldier
 * @Date 20-4-25 上午11:09
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:接口限流防刷的redis-key
 */
public class AccessKey extends BasePrefix {

    private AccessKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    // 创建一个AccessKey：过期时间为expireSeconds， 前缀为className+prefix=AccessKey:access
    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }

}

