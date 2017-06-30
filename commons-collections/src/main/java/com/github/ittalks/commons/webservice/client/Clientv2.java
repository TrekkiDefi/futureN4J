package com.github.ittalks.commons.webservice.client;

import com.github.ittalks.commons.webservice.IHelloWebService;
import com.github.ittalks.commons.webservice.entity.User;
import com.github.ittalks.commons.webservice.entity.Users;
import com.github.ittalks.commons.webservice.IUserWebServcie;
import com.github.ittalks.fn.common.result.APIResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

/**
 * Created by 刘春龙 on 2017/5/3.
 */
@DependsOn("userService") //保证userService先被加载并初始化
@Controller
@RequestMapping("/ws/client")
public class Clientv2 {

    private Logger logger = LoggerFactory.getLogger(Clientv2.class);

    @Autowired
    @Qualifier("helloService")
    private IHelloWebService helloWebService;

    @Autowired
    @Qualifier("userService")
    private IUserWebServcie userWebServcie;

    //http://localhost:9090/futureN4J/ws/client/helloService/conveyMap
    @RequestMapping(value = "/helloService/conveyMap", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public Object conveyMap(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, User> map = new HashMap<String, User>();
        User tom = new User();
        tom.setName("tom");
        map.put("1", tom);
        User Dave = new User();
        Dave.setName("Dave");
        map.put("2", Dave);

        Users users = new Users();
        users.setMaps(map);

        String result = helloWebService.conveyMap(users, 1);
        return APIResult.Y(result);
    }

    //http://localhost:9090/futureN4J/ws/client/userService/queryUser
    @RequestMapping(value = "/userService/queryUser", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public Object queryUser(HttpServletRequest request, HttpServletResponse response) {
        Users users = userWebServcie.queryUser();
        return APIResult.Y(users);
    }
}
