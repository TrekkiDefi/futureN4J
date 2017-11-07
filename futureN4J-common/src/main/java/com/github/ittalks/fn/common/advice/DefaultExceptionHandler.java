package com.github.ittalks.fn.common.advice;

import com.github.ittalks.fn.common.advice.exception.ErrorMessage;
import com.github.ittalks.fn.common.advice.exception.NestedException;
import com.github.ittalks.fn.common.result.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by 刘春龙 on 2017/6/6.
 * <p>
 * 它希望通过{@code @ExceptionHandler}方法跨所有{@code @RequestMapping}方法提供集中的异常处理。
 * 这个基础类提供了一个{@code @ExceptionHandler}方法来处理Spring MVC内部的异常。
 * 此方法返回一个{@code ResponseEntity}，用于通过{@link HttpMessageConverter 消息转换器}写入到响应中。
 * 相反，{@link org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver DefaultHandlerExceptionResolver}返回一个{@link org.springframework.web.servlet.ModelAndView ModelAndView}。
 * <p>
 * 如果不需要将错误内容写入响应主体，或者使用视图解析（例如，通过{@code ContentNegotiatingViewResolver}），那么{@code DefaultHandlerExceptionResolver}就足够了。
 * <p>
 * 请注意，为了检测{@link ControllerAdvice @ControllerAdvice}注释的类，必须配置{@link ExceptionHandlerExceptionResolver}，
 * 用{@link ControllerAdvice @ControllerAdvice}注解，在这种情况下，该类将被自动检测。
 */
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NestedException.class})
    public ResponseEntity<?> handleNestedException(NestedException e) {
        ErrorMessage eErrorMessage = e.getErrorMessage();
        if (eErrorMessage != null) {
            return new ResponseEntity<>(eErrorMessage, null, eErrorMessage.getHttpStatus());
        } else {
            return new ResponseEntity<>(ErrorCode.BAD_REQUEST, null, ErrorCode.BAD_REQUEST.getHttpStatus());
        }
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(ErrorCode.INTERNAL_SERVER_ERROR, null, ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
}
