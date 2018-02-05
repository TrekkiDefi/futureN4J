package com.github.ittalks.fn.core;

import com.github.ittalks.fn.config.RootApplicationConfig;
import com.github.ittalks.fn.config.WebApplicationConfig;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by 刘春龙 on 2017/8/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)

/**
 * If you do not add the @WebAppConfiguration annotation, it will cause the following error:
 *      Caused by: java.lang.IllegalArgumentException: A ServletContext is required to configure default servlet handling
 */
@WebAppConfiguration

@ContextConfiguration(
        classes = {
                RootApplicationConfig.class,
                WebApplicationConfig.class
        })
public class FnAppTest {

    protected static final Logger logger = LoggerFactory.getLogger(FnAppTest.class);
}
