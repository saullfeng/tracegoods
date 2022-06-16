package com.jmtc.tracegoods.services;

import com.jmtc.tracegoods.domain.User;

/**
 * @author Chris
 * @date 2021/6/8 13:03
 * @Email:gang.wu@nexgaming.com
 */
public interface UserService {
    User findUserById(String userId);
}
