package com.github.ittalks.commons.v2.retrofit.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by 刘春龙 on 2017/6/7.
 */
public class CacheInterceptor implements Interceptor {

    //TODO 未测试效果
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originResponse = chain.proceed(request);

        //获取服务器返回的Cache-Control
        String serverCache = originResponse.header("Cache-Control");
        if (StringUtils.isEmpty(serverCache)) {//服务器未返回Cache-Control
            //获取请求中的Cache-Control
            String cacheControl = request.cacheControl().toString();
            if (StringUtils.isEmpty(cacheControl)) {//请求中未设置CacheControl
                //获取请求头Cache-Time，根据Cache-Time设置缓存策略
                String cache = request.header("Cache-Time");
                if (StringUtils.isEmpty(cache)) {
                    return originResponse;
                } else {
                    Response response = originResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + cache)
                            .build();
                    return response;
                }
            } else {//请求中设置了Cache-Control，则使用请求中设置的Cache-Control设置缓存策略
                Response response = originResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();
                return response;
            }
        } else {//服务端设置相应的缓存策略，那么遵从服务端的不做修改
            return originResponse;
        }
    }
}
