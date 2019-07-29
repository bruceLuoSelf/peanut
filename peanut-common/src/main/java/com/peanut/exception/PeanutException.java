package com.peanut.exception;

import com.peanut.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PeanutException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public PeanutException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }


}
