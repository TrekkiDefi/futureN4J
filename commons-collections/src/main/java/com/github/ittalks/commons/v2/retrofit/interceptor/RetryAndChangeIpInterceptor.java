package com.github.ittalks.commons.v2.retrofit.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 刘春龙 on 2017/6/7.
 */
public class RetryAndChangeIpInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(RetryAndChangeIpInterceptor.class);

    private static final String urlReg = "^(https?://)?((?:\\S+?)|(?:(?:\\d{1,3}\\.){3}\\d{1,3})(?::\\d+)?)(/\\S*)?$";

    final int retryCount;
    List<String> sERVERS;

    public RetryAndChangeIpInterceptor(List<String> sERVERS) {
        this.retryCount = 0;
        this.sERVERS = sERVERS;
    }

    public RetryAndChangeIpInterceptor(int retryCount, List<String> sERVERS) {
        this.retryCount = retryCount;
        this.sERVERS = sERVERS;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // try the request
        Response response = doRequest(chain, request);

        int retryTimes = 0;
        String url = request.url().toString();

        while (response == null && retryTimes < this.retryCount) {
            // request fail
            if (retryTimes < this.sERVERS.size()) {
                url = switchServer(url, retryTimes);
                request = request.newBuilder().url(url).build();
            }
            logger.info("intercept", "Request is not successful，do retry - " + retryTimes + ".");
            retryTimes++;
            // retry the request
            response = doRequest(chain, request);
        }
        if (response == null) {
            throw new IOException();
        }
        return response;
    }

    /**
     * 执行请求
     *
     * @param chain
     * @param request
     * @return
     */
    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 切换服务器
     *
     * @param url
     * @return
     */
    private String switchServer(String url, int index) {
        if (this.sERVERS == null || this.sERVERS.size() == 0) {
            return url;
        }

        Pattern urlPattern = Pattern.compile(urlReg);

        Matcher baseUrlMatcher = urlPattern.matcher(url);
        Matcher serverMatcher = urlPattern.matcher(this.sERVERS.get(index));
        if (baseUrlMatcher.matches() && serverMatcher.matches()) {
            String originProtocol = baseUrlMatcher.group(1);
            String originIpPort = baseUrlMatcher.group(2);

            String protocol = serverMatcher.group(1);
            String ipPort = serverMatcher.group(2);

            url = url.replace(originProtocol, protocol).replace(originIpPort, ipPort);
        }
        return url;
    }

    public static void main(String[] args) {
        String baseUrlReg = "^(https?://)?((?:\\S+?)|(?:(?:\\d{1,3}\\.){3}\\d{1,3})(?::\\d+)?)(/\\S*)?$";
        Pattern baseUrlPattern = Pattern.compile(baseUrlReg);
        Matcher keyMatcher = baseUrlPattern.matcher("http://www.baidu.com");
        /*
        http://
        127.0.0.1:8080
        /admin/index/
         */
//        Matcher keyMatcher = baseUrlPattern.matcher("https://baidu.com:8080/admin/index/");
        /*
        https://
        baidu.com:8080
        /admin/index/
         */
//        Matcher keyMatcher = baseUrlPattern.matcher("www.baidu.com/admin/index/");
        /*
        null
        www.baidu.com
        /admin/index/
         */
        System.out.println(keyMatcher.matches());
        System.out.println(keyMatcher.group(1));
        System.out.println(keyMatcher.group(2));
        System.out.println(keyMatcher.group(3));
    }
}
