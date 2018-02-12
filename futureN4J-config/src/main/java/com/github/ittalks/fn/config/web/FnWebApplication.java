package com.github.ittalks.fn.config.web;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.core.Conventions;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by liuchunlong on 2018/2/12.
 */
public class FnWebApplication extends SpringHttpSessionInitializer {

    private static Properties properties = new Properties();

    static {
        InputStream inputStream = FnWebApplication.class.getClassLoader().getResourceAsStream("common/druid.properties");
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 相对于web.xml，用于配置{@link ContextLoaderListener}的{@code <context-param>}中的{@code contextConfigLocation}参数
     * <p>
     * 只是这里返回的是一个JavaConfig的Class数组
     *
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootApplicationConfig.class};
    }

    /**
     * 相对于web.xml，用于配置{@link DispatcherServlet}的{@code <init-param>}中的{@code contextConfigLocation}参数
     * <p>
     * 只是这里返回的是一个JavaConfig的Class数组
     *
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebApplicationConfig.class};
    }

    /**
     * 指定{@link DispatcherServlet}的servlet映射 - 例如"/", "/app"等
     *
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    ////////////////////////////////////////
    //          for spring http session
    ////////////////////////////////////////

    protected void insertSessionRepositoryFilter(ServletContext servletContext) {
        String filterName = DEFAULT_FILTER_NAME;
        DelegatingFilterProxy springSessionRepositoryFilter = new DelegatingFilterProxy(filterName);
        String contextAttribute = getWebApplicationContextAttribute();
        if (contextAttribute != null) {
            springSessionRepositoryFilter.setContextAttribute(contextAttribute);
        }
        registerFilter(servletContext, true, filterName, springSessionRepositoryFilter);
    }

    protected boolean isAsyncSessionSupported() {
        return true;
    }

    protected EnumSet<DispatcherType> getSessionDispatcherTypes() {
        return EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR,
                DispatcherType.ASYNC);
    }

    ////////////////////////////////////////
    //          for spring http session
    ////////////////////////////////////////

    protected void beforeSessionRepositoryFilter(ServletContext servletContext) {

        /**
         * 阿里数据库连接池，监控后台配置
         */
        Servlet statViewServlet = new StatViewServlet();
        Map<String, String> svsInitParams = new HashMap<>();
        svsInitParams.put("allow", properties.getProperty("druid.manager.allow"));
        svsInitParams.put("deny", properties.getProperty("druid.manager.deny"));
        svsInitParams.put("loginUsername", properties.getProperty("druid.manager.loginUsername"));
        svsInitParams.put("loginPassword", properties.getProperty("druid.manager.loginPassword"));
        String statViewServletName = Conventions.getVariableName(statViewServlet);
        registerServlet(servletContext, statViewServletName, statViewServlet, svsInitParams, "/druid/*");

        /**
         * 阿里数据库连接池，启用 Web 监控统计功能
         */
        Filter webStatFilter = new WebStatFilter();
        Map<String, String> wsfInitParams = new HashMap<>();
        wsfInitParams.put("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        String webStatFilterName = Conventions.getVariableName(webStatFilter);
        registerFilter(servletContext, false, webStatFilterName, webStatFilter, wsfInitParams, "/*");
    }
}
