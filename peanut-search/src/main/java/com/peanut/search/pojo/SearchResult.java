package com.peanut.search.pojo;

import com.peanut.item.entity.Brand;
import com.peanut.item.entity.Category;
import com.peanut.vo.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author ljn
 * @date 2019/8/15.
 */
public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;

    private List<Brand> brands;

    /**
     * 规格参数过滤条件
     */
    private List<Map<String, Object>> specs;

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public SearchResult() {
    }

    public SearchResult(Long page, Integer totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(page, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
