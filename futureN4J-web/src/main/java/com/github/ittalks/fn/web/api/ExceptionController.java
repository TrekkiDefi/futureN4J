package com.github.ittalks.fn.web.api;

import com.github.ittalks.fn.common.advice.exception.NestedException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 刘春龙 on 2017/6/2.
 */
@Controller
@RequestMapping("/exception")
public class ExceptionController {

    /*
        HTTP/1.1 500 Internal Server Error
        Server: Apache-Coyote/1.1
        Content-Type: application/json;charset=UTF-8
        Transfer-Encoding: chunked
        Date: Fri, 02 Jun 2017 09:40:30 GMT
        Connection: close

        {"success": false,"code": "S01001","desc": "服务内部错误"}
     */
    @RequestMapping(value = "/internal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object internal() {
        throw new NullPointerException();
    }

    /*
        HTTP/1.1 400 Bad Request
        Server: Apache-Coyote/1.1
        Content-Type: application/json;charset=UTF-8
        Transfer-Encoding: chunked
        Date: Fri, 02 Jun 2017 09:40:51 GMT
        Connection: close

        {"success": false,"code": "R01002","desc": "缺少必填参数"}
     */
    @RequestMapping(value = "/custom", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object custom() {
        throw new NestedException();
    }
}
