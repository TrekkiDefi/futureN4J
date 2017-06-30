package com.github.ittalks.commons.sdk.google.client.common.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by 刘春龙 on 2017/2/17.
 */
public class Configration {

    public static String DATA_STORE_DIR;
    public static String CLIENT_SECRETS;
    public static String OAUTH2_REDIRECT_URL;
    public static String OAUTH2_CALLBACK;

    public void setDataStoreDir(String dataStoreDir) {
        DATA_STORE_DIR = dataStoreDir;
    }

    public void setClientSecrets(String clientSecrets) {
        CLIENT_SECRETS = clientSecrets;
    }

    public void setOauth2RedirectUrl(String oauth2RedirectUrl) {
        OAUTH2_REDIRECT_URL = oauth2RedirectUrl;
    }

    public void setOauth2Callback(String oauth2Callback) {
        OAUTH2_CALLBACK = oauth2Callback;
    }
}