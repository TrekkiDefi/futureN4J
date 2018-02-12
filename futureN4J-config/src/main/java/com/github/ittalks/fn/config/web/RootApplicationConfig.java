package com.github.ittalks.fn.config.web;

import com.github.ittalks.commons.sdk.google.calendar.enums.Queue;
import com.github.ittalks.fn.config.FnDataSourceConfig;
import com.github.ittalks.fn.config.FnExecutorConfigurer;
import com.kingsoft.wps.mail.queue.KMQueueManager;
import com.kingsoft.wps.mail.queue.config.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by 刘春龙 on 2017/7/26.
 */
@Configuration
@EnableRedisHttpSession // enable spring redis session
@PropertySource(value = {
        "classpath:/common/druid.properties",
        "classpath:/common/mongo.properties",
        "classpath:/common/redis.properties",
        "classpath:/common/rabbitmq.properties",
        "classpath:/thirdparty/gclient.properties",
        "classpath:/webservice/server.properties"
}, ignoreResourceNotFound = true)
@ImportResource("classpath:/spring-ctx.xml")
@Import(value = {
        FnExecutorConfigurer.class,
        FnDataSourceConfig.class
})
@ComponentScan(basePackages = {"com.github.ittalks"},
        excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = {EnableWebMvc.class})}
)
public class RootApplicationConfig {

    private final Environment environment;

    @Autowired
    public RootApplicationConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public KMQueueManager calKMQueueManager(@Value("${redis.host}") String host,
                                            @Value("${redis.port}") int port,
                                            @Value("${redis.pool.maxWaitMillis}") Long poolMaxWaitMillis,
                                            @Value("${redis.pool.maxTotal}") Integer poolMaxTotal,
                                            @Value("${redis.pool.maxIdle}") Integer poolMaxIdle) {
        KMQueueManager kmQueueManager = new KMQueueManager.Builder(host, port, Queue.msg_queue.getName(), Queue.data_queue.getName())
                .setMaxWaitMillis(poolMaxWaitMillis)
                .setMaxTotal(poolMaxTotal)
                .setMaxIdle(poolMaxIdle)
                .setAliveTimeout(Constant.ALIVE_TIMEOUT)
                .build();
        // 初始化队列
        kmQueueManager.init();
        return kmQueueManager;
    }
}
