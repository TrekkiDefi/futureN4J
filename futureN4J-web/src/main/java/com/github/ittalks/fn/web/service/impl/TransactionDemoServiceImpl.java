package com.github.ittalks.fn.web.service.impl;

import com.github.ittalks.fn.annotation.JdbcTransactional;
import com.github.ittalks.fn.core.jdo.pojo.PersonInfo;
import com.github.ittalks.fn.web.service.TransactionDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by 刘春龙 on 2018/3/6.
 */
@Service
public class TransactionDemoServiceImpl implements TransactionDemoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    18:24:34.899 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Creating new transaction with name [com.github.ittalks.fn.web.service.impl.TransactionDemoServiceImpl.demo]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT; ''
//    18:24:34.901 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Acquired Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@1182d1df] for JDBC transaction
//    18:24:34.914 [main] DEBUG o.s.jdbc.core.JdbcTemplate - Executing SQL query [select * from person]
//    18:24:34.931 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Initiating transaction commit
//    18:24:34.931 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Committing JDBC transaction on Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@1182d1df]
//    18:24:34.933 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Releasing JDBC Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@1182d1df] after transaction
//    18:24:34.933 [main] DEBUG o.s.jdbc.datasource.DataSourceUtils - Returning JDBC Connection to DataSource
//    18:24:35.295 [main] INFO  c.github.ittalks.fn.TransactionTest - [{"age":11,"email":"631521383@qq.com","id":1,"name":"小红"},{"age":23,"email":"1325836985@qq.com","id":2,"name":"小明"}]
//    @Transactional

//    18:30:20.677 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Creating new transaction with name [com.github.ittalks.fn.web.service.impl.TransactionDemoServiceImpl.demo]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT; 'jdbcTransactionManager'
//    18:30:20.678 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Acquired Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@5a39e554] for JDBC transaction
//    18:30:20.695 [main] DEBUG o.s.jdbc.core.JdbcTemplate - Executing SQL query [select * from person]
//    18:30:20.737 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Initiating transaction commit
//    18:30:20.738 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Committing JDBC transaction on Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@5a39e554]
//    18:30:20.739 [main] DEBUG o.s.j.d.DataSourceTransactionManager - Releasing JDBC Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@5a39e554] after transaction
//    18:30:20.741 [main] DEBUG o.s.jdbc.datasource.DataSourceUtils - Returning JDBC Connection to DataSource
//    18:30:21.091 [main] INFO  c.github.ittalks.fn.TransactionTest - [{"age":11,"email":"631521383@qq.com","id":1,"name":"小红"},{"age":23,"email":"1325836985@qq.com","id":2,"name":"小明"}]
    @JdbcTransactional
    @Override
    public List<PersonInfo> demo() {
        return jdbcTemplate.query("select * from person", (rs, rowNum) -> {
            PersonInfo personInfo = new PersonInfo();
            personInfo.setId(rs.getInt("id"));
            personInfo.setName(rs.getString("name"));
            personInfo.setAge(rs.getInt("age"));
            personInfo.setEmail(rs.getString("email"));
            return personInfo;
        });
    }
}
