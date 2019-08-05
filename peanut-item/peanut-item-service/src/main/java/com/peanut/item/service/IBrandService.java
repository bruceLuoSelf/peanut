package com.peanut.item.service;

import com.peanut.item.entity.Brand;
import com.peanut.item.entity.BrandVo;
import com.peanut.item.entity.Category;
import com.peanut.vo.PageResult;

import java.util.List;

/**
 * @author ljn
 * @date 2019/7/31.
 */
public interface IBrandService {

    PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    void addBrand(Brand brand, List<Long> cids);

    void updateBrand(BrandVo brandVo);

    Brand selectBrand(Long bid);

    List<Category> queryCategoryByBid(Long id);
}
