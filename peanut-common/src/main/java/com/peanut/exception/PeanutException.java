package com.peanut.exception;

import com.peanut.enums.ExceptionEnum;

public class PeanutException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public PeanutException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public void setExceptionEnum(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
