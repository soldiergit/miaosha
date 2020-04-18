package com.soldier.domain;

/**
 * @Author soldier
 * @Date 20-4-16 上午10:01
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
public class User {

    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
