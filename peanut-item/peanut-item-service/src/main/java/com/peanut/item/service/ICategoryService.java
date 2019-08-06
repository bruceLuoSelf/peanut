package com.peanut.item.service;

import com.peanut.item.entity.Category;

import java.util.List;

public interface ICategoryService {

    List<Category> getCategoryList(Long parentId);

    List<Category> queryCategoryListByIds(List<Long> ids);
}
