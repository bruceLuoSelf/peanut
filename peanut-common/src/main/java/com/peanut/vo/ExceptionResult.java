package com.peanut.vo;

import com.peanut.enums.ExceptionEnum;

import java.util.Date;

/**
 * 异常结果
 */
public class ExceptionResult {

    private int status;

    private String message;

    private Long timestamp;

    public ExceptionResult(ExceptionEnum em) {
        this.status = em.getCode();
        this.message = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }

    public ExceptionResult() {
    }
}
