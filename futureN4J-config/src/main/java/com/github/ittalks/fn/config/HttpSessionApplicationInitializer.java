package com.github.ittalks.fn.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * Created by 刘春龙 on 2017/6/30.
 */
public class HttpSessionApplicationInitializer extends AbstractHttpSessionApplicationInitializer {

    public HttpSessionApplicationInitializer() {
        super(HttpSessionConfig.class);
    }
}
