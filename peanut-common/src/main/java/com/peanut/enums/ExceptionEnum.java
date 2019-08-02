package com.peanut.enums;

public enum ExceptionEnum {

    PRICE_CANNOT_BE_NULL(404, "商品价格不能为空"),

    CATEGORY_NOT_FOUND(404, "未找到商品分类"),

    BRAND_NOT_FOUND(404, "未找到商品品类"),

    PARAM_ERROR(404, "参数错误"),

    ALREADY_EXIST_DATA(404, "已存在相同的数据"),

    UPLOAD_FILE_FAIL(404, "上传文件失败"),

    INVALID_FILE_TYPE(500, "无效的文件类型")
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
