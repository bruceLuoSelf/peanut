package com.peanut.enums;

public enum ExceptionEnum {

    PRICE_CANNOT_BE_NULL(400, "商品价格不能为空"),
    ;
    private int code;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
