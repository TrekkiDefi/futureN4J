package com.github.ittalks.fn.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by 刘春龙 on 2017/7/26.
 * <p>
 * 在Servlet3.0+下，我们服务器启动后，Web容器（Tomcat等）会自动查找实现了{@link javax.servlet.ServletContainerInitializer}接口的类来完成初始化（应该类似于替代web.xml）
 * 然而我们的这个类与ServletContainerInitializer没有半毛钱关系
 * <p>
 * 但是Spring的{@link org.springframework.web.SpringServletContainerInitializer}实现了这个接口
 * 在SpringServletContainerInitializer中会查找实现类了{@link org.springframework.web.WebApplicationInitializer}接口的类并将配置任务交给他们完成
 * 当然我们的FnApplicationInitializer也就是间接实现了这个接口
 * <p>
 * 而且在继承这个抽象类时，必须实现如下的三个方法
 */
public class FnApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * 类似于web.xml中配置ContextLoaderListener的contextConfigLocation
     * 只是这个返回的是一个JavaConfig的Class数组
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootApplicationConfig.class};
    }

    /**
     * 类似于web.xml中配置DispatcherServlet的contextConfigLocation
     * 只是这个返回的是一个JavaConfig的Class数组
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebApplicationConfig.class};
    }

    /**
     * 等同于{@code <mapping-url/>};
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
