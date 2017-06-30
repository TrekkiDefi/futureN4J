package com.github.ittalks.fn.common.advice;

import com.github.ittalks.fn.common.advice.exception.ErrorMessage;
import com.github.ittalks.fn.common.advice.exception.NestedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by 刘春龙 on 2017/6/6.
 */
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NestedException.class)
    public ResponseEntity<?> handleNestedException(NestedException e) {
        ErrorMessage eErrorMessage = e.getErrorMessage();
        return new ResponseEntity<ErrorMessage>(eErrorMessage, null, eErrorMessage.getHttpStatus());
    }
}
