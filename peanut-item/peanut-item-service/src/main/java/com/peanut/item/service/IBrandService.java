package com.peanut.item.service;

import com.peanut.item.entity.Brand;
import com.peanut.vo.PageResult;

/**
 * @author ljn
 * @date 2019/7/31.
 */
public interface IBrandService {

    PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key);
}
