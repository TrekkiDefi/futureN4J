package com.github.ittalks.commons.webservice;

import com.github.ittalks.commons.webservice.entity.Users;
import org.apache.cxf.annotations.EndpointProperty;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by 刘春龙 on 2017/4/28.
 *
 * jax-ws接口
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
@EndpointProperty(ref = "HelloWebService", key = "helloServer")
public interface IHelloWebService {
    public String conveyMap(@WebParam(name = "users") Users users, int index);
}
