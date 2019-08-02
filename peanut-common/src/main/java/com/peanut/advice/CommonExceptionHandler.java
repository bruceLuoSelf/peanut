package com.peanut.advice;

import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import com.peanut.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(PeanutException.class)
    public ResponseEntity<ExceptionResult> handlerException(PeanutException e) {
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }
}
