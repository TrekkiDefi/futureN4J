package com.github.ittalks.commons.retrofit.cache;

import okhttp3.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by liuchunlong on 2017/7/17.
 */
public class CacheProvider {

    private static Logger logger = LoggerFactory.getLogger(CacheProvider.class);

    String cachePath;

    public CacheProvider(String cachePath) {
        this.cachePath = cachePath;
    }

    public Cache provideCache() {
        File cacheFile = new File(this.cachePath, "retrofit" + File.separator + "caches");
        return new Cache(cacheFile, 50 * 1024 * 1024);
    }

}
