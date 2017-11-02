package com.github.ittalks.commons.example.net.proxy;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 刘春龙 on 2017/11/1.
 * <p>
 * 以System.setProperty的方式设置http代理
 */
public class _SystemMain {


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

        // 开始请求
        try {

            String encodedLogin = new BASE64Encoder().encode((proxyUser + ":" + proxyPass).getBytes());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            URL u = new URL(url);
            HttpsURLConnection.setFollowRedirects(true);
            HttpsURLConnection conn = (HttpsURLConnection) u.openConnection(proxy);
            conn.setRequestProperty("Proxy-Authorization", "Basic " + encodedLogin);

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
}
