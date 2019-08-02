package com.peanut.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import com.peanut.item.dao.mapper.BrandMapper;
import com.peanut.item.entity.Brand;
import com.peanut.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author ljn
 * @date 2019/7/31.
 */
@Service
public class BrandService implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        PageHelper.startPage(page, rows);
        if (StringUtils.isNotBlank(sortBy) && desc != null) {
            example.setOrderByClause(sortBy + (desc ? " DESC" : " ASC"));
        }
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%");
        }
        List<Brand> brandList = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new PeanutException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        PageResult<Brand> result = new PageResult<Brand>();
        PageInfo<Brand> pageInfo = new PageInfo<Brand>(brandList);
        result.setItems(brandList);
        result.setTotlePage(pageInfo.getPages());
        result.setPage(pageInfo.getTotal());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBrand(Brand brand, List<Long> cids) {
        if (brand == null || StringUtils.isBlank(brand.getName()) || StringUtils.isBlank(brand.getLetter()) ||
                CollectionUtils.isEmpty(cids)) {
            throw new PeanutException(ExceptionEnum.PARAM_ERROR);
        }
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", brand.getName());
        criteria.andEqualTo("letter", brand.getLetter());
        List<Brand> brandList = brandMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(brandList)) {
            throw new PeanutException(ExceptionEnum.ALREADY_EXIST_DATA);
        }
        brandMapper.insert(brand);
        for (Long cid : cids) {
            brandMapper.addCategoryBrand(cid, brand.getId());
        }
    }
}
