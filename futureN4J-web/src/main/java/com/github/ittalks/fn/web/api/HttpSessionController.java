package com.github.ittalks.fn.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by 刘春龙 on 2017/7/27.
 *
 * 测试基于redis的http session
 */
@Controller
@RequestMapping("/httpsession")
public class HttpSessionController {

    @RequestMapping(value = "/query", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object query(HttpServletRequest request) {
        request.getSession().setAttribute("accessTime", new Date());
        return HttpStatus.NO_CONTENT;
    }
}
