package com.peanut.search.pojo;

import com.peanut.item.entity.Brand;
import com.peanut.item.entity.Category;
import com.peanut.vo.PageResult;

import java.util.List;

/**
 * @author ljn
 * @date 2019/8/15.
 */
public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;

    private List<Brand> brands;

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

    public SearchResult(Long page, Integer totalPage, List items, List<Category> categories, List<Brand> brands) {
        super(page, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult() {
    }
}
