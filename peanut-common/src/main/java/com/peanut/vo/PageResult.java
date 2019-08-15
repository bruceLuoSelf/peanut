package com.peanut.vo;

import java.util.List;

/**
 * @author ljn
 * @date 2019/7/31.
 */
public class PageResult<T> {

    private Long page;

    private Integer totalPage;

    private List<T> items;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public PageResult(Long page, Integer totalPage, List<T> items) {
        this.page = page;
        this.totalPage = totalPage;
        this.items = items;
    }

    public PageResult() {
    }
}
