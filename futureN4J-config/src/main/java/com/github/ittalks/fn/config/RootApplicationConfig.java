package com.github.ittalks.fn.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by 刘春龙 on 2017/7/26.
 */
@Configuration

// enable spring redis session
@EnableRedisHttpSession

// enable transaction management
@EnableTransactionManagement

// enable spring data jpa
//@EnableJpaRepositories(basePackages = "com.github.ittalks.fn.core.jpa")

@PropertySource(value = {
        "classpath:/common/druid.properties",
        "classpath:/common/mongo.properties",
        "classpath:/common/redis.properties",
        "classpath:/third/gclient.properties",
        "classpath:/webservice/server.properties"
}, ignoreResourceNotFound = true)
@ImportResource("classpath:/spring-ctx.xml")
@Import(value = {
        FnWebSocketConfig.class,
        FnExecutorConfigurer.class,
        FnDataSourceConfig.class
})
@ComponentScan(basePackages = {"com.github.ittalks"},
        excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = {EnableWebMvc.class})}
)
public class RootApplicationConfig implements EnvironmentAware {

    Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
