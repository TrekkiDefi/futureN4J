package com.github.ittalks.commons.retrofit.interceptor;

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
 * Created by liuchunlong on 2017/7/17.
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

        while (response == null && retryTimes < this.retryCount) {// request fail
            url = switchServer(url, retryTimes);
            request = request.newBuilder().url(url).build();

            logger.info("intercept", "request is not successful，retry " + retryTimes + " times.");
            retryTimes++;
            // retry the request
            response = doRequest(chain, request);
        }

        if (response == null) {
            throw new IOException();
        }
        return response;
    }

    private Response doRequest(Chain chain, Request request) {
        Response response = null;

        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private String switchServer(String url, int retryTimes) {
        if (this.sERVERS == null || this.sERVERS.size() == 0) {
            return url;
        }
        Pattern urlPattern = Pattern.compile(urlReg);

        Matcher originUrlMatcher = urlPattern.matcher(url);
        Matcher retryServerMatcher = urlPattern.matcher(this.sERVERS.get(retryTimes % this.sERVERS.size()));

        if (originUrlMatcher.matches() && retryServerMatcher.matches()) {
            String originProtocol = originUrlMatcher.group(1);
            String originIpPort = originUrlMatcher.group(2);

            String protocol = retryServerMatcher.group(1);
            String ipPort = retryServerMatcher.group(2);

            url = url.replace(originProtocol, protocol).replace(originIpPort, ipPort);
        }
        return url;
    }
}
