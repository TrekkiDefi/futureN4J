package com.github.ittalks.commons.v2.retrofit.utils;

import com.github.ittalks.commons.v2.retrofit.cache.CacheProvider;
import com.github.ittalks.commons.v2.retrofit.interceptor.CacheInterceptor;
import com.github.ittalks.commons.v2.retrofit.interceptor.RetryAndChangeIpInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuchunlong on 2017/7/17.
 */
public class OkhttpProvider {

    public static OkHttpClient okHttpClient(final String cachePath, List<String> servers, boolean debug) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RetryAndChangeIpInterceptor(servers))
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(new CacheProvider(cachePath).provideCache())
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

    public static OkHttpClient okHttpClient(final String cachePath, List<String> servers) {
        return okHttpClient(cachePath, servers, false);
    }
}
