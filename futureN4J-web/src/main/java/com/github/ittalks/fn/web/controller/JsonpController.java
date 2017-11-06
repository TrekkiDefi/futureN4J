package com.github.ittalks.fn.web.controller;

import com.github.ittalks.commons.example.ws.cxf.integration.server.User;
import com.github.ittalks.commons.jackson.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 刘春龙 on 2017/5/25.
 */
@Controller
@RequestMapping("/jsonp")
public class JsonpController {

    // method({"name":"ittalks","address":"北京","email":"631521383@qq.com"})
    public static void main(String args[]) {
        User user = new User();
        user.setEmail("631521383@qq.com");
        user.setAddress("北京");
        user.setName("凡派,");
        System.out.println(JsonUtils.buildNonEmptyMapper().toJsonP("method", user));
    }

    // 请求地址：http://127.0.0.1:9090/futureN4J/jsonp?callback=fnpac
    // 相应结果：/**/fnpac({"name":"凡派,","address":"北京","email":"631521383@qq.com"});
    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User jsonp() {
        User user = new User();
        user.setEmail("631521383@qq.com");
        user.setAddress("北京");
        user.setName("凡派,");
        return user;
    }
}
