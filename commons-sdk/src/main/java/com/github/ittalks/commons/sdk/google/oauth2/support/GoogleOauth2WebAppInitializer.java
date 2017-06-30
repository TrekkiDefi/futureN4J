package com.github.ittalks.commons.sdk.google.oauth2.support;

import com.github.ittalks.commons.sdk.google.oauth2.GoogleAuthorizationAccessTokenServlet;
import com.github.ittalks.commons.sdk.google.oauth2.GoogleAuthorizationCodeServlet;
import com.github.ittalks.commons.sdk.google.oauth2.GoogleAuthorizationServlet;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/2/15.
 */
public class GoogleOauth2WebAppInitializer implements WebApplicationInitializer {

    private static Logger logger = Logger.getLogger("GoogleClientOauth2WebAppInitializer");

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        ServletRegistration.Dynamic dispatcherAuth = servletContext.addServlet("googleOauth2Auth", new GoogleAuthorizationServlet());
        dispatcherAuth.setLoadOnStartup(1);
        dispatcherAuth.addMapping("/google/oauth2/auth/install");
        logger.info("Register GoogleAuthorizationServlet【本地安装的应用程序OAUTH2授权】 success!...");

        ServletRegistration.Dynamic dispatcherCode = servletContext.addServlet("googleOauth2Code", new GoogleAuthorizationCodeServlet());
        dispatcherCode.setLoadOnStartup(1);
        dispatcherCode.addMapping("/google/oauth2/code");
        logger.info("Register GoogleAuthorizationCodeServlet【Web服务器应用程序获取用户授权CODE】 success!...");

        ServletRegistration.Dynamic dispatcherAccessToken = servletContext.addServlet("googleOauth2AccessToken", new GoogleAuthorizationAccessTokenServlet());
        dispatcherAccessToken.setLoadOnStartup(1);
        dispatcherAccessToken.addMapping("/google/oauth2/accessToken");
        logger.info("Register GoogleAuthorizationAccessTokenServlet【Web服务器应用程序使用用户授权CODE换取访问令牌ACCESS_TOKEN】 success!...");
    }
}
