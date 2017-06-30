package com.github.ittalks.commons.v2.retrofit.utils;

import com.github.ittalks.commons.v2.retrofit.cache.CacheProvider;
import com.github.ittalks.commons.v2.retrofit.interceptor.CacheInterceptor;
import com.github.ittalks.commons.v2.retrofit.interceptor.RetryAndChangeIpInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 刘春龙 on 2017/6/7.
 */
public class OkhttpProvider {

    public static OkHttpClient okHttpClient(final String cache, List<String> servers, boolean debug) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RetryAndChangeIpInterceptor(servers))
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(new CacheProvider(cache).provideCache())
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .build();
        if (debug) {//printf logs while debug
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = client.newBuilder().addInterceptor(logging).build();
        }
        return client;
    }

    public static OkHttpClient okHttpClient(final String cache, List<String> servers) {
        return okHttpClient(cache, servers, false);
    }
}
