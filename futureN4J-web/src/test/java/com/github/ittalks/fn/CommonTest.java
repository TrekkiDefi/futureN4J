package com.github.ittalks.fn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Conventions;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;

/**
 * Created by 刘春龙 on 2017/7/27.
 */
public class CommonTest {

    private static final Logger logger = LoggerFactory.getLogger(CommonTest.class.getName());

    public static void main(String[] args) {
        String filterName = "filter";
        Filter filter = new DelegatingFilterProxy(filterName);
        logger.info(Conventions.getVariableName(filter));// delegatingFilterProxy
    }
}
