package com.github.ittalks.fn.common.advice;

import com.github.ittalks.fn.common.advice.exception.ErrorMessage;
import com.github.ittalks.fn.common.advice.exception.NestedException;
import com.github.ittalks.fn.common.result.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by 刘春龙 on 2017/6/6.
 */
@ControllerAdvice("com.github.ittalks")
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NestedException.class})
    public ResponseEntity<?> handleNestedException(NestedException e) {
        ErrorMessage eErrorMessage = e.getErrorMessage();
        if (eErrorMessage != null) {
            return new ResponseEntity<ErrorMessage>(eErrorMessage, null, eErrorMessage.getHttpStatus());
        } else {
            return new ResponseEntity<ErrorMessage>(ErrorCode.BAD_REQUEST, null, ErrorCode.BAD_REQUEST.getHttpStatus());
        }
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<?> handleNestedException(RuntimeException e) {
        return new ResponseEntity<ErrorMessage>(ErrorCode.INTERNAL_SERVER_ERROR, null, ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
}
