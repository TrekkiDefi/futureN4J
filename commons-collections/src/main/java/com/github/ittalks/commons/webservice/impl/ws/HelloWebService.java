package com.github.ittalks.commons.webservice.impl.ws;

import com.github.ittalks.commons.webservice.IHelloWebService;
import com.github.ittalks.commons.webservice.entity.User;
import com.github.ittalks.commons.webservice.entity.Users;
import org.springframework.stereotype.Service;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by 刘春龙 on 2017/4/28.
 *
 * jax-ws服务实现
 */
@Service(value = "helloWebService")
@WebService(name = "helloServer", endpointInterface = "com.github.ittalks.commons.webservice.IHelloWebService", serviceName = "/hello")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@SuppressWarnings("deprecation")
public class HelloWebService implements IHelloWebService {

    @Override
    public String conveyMap(@WebParam(name = "users") Users users, int index) {
        User user = users.getMaps().get(Integer.valueOf(index).toString());
        return user.getName();
    }
}
