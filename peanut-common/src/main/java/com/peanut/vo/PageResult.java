package com.peanut.vo;

import java.util.List;

/**
 * @author ljn
 * @date 2019/7/31.
 */
public class PageResult<T> {

    private Long page;

    private Integer totlePage;

    private List<T> items;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Integer getTotlePage() {
        return totlePage;
    }

    public void setTotlePage(Integer totlePage) {
        this.totlePage = totlePage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
