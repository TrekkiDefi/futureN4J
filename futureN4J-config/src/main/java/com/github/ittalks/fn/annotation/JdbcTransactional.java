package com.github.ittalks.fn.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Created by 刘春龙 on 2018/3/6.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional("jdbcTransactionManager")
public @interface JdbcTransactional {
}
