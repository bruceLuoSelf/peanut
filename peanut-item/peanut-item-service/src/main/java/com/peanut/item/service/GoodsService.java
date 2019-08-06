package com.peanut.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import com.peanut.item.dao.mapper.SpuDetailMapper;
import com.peanut.item.dao.mapper.SpuMapper;
import com.peanut.item.entity.Category;
import com.peanut.item.entity.Spu;
import com.peanut.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService implements IGoodsService {

    @Autowired
    SpuMapper spuMapper;

    @Autowired
    SpuDetailMapper spuDetailMapper;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IBrandService brandService;

    @Override
    public PageResult<Spu> queryGoods(Integer page, Integer rows, Boolean saleable, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        example.setOrderByClause("last_update_time DESC");
        List<Spu> spuList = spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spuList)) {
            throw new PeanutException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        loadCategoryAndBrandName(spuList);
        PageInfo<Spu> info = new PageInfo<Spu>(spuList);
        PageResult<Spu> pageResult = new PageResult<Spu>();
        pageResult.setTotlePage(info.getPages());
        pageResult.setItems(spuList);
        pageResult.setPage(info.getTotal());
        return pageResult;
    }

    private void loadCategoryAndBrandName(List<Spu> spuList) {
        for (Spu spu : spuList) {
            List<String> names = categoryService.queryCategoryListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCategoryName(StringUtils.join(names, "/"));
            spu.setBrandName(brandService.queryBrandNameById(spu.getBrandId()).getName());
        }

    }
}
