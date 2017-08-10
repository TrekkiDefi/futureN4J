package com.github.ittalks.fn.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jdo.JdoTransactionManager;
import org.springframework.orm.jdo.LocalPersistenceManagerFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘春龙 on 2017/8/9.
 */
@Configuration
public class FnDataSourceConfig {

    @Value("${spring.datasource.driverClassName}")
    String driver;
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.initialSize}")
    int initialSize;
    @Value("${spring.datasource.maxActive}")
    int maxActive;
    @Value("${spring.datasource.maxWait}")
    long maxWait;
    @Value("${spring.datasource.minIdle}")
    int minIdle;
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    long timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    long minEvictableIdleTimeMillis;
    @Value("${spring.datasource.validationQuery}")
    String validationQuery;
    @Value("${spring.datasource.testWhileIdle}")
    boolean testWhileIdle;
    @Value("${spring.datasource.testOnBorrow}")
    boolean testOnBorrow;
    @Value("${spring.datasource.testOnReturn}")
    boolean testOnReturn;
    @Value("${spring.datasource.poolPreparedStatements}")
    boolean poolPreparedStatements;
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.connectionProperties}")
    String connectionProperties;
    @Value("${spring.datasource.filters}")
    String filters;

    //===============================================
    //阿里数据库连接池
    //===============================================
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setDefaultAutoCommit(false);
        // additional
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        druidDataSource.setConnectionProperties(connectionProperties);
        try {
            druidDataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

    //===============================================
    //jdo
    //===============================================
    @Bean(name = "pmf")
    public LocalPersistenceManagerFactoryBean jdoPersistenceManagerFactory() {
        LocalPersistenceManagerFactoryBean jdoPersistenceManagerFactory = new LocalPersistenceManagerFactoryBean();

        Map<String, Object> jdoPropertyMap = new HashMap<>();
        jdoPropertyMap.put("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
        jdoPropertyMap.put("javax.jdo.option.ConnectionFactory", dataSource());
        jdoPropertyMap.put("datanucleus.schema.autoCreateAll", true);
        jdoPersistenceManagerFactory.setJdoPropertyMap(jdoPropertyMap);
        return jdoPersistenceManagerFactory;
    }
    @Bean(name = "jdoTransactionManager")
    public JdoTransactionManager jdoTransactionManager() {
        JdoTransactionManager jdoTransactionManager = new JdoTransactionManager();
        jdoTransactionManager.setPersistenceManagerFactory(jdoPersistenceManagerFactory().getObject());
        return jdoTransactionManager;
    }

    //===============================================
    //jpa
    //===============================================

}
