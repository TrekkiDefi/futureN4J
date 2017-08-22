package com.github.ittalks.fn.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.github.ittalks.commons.sdk.google.calendar.task.DTQueueConsumer;
import com.github.ittalks.commons.sdk.google.calendar.task.MSQueueConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Conventions;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.util.Assert;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by liuchunlong on 2017/8/5.
 * <p>
 * <ul>
 * <li>1. Add springSessionRepositoryFilter, for Spring http session.
 * <li>2. Filter、Listener、Servlet Registration
 * </ul>
 * <p>
 * see {@link AbstractHttpSessionApplicationInitializer}
 */
@Order(Integer.MAX_VALUE)
public class FnSpringHttpSessionInitializer implements WebApplicationInitializer {

    /**
     * The default name for Spring Session's repository filter.
     */
    public static final String DEFAULT_FILTER_NAME = "springSessionRepositoryFilter";

    private static final String SERVLET_CONTEXT_PREFIX = "org.springframework.web.servlet.FrameworkServlet.CONTEXT.";


    /**
     * Java Config
     */
    private final Class<?>[] configurationClasses;


    static Properties properties = new Properties();
    static {
        InputStream inputStream = FnSpringHttpSessionInitializer.class.getClassLoader().getResourceAsStream("common/druid.properties");
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 构造函数，初始化{@code configurationClasses}为Null，假设Spring Session配置是通过其他方式加载的。<br/>
     * <p>
     * 例如，用户可以实现{@link AbstractContextLoaderInitializer}接口实例化一个{@link ContextLoaderListener}。
     *
     * @see ContextLoaderListener
     */
    public FnSpringHttpSessionInitializer() {
        this.configurationClasses = null;
    }

    /**
     * 构造函数，它将使用指定的Java Config实例化{@link ContextLoaderListener}。
     *
     * @param configurationClasses 用于配置应用上下文的 {@code @Configuration}类
     */
    public FnSpringHttpSessionInitializer(Class<?>... configurationClasses) {
        this.configurationClasses = configurationClasses;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        beforeSessionRepositoryFilter(servletContext);
        if (this.configurationClasses != null) {
            AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
            rootAppContext.register(this.configurationClasses);
            servletContext.addListener(new ContextLoaderListener(rootAppContext));
        }
        insertSessionRepositoryFilter(servletContext);
        afterSessionRepositoryFilter(servletContext);
    }

    /**
     * 注册springSessionRepositoryFilter。
     *
     * @param servletContext {@link ServletContext}
     */
    private void insertSessionRepositoryFilter(ServletContext servletContext) {
        String filterName = DEFAULT_FILTER_NAME;
        DelegatingFilterProxy springSessionRepositoryFilter = new DelegatingFilterProxy(filterName);
        String contextAttribute = getWebApplicationContextAttribute();

        if (contextAttribute != null) {
            springSessionRepositoryFilter.setContextAttribute(contextAttribute);
        }
        registerFilter(servletContext, true, filterName, springSessionRepositoryFilter);
    }

    /**
     * 使用{@link #isAsyncSessionSupported()}和{@link #getSessionDispatcherTypes()}注册提供的过滤器。
     *
     * @param servletContext           servlet上下文
     * @param insertBeforeOtherFilters 此过滤器是否应该在其他{@link Filter}之前插入
     * @param filterName               过滤器名称
     * @param filter                   过滤器
     */
    private void registerFilter(ServletContext servletContext,
                                boolean insertBeforeOtherFilters, String filterName, Filter filter) {
        FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
        if (registration == null) {
            throw new IllegalStateException(
                    "Duplicate Filter registration for '" + filterName
                            + "'. Check to ensure the Filter is only configured once.");
        }
        registration.setAsyncSupported(isAsyncSessionSupported());
        EnumSet<DispatcherType> dispatcherTypes = getSessionDispatcherTypes();

        /**
         * dispatcherTypes - filter mapping 的 dispatcher types，如果要使用默认的DispatcherType.REQUEST，则为null。
         * isMatchAfter - 如果给定的filter mapping在任何声明的filter mapping之后匹配，则为true；如果在获取此FilterRegistration的ServletContext的任何声明的filter mapping之前应该匹配，则为false。
         * urlPatterns - filter mapping的url patterns
         */
        registration.addMappingForUrlPatterns(dispatcherTypes, !insertBeforeOtherFilters,
                "/*");
    }

    /**
     * 使用{@link #isAsyncSessionSupported()}和{@link #getSessionDispatcherTypes()}注册提供的过滤器。
     *
     * @param servletContext           servlet上下文
     * @param insertBeforeOtherFilters 此过滤器是否应该在其他{@link Filter}之前插入
     * @param filterName               过滤器名称
     * @param filter                   过滤器
     * @param initParams               初始化参数
     * @param urlPatterns              拦截路径
     */
    private void registerFilter(ServletContext servletContext,
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

    /**
     * 使用{@link #isAsyncSessionSupported()}注册提供的Servlet。
     *
     * @param servletContext servlet上下文
     * @param servletName    servlet名称
     * @param servlet        servlet
     * @param initParams     初始化参数
     * @param urlPatterns    拦截路径
     */
    private void registerServlet(ServletContext servletContext,
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

    /**
     * 使用默认生成的名称、{@link #isAsyncSessionSupported()}和{@link #getSessionDispatcherTypes()}注册提供的{@link Filter}。
     *
     * @param servletContext           servlet上下文
     * @param insertBeforeOtherFilters 如果为true，将插入提供的{@link Filter}在其他{@link Filter}之前。 否则，将插入{@link Filter} 在其他{@link Filter}之后。
     * @param filters                  要注册的{@link Filter}
     */
    private void registerFilters(ServletContext servletContext,
                                 boolean insertBeforeOtherFilters, Filter... filters) {
        Assert.notEmpty(filters, "filters cannot be null or empty");
        for (Filter filter : filters) {
            if (filter == null) {
                throw new IllegalArgumentException(
                        "filters cannot contain null values. Got "
                                + Arrays.asList(filters));
            }
            String filterName = Conventions.getVariableName(filter);
            registerFilter(servletContext, insertBeforeOtherFilters, filterName, filter);
        }
    }

    protected final void insertFilters(ServletContext servletContext, Filter... filters) {
        registerFilters(servletContext, true, filters);
    }

    protected final void appendFilters(ServletContext servletContext, Filter... filters) {
        registerFilters(servletContext, false, filters);
    }

    /**
     * springSessionRepositoryFilter是否支持异步。默认值为true。
     *
     * @return true 如果springSessionRepositoryFilter支持异步，则返回true
     */
    protected boolean isAsyncSessionSupported() {
        return true;
    }

    /**
     * Get the {@link DispatcherType} for the springSessionRepositoryFilter.
     *
     * @return the {@link DispatcherType} for the filter
     */
    protected EnumSet<DispatcherType> getSessionDispatcherTypes() {
        return EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR,
                DispatcherType.ASYNC);
    }

    private String getWebApplicationContextAttribute() {
        String dispatcherServletName = getDispatcherWebApplicationContextSuffix();
        if (dispatcherServletName == null) {
            return null;
        }
        return SERVLET_CONTEXT_PREFIX + dispatcherServletName;
    }

    /**
     * 返回{@code <servlet-name>}，以使用{@link DispatcherServlet}的{@link WebApplicationContext}查找{@link DelegatingFilterProxy}，或者Null使用父级{@link ApplicationContext}。
     * <p>
     * 例如，如果您使用{@link AbstractDispatcherServletInitializer}或{@link AbstractAnnotationConfigDispatcherServletInitializer}并使用提供的Servlet名称，
     * 则可以从此方法返回"dispatcher"以使用DispatcherServlet的{@link WebApplicationContext}。
     *
     * @return DispatcherServlet的{@code <servlet-name>}以使用它的{@link WebApplicationContext}或Null（默认）使用父级{@link ApplicationContext}。
     */
    protected String getDispatcherWebApplicationContextSuffix() {
        return "dispatcher";
    }

    /**
     * 在添加springSessionRepositoryFilter之前调用。
     *
     * @param servletContext {@link ServletContext}
     */
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

    /**
     * 在添加springSessionRepositoryFilter之后调用。
     *
     * @param servletContext {@link ServletContext}
     */
    protected void afterSessionRepositoryFilter(ServletContext servletContext) {
    }

}
