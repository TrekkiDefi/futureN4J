package com.github.ittalks.fn.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jdo.JdoTransactionManager;
import org.springframework.orm.jdo.LocalPersistenceManagerFactoryBean;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘春龙 on 2017/8/9.
 */
@Configuration
/**
 * 启用Spring的注释驱动事务管理功能
 *
 * @EnableTransactionManagement 按类型查找容器中任何PlatformTransactionManager bean
 *
 * 如果同一个容器中存在两个PlatformTransactionManager bean，
 * 实现TransactionManagementConfigurer回调接口为@EnableTransactionManagement指定确切事务管理器bean
 *
 * 此外，如果存在多个数据源，可以使用分布式事务进行管理
 * 项目进行读写分离及分库分表，在一个业务中，在一个事务中处理时候将切换多个数据源，需要保证同一事务多个数据源数据的一致性。可以使用atomikos来实现。
 *
 * 一般不会出现同时使用多个ORM框架的情况（如Hibernate+JPA+JDO）
 *
 * 如果配置多个PlatformTransactionManager bean，会报如下错误：
 * org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type [org.springframework.transaction.PlatformTransactionManager] is defined: expected single matching bean but found 3: jpaTransactionManager,jdoTransactionManager,jdbcTransactionManager
 *
 * 下面有两种处理方式：
 * 1. 通过@Primary指定候选PlatformTransactionManager bean
 * 2. 通过@Transactional的transactionManager属性指定事务管理器，参见 com.github.ittalks.fn.annotation.JdbcTransactional
 */
@EnableTransactionManagement
/**
 * Enable spring data JPA
 */
@EnableJpaRepositories(
        basePackages = "com.github.ittalks.fn.core",
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        transactionManagerRef = "jpaTransactionManager"
)
public class FnDataSourceConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

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
    // 阿里数据库连接池
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

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    @Primary
    public PlatformTransactionManager jdbcTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    //===============================================
    // JDO
    //===============================================
    @Bean
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
    public PlatformTransactionManager jdoTransactionManager() {
        JdoTransactionManager transactionManager = new JdoTransactionManager();
        transactionManager.setPersistenceManagerFactory(jdoPersistenceManagerFactory().getObject());
        return transactionManager;
    }

    //===============================================
    // JPA 自定义配置
    //===============================================
    @Bean
    public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.valueOf(environment.getProperty("jpa.database")));
        jpaVendorAdapter.setGenerateDdl(Boolean.parseBoolean(environment.getProperty("jpa.generateDdl")));
        jpaVendorAdapter.setShowSql(Boolean.parseBoolean(environment.getProperty("jpa.showSql")));

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("com.github.ittalks.fn.core");
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        return emf;
    }

    @Bean
    public PlatformTransactionManager jpaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(jpaEntityManagerFactory().getObject());
        // 设置用于此事务管理器的JPA方言。方言对象可用于检索底层的JDBC连接，从而允许将JPA事务公开为JDBC事务。
        transactionManager.setJpaDialect(hibernateJpaDialect());
        return transactionManager;
    }

    /**
     * JPA方言
     * <p>
     * 以同时支持JPA/JDBC访问，除了HibernateJpaDialect，其他的还有EclipseLinkJpaDialect、OpenJpaDialect
     * <p>
     * 默认的DefaultJpaDialect不支持，因为其getJdbcConnection()方法返回null
     *
     * @return
     */
    @Bean
    public JpaDialect hibernateJpaDialect() {
        return new HibernateJpaDialect();
    }
}
