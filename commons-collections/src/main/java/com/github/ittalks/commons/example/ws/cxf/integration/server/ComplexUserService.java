package com.github.ittalks.commons.example.ws.cxf.integration.server;

import com.github.ittalks.commons.example.ws.cxf.integration.model.User;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.UUID;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class ComplexUserService implements IComplexUserService {

    @Override
    public User getUserByName(@WebParam(name = "name") String name) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(name);
        user.setAddress("china");
        user.setEmail(name + "@gmail.com");
        return user;
    }

    @Override
    public void setUser(User user) {
        System.out.println("############Server setUser###########");
        System.out.println("设置用户信息：" + user);
    }
}
