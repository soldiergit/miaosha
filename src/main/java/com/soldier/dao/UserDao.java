package com.soldier.dao;

import com.soldier.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author soldier
 * @Date 20-4-16 上午10:02
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User selectById(@Param("id")int id);

    @Insert("insert into user(id, name) values(#{id}, #{name})")
    public int insert(User user);
}
