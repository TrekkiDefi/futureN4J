package com.github.ittalks.commons.sdk.google.oauth2;

import com.github.ittalks.commons.sdk.google.oauth2.handler.GoogleAuthorizationHandler;
import com.google.api.client.auth.oauth2.Credential;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 刘春龙 on 2017/2/17.
 * 本地安装的应用程序OAUTH2授权
 */
public class GoogleAuthorizationServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Credential credential = GoogleAuthorizationHandler.buildOauth2(req);

        resp.setContentType("application/json;charset=utf-8");
        if (credential != null && credential.getAccessToken() != null) {
            resp.getWriter().write("{\"success\": true," +
                    "\"msg\":\"本地安装的应用程序用户授权成功!\"}");
        } else {
            resp.getWriter().write("{\"success\": false," +
                    "\"msg\":\"本地安装的应用程序用户授权失败!\"}");
        }
    }
}
