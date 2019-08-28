package com.peanut.enums;

public enum ExceptionEnum {

    PRICE_CANNOT_BE_NULL(404, "商品价格不能为空"),

    CATEGORY_NOT_FOUND(404, "未找到商品分类"),

    BRAND_NOT_FOUND(404, "未找到商品品类"),

    PARAM_ERROR(404, "参数错误"),

    ALREADY_EXIST_DATA(404, "已存在相同的数据"),

    UPLOAD_FILE_FAIL(404, "上传文件失败"),

    INVALID_FILE_TYPE(500, "无效的文件类型"),

    SPEC_GROUP_NOT_FOUND(404, "未找到分组"),

    SPEC_PARAM_NOT_FOUND(404, "未找到商品规格参数"),

    GOODS_NOT_FOUND(404, "未找到商品"),

    ADD_GOODS_FAILD(404, "新增商品失败"),

    SPU_DETAIL_NOT_FOUND(404, "未找到商品详情"),

    GOODS_SKU_NOT_FOUND(404, "未找到商品SKU"),

    GOODS_STOCK_NOT_FOUND(400, "未找到商品库存"),

    SMS_TIMES_LIMIT(400, "一分钟之内只能发送一次短信验证码"),

    USER_ALREADY_EXIST(400, "该用户名已存在"),

    VERIFY_CODE_ERROR(400, "验证码错误"),
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
