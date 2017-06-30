package com.github.ittalks.commons.v2.retrofit.interfaces;

import java.util.Map;

/**
 * Created by 刘春龙 on 2017/6/7.
 */
@FunctionalInterface
public interface ParamsInterceptor {
    Map checkParams(Map params);
}
