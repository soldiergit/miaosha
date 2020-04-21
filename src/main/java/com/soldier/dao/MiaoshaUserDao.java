package com.soldier.dao;

import com.soldier.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author soldier
 * @Date 20-4-18 上午10:38
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Mapper
public interface MiaoshaUserDao {

    MiaoshaUser getById(@Param("id")long id);

}
