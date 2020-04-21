package com.soldier.service;

import com.soldier.dao.UserDao;
import com.soldier.domain.User;
import com.soldier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author soldier
 * @Date 20-4-16 上午10:07
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:用户service
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User selectById(int id) {
        return userDao.selectById(id);
    }

    public int insert(User user) {
        return 0;
    }

    /**
     * 测试事务
     *  数据库中id1存在，id2不存在
     *  有‘@Transactional’时，执行完后，会回滚，不会插入2
     *  没有‘@Transactional’时。会插入2
     *  所以@Transactional能保证事务
     */
    @Transactional
    public boolean tx() {
        User u1 = new User(2, "soldier");
        userDao.insert(u1);

        User u2 = new User(1, "shuaige");
        userDao.insert(u2);

        return true;
    }
}
