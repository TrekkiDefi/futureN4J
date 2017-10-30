package com.github.ittalks.commons.example.ws.jax.demo3.client;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
@WebService
public class MyWebServiceImpl implements MyWebService {

    @Override
    public List<Role> getRoleByUser(User user) {
        List<Role> roleList = new ArrayList<Role>();
        if (user != null) {
            if (user.getName().equals("admin") && user.getPassword().equals("123")) {
                roleList.add(new Role(1, "技术总监"));
                roleList.add(new Role(2, "架构师"));
            } else if (user.getName().equals("eson15") && user.getPassword().equals("123")) {
                roleList.add(new Role(3, "java菜鸟"));
            }
            return roleList;
        }
        return null;
    }
}