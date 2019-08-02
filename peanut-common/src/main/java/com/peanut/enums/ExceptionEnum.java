package com.peanut.enums;

public enum ExceptionEnum {

    PRICE_CANNOT_BE_NULL(400, "商品价格不能为空"),

    CATEGORY_NOT_FOUND(404, "未找到商品分类"),

    BRAND_NOT_FOUND(404, "未找到商品品类"),

    PARAM_ERROR(404, "参数错误"),

    ALREADY_EXIST_DATA(404, "已存在相同的数据")
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
