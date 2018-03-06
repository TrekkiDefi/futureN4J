package com.github.ittalks.fn;

import com.alibaba.fastjson.JSON;
import com.github.ittalks.fn.core.FnAppTest;
import com.github.ittalks.fn.web.service.TransactionDemoService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 刘春龙 on 2017/8/5.
 */
public class TransactionTest extends FnAppTest {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTest.class);

    @Autowired
    private TransactionDemoService transactionDemoService;

    @Test
    public void transactionDemoTest() {
        logger.info(JSON.toJSONString(transactionDemoService.demo()));
    }
}
