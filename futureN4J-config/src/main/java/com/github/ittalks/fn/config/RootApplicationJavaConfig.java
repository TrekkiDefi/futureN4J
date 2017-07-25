package com.github.ittalks.fn.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by 刘春龙 on 2017/6/30.
 */
@Configuration
@PropertySource(value = {
        "classpath:redis/*.properties",
        "classpath:common/*.properties",
        "classpath:google/*.properties",
        "classpath:webservice/*.properties"
}, ignoreResourceNotFound = true)
@ImportResource("classpath:/spring-ctx.xml")
@EnableRedisHttpSession
public class RootApplicationJavaConfig {
}
