package com.github.ittalks.commons.v2.retrofit.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by 刘春龙 on 2017/7/19.
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 执行请求
        Request request = chain.request();
        Response originResponse = chain.proceed(request);

        // 获取服务器返回的Cache-Control
        String serverCache = originResponse.header("Cache-Control");
        if (StringUtils.isEmpty(serverCache)) {// 服务器未返回Cache-Control
            // 获取请求中的Cache-Control
            String cacheControl = request.cacheControl().toString();
            if (StringUtils.isEmpty(cacheControl)) {// 请求中未设置CacheControl
                // 获取请求头Cache-Time，根据Cache-Time设置缓存策略
                String cacheTime = request.header("Cache-Time");
                if (StringUtils.isEmpty(cacheTime)) {
                    return originResponse;
                } else {
                    /*
                        Pragma：
                            作用： 防止页面被缓存， 在HTTP/1.1版本中，它和Cache-Control:no-cache作用一模一样。
                            Pargma只有一个用法， 例如： Pragma: no-cache。
                            注意: 在HTTP/1.0版本中，只实现了Pragema:no-cache, 没有实现Cache-Control。
                     */
                    return originResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + cacheTime)
                            .build();
                }
            } else {// 请求中设置了Cache-Control，则使用请求中设置的Cache-Control设置缓存策略
                return originResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();
            }
        } else {// 服务端设置相应的缓存策略，那么遵从服务端的不做修改
            return originResponse;
        }
    }
}
