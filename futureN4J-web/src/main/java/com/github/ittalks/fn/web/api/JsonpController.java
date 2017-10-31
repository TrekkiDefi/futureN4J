package com.github.ittalks.fn.web.api;

import com.github.ittalks.commons.example.ws.cxf.integration.model.User;
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

    //method({"id":0,"name":"ittalks","email":"631521383@qq.com","address":"北京"})
    public static void main(String args[]) {
        User user = new User();
        user.setEmail("631521383@qq.com");
        user.setAddress("北京");
        user.setName("ittalks");
        System.out.println(JsonUtils.buildNonEmptyMapper().toJsonP("method", user));
    }

    ///**/method({"id":0,"name":"ittalks","email":"631521383@qq.com","address":"北京"});
    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User jsonp() {
        User user = new User();
        user.setEmail("631521383@qq.com");
        user.setAddress("北京");
        user.setName("ittalks");
        return user;
    }
}
