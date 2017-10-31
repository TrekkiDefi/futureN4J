package com.github.ittalks.commons.example.ws.jax.demo3.server;

import java.util.List;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
public interface MyWebService {
    List<Role> getRoleByUser(User user);
}
