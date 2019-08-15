package com.peanut.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peanut.common.utils.BeanMapper;
import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import com.peanut.item.dao.mapper.BrandMapper;
import com.peanut.item.entity.Brand;
import com.peanut.item.entity.BrandCategory;
import com.peanut.item.entity.BrandVo;
import com.peanut.item.entity.Category;
import com.peanut.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Iterator;
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
        result.setTotalPage(pageInfo.getPages());
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
        this.insertBrandCategory(brand.getId(), cids);
    }

    private void insertBrandCategory(Long bid, List<Long> cids) {
        for (Long cid : cids) {
            brandMapper.addCategoryBrand(cid, bid);
        }
    }

    @Override
    public List<Category> queryCategoryByBid(Long id) {
        return brandMapper.queryCategoryByBid(id);
    }

    @Override
    public void deleteBrand(Long id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改品牌分类
     *
     * @param brandVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBrand(BrandVo brandVo) {
        if (brandVo == null) {
            throw new PeanutException(ExceptionEnum.PARAM_ERROR);
        }
        Brand brand = BeanMapper.map(brandVo, Brand.class);
        brandMapper.updateByPrimaryKey(brand);
        List<BrandCategory> brandCategoryList = brandMapper.getBrandCategoryByBrandId(brandVo.getId());
        if (CollectionUtils.isNotEmpty(brandCategoryList)) {
            Iterator<BrandCategory> it = brandCategoryList.iterator();
            while (it.hasNext()) {
                BrandCategory category = it.next();
                if (brandVo.getCids().contains(category.getCategoryId())) {
                    //删除新数据中与就数据重复的,剩下的是要插入的
                    brandVo.getCids().remove(category.getCategoryId());
                    //删除重复数据，剩下的是要删除的
                    it.remove();
                }
            }
        }
        //删除页面删除的品牌分类
        if (CollectionUtils.isNotEmpty(brandCategoryList)) {
            for (BrandCategory category : brandCategoryList) {
                brandMapper.deleteBrandCategoryByBrandCategoryId(brandVo.getId(), category.getCategoryId());
            }
        }
        //插入新增的品牌分类
        if (CollectionUtils.isNotEmpty(brandVo.getCids())) {
            this.insertBrandCategory(brandVo.getId(), brandVo.getCids());
        }
    }

    @Override
    public Brand queryBrandById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null) {
            throw new PeanutException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    @Override
    public List<Brand> queryBrandByCategoryId(Long cid) {
        List<Brand> brands = brandMapper.queryBrandByCategoryId(cid);
        if (CollectionUtils.isEmpty(brands)) {
            throw new PeanutException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }

    @Override
    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brands)) {
            throw new PeanutException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }

}
