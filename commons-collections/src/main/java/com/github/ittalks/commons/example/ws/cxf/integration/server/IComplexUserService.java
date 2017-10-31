package com.github.ittalks.commons.example.ws.cxf.integration.server;

import com.github.ittalks.commons.example.ws.cxf.integration.model.User;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
public interface IComplexUserService {

    User getUserByName(String name);
    void setUser(User user);
}
