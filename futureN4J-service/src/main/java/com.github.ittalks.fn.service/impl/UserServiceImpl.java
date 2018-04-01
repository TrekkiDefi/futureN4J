package com.github.ittalks.fn.service.impl;

import com.github.ittalks.commons.example.webservice.cxf.integration.server.User;
import com.github.ittalks.fn.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by 刘春龙 on 2018/4/1.
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findOne() {
        User user = new User();
        user.setEmail("631521383@qq.com");
        user.setAddress("北京");
        user.setName("凡派,");
        return user;
    }
}
