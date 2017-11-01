package com.github.ittalks.fn.web.controller;

import com.github.ittalks.fn.common.advice.exception.NestedException;
import com.github.ittalks.fn.common.result.APIResult;
import com.github.ittalks.fn.common.result.ErrorCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/2/16.
 */
@Controller
@RequestMapping("/google/oauth2")
public class GoogleOauth2Controller {

    public static final Logger logger = Logger.getLogger(GoogleOauth2Controller.class.getName());


    @RequestMapping(value = "auth/web/{userid}", method = RequestMethod.GET)
    public void auth(HttpServletRequest request, HttpServletResponse response, @PathVariable("userid") String userid) throws IOException {

        //请求参数userid
        logger.info("用户请求授权， 用户ID:" + userid);

        //设置Cookie
        Cookie cookie = new Cookie("user_id", userid);
        cookie.setPath("/");
        cookie.setMaxAge(1 * 60 * 60 * 1000);
        response.addCookie(cookie);

        response.sendRedirect(request.getContextPath() + "/google/oauth2/code");
    }

    @RequestMapping(value = "/{result}/Callback", method = RequestMethod.GET, produces = {MediaType
            .APPLICATION_JSON_VALUE})
    @ResponseBody
    public Object Callback(HttpServletRequest request,
                           @PathVariable("result") String result) {
        APIResult apiResult = null;

        if (result != null && "success".equals(result.toLowerCase())) {
            apiResult = APIResult.Y();
            return apiResult;
        } else {
            throw new NestedException(ErrorCode.GOOGLE_OAUTH2_WEB_FAIL);
        }

    }
}
