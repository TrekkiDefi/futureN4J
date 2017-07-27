package com.github.ittalks.fn.web.api;

import com.github.ittalks.fn.common.advice.exception.NestedException;
import com.github.ittalks.fn.common.result.ErrorCode;
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

    @RequestMapping(value = "/internal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object internal() {
        throw new NullPointerException();
    }

    @RequestMapping(value = "/invalidParam", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object invalidParam() {
        throw new NestedException(ErrorCode.PARAM_INVALID);
    }
}
