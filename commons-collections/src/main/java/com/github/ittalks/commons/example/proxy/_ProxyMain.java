package com.github.ittalks.commons.example.proxy;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.helpers.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 刘春龙 on 2017/11/1.
 * <p>
 * 使用Proxy代理
 */
public class _ProxyMain {

    private static String proxyHost = "";
    private static int proxyPort = 80;
    private static String proxyUser = "";
    private static String proxyPass = "";

    public static void main(String[] args) {
        String url = "https://www.google.com/";
        String content = doProxy(url);
        System.out.println("Result :===================\n "
                + content);
    }

    private static String doProxy(String url) {
        // 设置系统变量
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", "" + proxyPort);
        // 针对https也开启代理
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", "" + proxyPort);
        // 设置默认校验器
        BasicAuthenticator auth = new BasicAuthenticator(proxyUser, proxyPass);
        Authenticator.setDefault(auth);

        //开始请求
        try {
            URL u = new URL(url);
            HttpsURLConnection.setFollowRedirects(true);
            HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();

            String encoding = conn.getContentEncoding();
            if (StringUtils.isEmpty(encoding)) {
                encoding = "UTF-8";
            }

            InputStream is = conn.getInputStream();
            return IOUtils.toString(is, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static class BasicAuthenticator extends Authenticator {

        private String userName;
        private String password;

        public BasicAuthenticator(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        /**
         * Called when password authorization is needed.  Subclasses should
         * override the default implementation, which returns null.
         *
         * @return The PasswordAuthentication collected from the
         * user, or null if none is provided.
         */
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password.toCharArray());
        }
    }
}
