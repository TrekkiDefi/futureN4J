package com.github.ittalks.commons.sdk.google.oauth2;

import com.github.ittalks.commons.sdk.google.client.common.config.Configration;
import com.github.ittalks.commons.sdk.google.oauth2.proxy.AuthorizationCodeFlowProxy;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.http.GenericUrl;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/2/15.
 *
 * Web服务器应用程序使用用户授权CODE换取访问令牌ACCESS_TOKEN
 */
public class GoogleAuthorizationAccessTokenServlet extends AbstractAuthorizationCodeCallbackServlet {

    private static final Logger logger = Logger.getLogger(GoogleAuthorizationAccessTokenServlet.class.getName());

    @Override
    protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential) throws ServletException, IOException {
        logger.info("AccessToken:" + credential.getAccessToken());
        logger.info("RefreshToken:" + credential.getRefreshToken());
        logger.info("AccessToken Expire:" + credential.getExpiresInSeconds());
        logger.info("Web服务器应用程序用户授权成功!");
        String callback = Configration.OAUTH2_CALLBACK.replace("{result}", "success");
        resp.sendRedirect(req.getContextPath() + callback);
    }

    @Override
    protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse) throws ServletException, IOException {
//        super.onError(req, resp, errorResponse);
        logger.info("Web服务器应用程序用户授权失败!");
        String callback = Configration.OAUTH2_CALLBACK.replace("{result}", "fail");
        resp.sendRedirect(req.getContextPath() + callback);
    }

    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
        return AuthorizationCodeFlowProxy.initializeFlow();
    }

    protected String getRedirectUri(HttpServletRequest httpServletRequest) throws ServletException, IOException {
        GenericUrl url = new GenericUrl(httpServletRequest.getRequestURL().toString());
        url.setRawPath(httpServletRequest.getContextPath() + Configration.OAUTH2_REDIRECT_URL);
        logger.info("使用用户授权CODE换取访问令牌ACCESS_TOKEN 回调Url : " + url.build());
        return url.build();
    }

    /**
     * 返回请求的唯一标识：用户ID
     *
     * @param httpServletRequest
     * @return 用户ID
     * @throws ServletException
     * @throws IOException
     */
    protected String getUserId(HttpServletRequest httpServletRequest) throws ServletException, IOException {
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user_id")) {
                String userid = cookie.getValue();
                logger.info("使用用户授权CODE换取访问令牌ACCESS_TOKEN，当前用户ID:" + userid);
                return userid;
            }
        }
        return null;
    }
}
