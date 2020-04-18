package com.soldier.redis;

/**
 * @Author soldier
 * @Date 20-4-18 上午8:08
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix) {
        // 0代表永不过期
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {//默认0为永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        // 获取当前类（子类等的）类名
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
