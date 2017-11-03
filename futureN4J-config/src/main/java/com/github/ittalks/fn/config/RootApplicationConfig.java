package com.github.ittalks.fn.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.ittalks.commons.redis.queue.config.ConfigManager;
import com.github.ittalks.commons.sdk.google.calendar.task.DTQueueConsumer;
import com.github.ittalks.commons.sdk.google.calendar.task.MSQueueConsumer;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.IOException;
import java.sql.SQLException;
import java.util.EventListener;
import java.util.Properties;

/**
 * Created by 刘春龙 on 2017/7/26.
 */
@Configuration
// enable spring redis session
@EnableRedisHttpSession
@PropertySource(value = {
        "classpath:/common/druid.properties",
        "classpath:/common/mongo.properties",
        "classpath:/common/redis.properties",
        "classpath:/common/queue.properties",
        "classpath:/thirdparty/gclient.properties",
        "classpath:/webservice/server.properties"
}, ignoreResourceNotFound = true)
@ImportResource("classpath:/spring-ctx.xml")
@Import(value = {
        FnWebSocketConfig.class,
        FnExecutorConfigurer.class,
        FnDataSourceConfig.class,
        FnSchedulerConfig.class
})
@ComponentScan(basePackages = {"com.github.ittalks"},
        excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = {EnableWebMvc.class})}
)
public class RootApplicationConfig {

    @Autowired
    Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ConfigManager queueConfigManager() throws IOException {
        Properties redisConn = new Properties();
        redisConn.setProperty("redis.pool.maxTotal", environment.getProperty("redis.pool.maxTotal"));
        redisConn.setProperty("redis.pool.maxIdle", environment.getProperty("redis.pool.maxIdle"));
        redisConn.setProperty("redis.pool.maxWaitMillis", environment.getProperty("redis.pool.maxWaitMillis"));
        redisConn.setProperty("redis.connectMode", environment.getProperty("redis.connectMode"));
        redisConn.setProperty("redis.pool.host", environment.getProperty("redis.host"));
        redisConn.setProperty("redis.pool.port", environment.getProperty("redis.port"));
        redisConn.setProperty("redis.hostPort", environment.getProperty("redis.hostPort"));

        String queues = environment.getProperty("redis.queues");
        int queueRepeat = Integer.parseInt(environment.getProperty("redis.queue.repeat"));
        return new ConfigManager(redisConn, queues, queueRepeat);
    }
}
