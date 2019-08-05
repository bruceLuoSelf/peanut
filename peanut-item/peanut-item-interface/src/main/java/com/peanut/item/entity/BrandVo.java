package com.peanut.item.entity;

import java.util.List;

/**
 * @author ljn
 * @date 2019/8/5.
 */
public class BrandVo {

    private Long id;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌图片地址
     */
    private String image;

    /**
     * 品牌的首字母
     */
    private String letter;

    /**
     * 分类id
     */
    private List<Long> cids;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<Long> getCids() {
        return cids;
    }

    public void setCids(List<Long> cids) {
        this.cids = cids;
    }
}
