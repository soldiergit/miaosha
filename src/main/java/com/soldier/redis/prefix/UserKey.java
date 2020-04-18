package com.soldier.redis.prefix;

import com.soldier.redis.BasePrefix;

/**
 * @Author soldier
 * @Date 20-4-18 上午8:22
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:对于用户的redis-key
 */
public class UserKey extends BasePrefix {

    // 不让外部篡改
    private UserKey(String prefix) {
        super(prefix);
    }

    // 创建一个UserKey：过期时间为0， 前缀为className+prefix=UserKey:id
    public static UserKey getById = new UserKey("id");

    // 创建一个UserKey：过期时间为0， 前缀为className+prefix=UserKey:name
    public static UserKey getByName = new UserKey("name");

}
