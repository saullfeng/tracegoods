package com.jmtc.tracegoods.services.impl;

import com.jmtc.tracegoods.domain.User;
import com.jmtc.tracegoods.services.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Chris
 * @date 2021/6/8 13:04
 * @Email:gang.wu@nexgaming.com
 */
@Service("UserService")
public class UserServiceImpl implements UserService {
    @Override
    public User findUserById(String userId) {
        User user = new User();
        user.setId("100");
        user.setName("JRTest");
        user.setPassword("test");

        return user;
    }
}
