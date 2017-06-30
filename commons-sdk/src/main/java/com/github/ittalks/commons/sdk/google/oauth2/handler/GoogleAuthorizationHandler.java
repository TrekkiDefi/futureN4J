package com.github.ittalks.commons.sdk.google.oauth2.handler;

import com.github.ittalks.commons.sdk.google.client.common.Constraints;
import com.github.ittalks.commons.sdk.google.oauth2.proxy.AuthorizationCodeFlowProxy;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/2/17.
 */
public class GoogleAuthorizationHandler {

    private static Logger logger = Logger.getLogger("GoogleAuthorizationHandler");

    private static Oauth2 oauth2;
    private static GoogleClientSecrets clientSecrets;

    public static Credential buildOauth2(HttpServletRequest req) {
        try {
            //get user_id from cookie
            String userid = getUserId(req);

            // authorization
            Credential credential = authorize(userid);
            clientSecrets = AuthorizationCodeFlowProxy.clientSecrets;
            // set up global Oauth2 instance
            oauth2 = new Oauth2.Builder(AuthorizationCodeFlowProxy.httpTransport, AuthorizationCodeFlowProxy.JSON_FACTORY, credential).setApplicationName(
                    Constraints.APPLICATION_NAME).build();

            logger.info("AccessToken:" + credential.getAccessToken());
            logger.info("RefreshToken:" + credential.getRefreshToken());

            // run commands
            tokenInfo(credential.getAccessToken());
            userInfo();

            // success!
            return credential;
        } catch (IOException e) {
            logger.info(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize(String userid) throws Exception {


        // set up authorization core flow
        GoogleAuthorizationCodeFlow flow = (GoogleAuthorizationCodeFlow) AuthorizationCodeFlowProxy.initializeFlow();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(userid);
    }

    private static Tokeninfo tokenInfo(String accessToken) throws IOException {
        header("Validating a token");
        Tokeninfo tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
        logger.info(tokeninfo.toPrettyString());
        if (!tokeninfo.getAudience().equals(clientSecrets.getDetails().getClientId())) {
            logger.info("ERROR: audience does not match our google ID!");
            return null;
        }
        return tokeninfo;
    }

    private static Userinfoplus userInfo() throws IOException {
        header("Obtaining User Profile Information");
        Userinfoplus userinfo = oauth2.userinfo().get().execute();
        logger.info(userinfo.toPrettyString());
        return userinfo;
    }

    static void header(String name) {
        logger.info("");
        logger.info("================== " + name + " ==================");
        logger.info("");
    }

    /**
     * 返回请求的唯一标识：用户ID
     *
     * @param httpServletRequest
     * @return 用户ID
     * @throws ServletException
     * @throws IOException
     */
    private static String getUserId(HttpServletRequest httpServletRequest) throws ServletException, IOException {
        String userId = httpServletRequest.getParameter("user_id");
        logger.info("本地安装的应用程序OAUTH2授权，当前用户ID:" + userId);
//        Cookie[] cookies = httpServletRequest.getCookies();
//        for (Cookie cookie : cookies) {
//            if(cookie.getName().equals("user_id")) {
//                String userID = cookie.getValue();
//                logger.info("Get accessToken user_id:" + userID);
//                return userID;
//            }
//        }
        return userId;
    }
}
