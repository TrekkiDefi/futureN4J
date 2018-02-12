package com.github.ittalks.fn.config.web;

import org.springframework.core.Conventions;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import static org.springframework.web.servlet.FrameworkServlet.SERVLET_CONTEXT_PREFIX;

/**
 * Created by liuchunlong on 2018/2/11.
 * <p>
 * Servlet3.0+规范下，服务器启动后，Web容器（eg, Tomcat）会自动查找{@code META-INF/services/javax.servlet.ServletContainerInitializer}文件指定的
 * {@link javax.servlet.ServletContainerInitializer}接口的实现类来完成初始化（取代基于web.xml的配置方式）。
 * <p>
 * 其中{@link org.springframework.web.SpringServletContainerInitializer}就是spring提供的实现类，
 * 在SpringServletContainerInitializer中会查找实现了{@link org.springframework.web.WebApplicationInitializer}接口的类并将配置任务交给他们完成。
 * <p>
 * {@link SpringHttpSessionInitializer}也就是间接实现了{@link org.springframework.web.WebApplicationInitializer}这个接口
 */
public abstract class SpringHttpSessionInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * see {@link RootApplicationConfig},
     * <p>
     * as @{@link EnableSpringHttpSession} import {@link SpringHttpSessionConfiguration},
     * <p>
     * {@link SpringHttpSessionConfiguration} config the bean with the name of {@code springSessionRepositoryFilter}
     */
    protected static final String DEFAULT_FILTER_NAME = "springSessionRepositoryFilter";

    ////////////////////////////////////////
    //          for spring http session
    ////////////////////////////////////////

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        // register filters
        beforeSessionRepositoryFilter(servletContext);

        // for spring http session
        insertSessionRepositoryFilter(servletContext);
    }

    protected abstract void insertSessionRepositoryFilter(ServletContext servletContext);

    protected abstract boolean isAsyncSessionSupported();

    protected abstract EnumSet<DispatcherType> getSessionDispatcherTypes();

    ////////////////////////////////////////
    //          for spring http session
    ////////////////////////////////////////

    protected abstract void beforeSessionRepositoryFilter(ServletContext servletContext);

    ////////////////////////////////////////
    //          for extension
    ////////////////////////////////////////

    /**
     * get dispatcherServlet's attribute in servletContext
     *
     * @return
     */
    protected String getWebApplicationContextAttribute() {
        String dispatcherServletName = getDispatcherWebApplicationContextSuffix();
        if (dispatcherServletName == null) {
            return null;
        }
        return SERVLET_CONTEXT_PREFIX + dispatcherServletName;
    }

    protected String getDispatcherWebApplicationContextSuffix() {
        return DEFAULT_SERVLET_NAME;
    }

    protected void registerFilters(ServletContext servletContext,
                                   boolean insertBeforeOtherFilters, Filter... filters) {
        Assert.notEmpty(filters, "filters cannot be null or empty");
        for (Filter filter : filters) {
            if (filter == null) {
                throw new IllegalArgumentException(
                        "filters cannot contain null values. Got "
                                + Arrays.asList(filters));
            }
            // 生成filter的名称
            String filterName = Conventions.getVariableName(filter);
            registerFilter(servletContext, insertBeforeOtherFilters, filterName, filter);
        }
    }

    protected void registerFilter(ServletContext servletContext,
                                  boolean insertBeforeOtherFilters, String filterName, Filter filter) {
        FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
        if (registration == null) {
            throw new IllegalStateException(
                    "Duplicate Filter registration for '" + filterName
                            + "'. Check to ensure the Filter is only configured once.");
        }
        registration.setAsyncSupported(isAsyncSessionSupported());
        EnumSet<DispatcherType> dispatcherTypes = getSessionDispatcherTypes();
        registration.addMappingForUrlPatterns(dispatcherTypes, !insertBeforeOtherFilters,
                "/*");
    }

    protected final void insertFilters(ServletContext servletContext, Filter... filters) {
        registerFilters(servletContext, true, filters);
    }

    protected final void appendFilters(ServletContext servletContext, Filter... filters) {
        registerFilters(servletContext, false, filters);
    }

    protected void registerFilter(ServletContext servletContext,
                                  boolean insertBeforeOtherFilters, String filterName, Filter filter,
                                  Map<String, String> initParams,
                                  String... urlPatterns) {
        Assert.notNull(initParams, "Init Params cant null.");
        FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
        if (registration == null) {
            throw new IllegalStateException(
                    "Duplicate Filter registration for '" + filterName
                            + "'. Check to ensure the Filter is only configured once.");
        }
        registration.setInitParameters(initParams);
        registration.setAsyncSupported(isAsyncSessionSupported());
        EnumSet<DispatcherType> dispatcherTypes = getSessionDispatcherTypes();
        registration.addMappingForUrlPatterns(dispatcherTypes, !insertBeforeOtherFilters,
                urlPatterns);
    }

    protected void registerServlet(ServletContext servletContext,
                                   String servletName, Servlet servlet,
                                   Map<String, String> initParams,
                                   String... urlPatterns) {
        Assert.notNull(initParams, "Init Params cant null.");
        ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, servlet);
        if (registration == null) {
            throw new IllegalStateException(
                    "Duplicate Servlet registration for '" + servletName
                            + "'. Check to ensure the Filter is only configured once.");
        }
        registration.setInitParameters(initParams);
        registration.setAsyncSupported(isAsyncSessionSupported());
        registration.addMapping(urlPatterns);
    }
}
