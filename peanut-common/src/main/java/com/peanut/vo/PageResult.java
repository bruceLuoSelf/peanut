package com.peanut.vo;

import java.util.List;

/**
 * @author ljn
 * @date 2019/7/31.
 */
public class PageResult<T> {

    private Integer page;

    private Long totlePage;

    private List<T> items;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Long getTotlePage() {
        return totlePage;
    }

    public void setTotlePage(Long totlePage) {
        this.totlePage = totlePage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
