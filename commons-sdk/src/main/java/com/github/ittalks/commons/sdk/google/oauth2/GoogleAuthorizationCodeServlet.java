package com.github.ittalks.commons.sdk.google.oauth2;

import com.github.ittalks.commons.sdk.google.client.common.config.Configration;
import com.github.ittalks.commons.sdk.google.oauth2.proxy.AuthorizationCodeFlowProxy;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.http.GenericUrl;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/2/20.
 *
 * Web服务器应用程序获取用户授权CODE
 * 返回码：405 - HTTP method * is not supported by this URL，This means that it has been authorized to pass。
 */
public class GoogleAuthorizationCodeServlet extends AbstractAuthorizationCodeServlet {

    public static final Logger logger = Logger.getLogger(GoogleAuthorizationCodeServlet.class.getName());
    @Override
    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {

        return AuthorizationCodeFlowProxy.initializeFlow();
    }

    @Override
    protected String getRedirectUri(HttpServletRequest httpServletRequest) throws ServletException, IOException {

        GenericUrl url = new GenericUrl(httpServletRequest.getRequestURL().toString());
        url.setRawPath(httpServletRequest.getContextPath() + Configration.OAUTH2_REDIRECT_URL);
        logger.info("获取用户授权CODE，回调Url :  " + url.build());
        return url.build();
    }

    @Override
    protected String getUserId(HttpServletRequest httpServletRequest) throws ServletException, IOException {

        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user_id")) {
                String userid = cookie.getValue();
                logger.info("获取用户授权CODE，当前用户ID:" + userid);
                return userid;
            }
        }
        return null;
    }
}
