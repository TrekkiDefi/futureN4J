package com.github.ittalks.commons.retrofit.interfaces;

import java.util.Map;

/**
 * Created by liuchunlong on 2017/7/15.
 */
@FunctionalInterface
public interface ParamsInterceptor {
    Map checkParams(Map params);
}
