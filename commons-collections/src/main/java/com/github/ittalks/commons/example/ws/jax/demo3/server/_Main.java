package com.github.ittalks.commons.example.ws.jax.demo3.server;

import java.util.List;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
public class _Main {

    public static void main(String[] args) {
        MyWebServiceImplService service = new MyWebServiceImplService();
        MyWebServiceImpl port = service.getMyWebServiceImplPort();

        User user = new User();
        user.setName("admin");
        user.setPassword("123");
        List<Role> roleList = port.getRoleByUser(user);

        for(Role role : roleList) {
            System.out.println(role.getId() + "," + role.getRoleName());
        }
    }
}
