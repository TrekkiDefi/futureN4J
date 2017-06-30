package com.github.ittalks.commons.v2.retrofit.cache;

import okhttp3.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by 刘春龙 on 2017/6/7.
 */
public class CacheProvider {
    private static Logger logger = LoggerFactory.getLogger(CacheProvider.class);

    String cachePath;

    public CacheProvider(String cachePath) {
        this.cachePath = cachePath;
    }

    public Cache provideCache() {
        File cacheFile = new File(this.cachePath, "caches");
        return new Cache(cacheFile, 50 * 1024 * 1024);
    }
}
