package com.peanut.search.pojo;

/**
 * @author ljn
 * @date 2019/8/13.
 */
public class SearchRequest {

    private static final int DEFAULT_ROWS = 20;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 搜索字段
     */
    private String key;

    public Integer getPage() {
        if (page == null) {
            page = 1;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static int getDefaultRows() {
        return DEFAULT_ROWS;
    }
}
