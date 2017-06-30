package com.github.ittalks.commons.webservice.impl.rs;

import com.github.ittalks.commons.webservice.entity.User;
import com.github.ittalks.commons.webservice.entity.Users;
import com.github.ittalks.commons.webservice.IUserWebServcie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘春龙 on 2017/4/28.
 *
 * jax-rs服务实现
 */
@Service("userWebService")
public class UserWebService implements IUserWebServcie {

    @Override
    public Users queryUser() {
        List<User> list = new ArrayList<User>();
        User user = new User();
        user.setAge("20");
        user.setName("Tom");
        list.add(user);

        Users users = new Users();
        users.setUsers(list);
        return users;
    }
}
