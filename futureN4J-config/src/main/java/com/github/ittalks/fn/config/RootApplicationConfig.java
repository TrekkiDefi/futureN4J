package com.github.ittalks.fn.config;

import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by 刘春龙 on 2017/7/26.
 */
@Configuration
@EnableRedisHttpSession
@PropertySource(value = {
        "classpath:/common/jdbc.properties",
        "classpath:/common/mongo.properties",
        "classpath:/common/redis.properties",
        "classpath:/t3/gclient.properties",
        "classpath:/webservice/server.properties"
}, ignoreResourceNotFound = true)
@ImportResource("classpath:/spring-ctx.xml")
@ComponentScan(basePackages = {"com.github.ittalks"},
        excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = {EnableWebMvc.class})}
)
public class RootApplicationConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
