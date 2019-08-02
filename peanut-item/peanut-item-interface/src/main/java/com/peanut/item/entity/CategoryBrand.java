package com.peanut.item.entity;

/**
 * @author ljn
 * @date 2019/8/2.
 * 分类品牌
 */
public class CategoryBrand {

    private Long brandId;

    private Long categoryId;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
