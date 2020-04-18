package com.soldier.redis;

/**
 * @Author soldier
 * @Date 20-4-18 上午8:04
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:redis数据库中key的前缀，区分不同部门的不同用户，防止出现key相同被覆盖
 */
public interface KeyPrefix {

    /**
     * 前缀的过期秒数,默认0为永不过期
     */
    public int expireSeconds();

    /**
     * 获取前缀
     */
    public String getPrefix();
}
