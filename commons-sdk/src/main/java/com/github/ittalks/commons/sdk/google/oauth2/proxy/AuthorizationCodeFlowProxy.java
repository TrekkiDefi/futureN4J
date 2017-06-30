package com.github.ittalks.commons.sdk.google.oauth2.proxy;

import com.github.ittalks.commons.sdk.google.client.common.Constraints;
import com.github.ittalks.commons.sdk.google.client.common.config.Configration;
import com.github.ittalks.commons.sdk.google.client.extensions.jdo.JdoDataStoreFactory;
import com.github.ittalks.commons.sdk.google.client.extensions.jdo.JdoDataStoreFactoryProxy;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/2/20.
 */
public class AuthorizationCodeFlowProxy {

    public static final Logger logger = Logger.getLogger(AuthorizationCodeFlowProxy.class.getName());

    public static GoogleClientSecrets clientSecrets;
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the HTTP transport.
     */
    public static HttpTransport httpTransport;
    /**
     * Global instance of the DataStoreFactory. The best practice is to make it a single
     * globally shared instance across your application.
     */
    public static JdoDataStoreFactory dataStoreFactory;

    private static AuthorizationCodeFlow flow;

    /**
     * 初始化授权流
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public static AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {

        if (flow != null) {
            return flow;
        }

        // 加载client secrets
        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(AuthorizationCodeFlowProxy.class.getResourceAsStream(Configration.CLIENT_SECRETS)));

        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            logger.info("Enter Client ID and Secret from https://core.google.com/apis/console/ "
                    + "into resources/google/client_secrets.json");
            throw new RuntimeException("Enter Client ID and Secret from https://code.google.com/apis/console/ "
                    + "into resources/google/client_secrets.json");
        }

        //data store
//        dataStoreFactory = new FileDataStoreFactory(Configration.DATA_STORE_DIR);
        dataStoreFactory = JdoDataStoreFactoryProxy.getFactory();

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        // 设置authorization core flow
        flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, Constraints.SCOPES).setDataStoreFactory(
                dataStoreFactory).setAccessType("offline").setApprovalPrompt("force").build();

        return flow;
    }
}
