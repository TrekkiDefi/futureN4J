package com.github.ittalks.fn.config;

import com.github.ittalks.fn.common.converter.JacksonObjectMapper;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by 刘春龙 on 2017/7/26.
 * <p>
 * 作为JavaConfig类，必须有@Configuration注解<br/>
 * 注解@{@link EnableWebMvc}启用SpringMVC，类似于xml中配置<mvc:annotation-driven/><br/>
 * 扩展{@link DelegatingWebMvcConfiguration}，即等价于@{@code EnableWebMvc}
 */
@Configuration
//@EnableWebMvc
@ComponentScan(basePackages = {"com.github.ittalks"},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {ControllerAdvice.class, ExceptionHandler.class})}
)
@ImportResource("/WEB-INF/spring-servlet.xml")
public class WebApplicationConfig extends DelegatingWebMvcConfiguration {


    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(new JacksonObjectMapper());
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        return stringHttpMessageConverter;
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(stringHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter());
        super.addDefaultHttpMessageConverters(converters);
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /**
     * 配置静态资源处理
     **/
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
