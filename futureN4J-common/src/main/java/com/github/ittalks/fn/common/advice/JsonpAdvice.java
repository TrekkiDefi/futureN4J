package com.github.ittalks.fn.common.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * Created by 刘春龙 on 2017/5/25.
 * <p>
 * 用{@link RequestMappingHandlerAdapter}和{@link ExceptionHandlerExceptionResolver}注册，
 * 或者更倾向于用{@link ControllerAdvice @ControllerAdvice}注解，在这种情况下，JsonpAdvice将被两者自动检测
 */
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpAdvice() {
        super("callback", "jsonp");
    }
}
