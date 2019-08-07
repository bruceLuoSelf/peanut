package com.peanut.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import com.peanut.item.dao.mapper.SkuMapper;
import com.peanut.item.dao.mapper.SpuDetailMapper;
import com.peanut.item.dao.mapper.SpuMapper;
import com.peanut.item.dao.mapper.StockMapper;
import com.peanut.item.entity.*;
import com.peanut.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
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

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGoods(Spu spu) {
        try {
            spu.setCreateTime(new Date());
            spu.setLastUpdateTime(new Date());
            spu.setValid(false);
            spu.setSaleable(true);
            spuMapper.insert(spu);

            SpuDetail detail = spu.getSpuDetail();
            detail.setSpuId(spu.getId());
            spuDetailMapper.insert(detail);

            List<Sku> skuList = spu.getSkus();
            List<Stock> stockList = new ArrayList<>();
            for (Sku sku : skuList) {
                sku.setCreateTime(new Date());
                sku.setLastUpdateTime(new Date());
                sku.setSpuId(spu.getId());
                skuMapper.insert(sku);

                Stock stock = new Stock();
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockList.add(stock);
            }
            stockMapper.insertList(stockList);

        } catch (Exception e) {
            throw new PeanutException(ExceptionEnum.ADD_GOODS_FAILD);
        }

    }

    @Override
    public SpuDetail queryDetailById(Long id) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        if (spuDetail == null) {
            throw new PeanutException(ExceptionEnum.SPU_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    @Override
    public List<Sku> querySkuList(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new PeanutException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new PeanutException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
        }
        Map<Long, Integer> map = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skuList.forEach(s -> s.setStock(map.get(s.getId())));
        return skuList;
    }

    private void loadCategoryAndBrandName(List<Spu> spuList) {
        for (Spu spu : spuList) {
            List<String> names = categoryService.queryCategoryListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCategoryName(StringUtils.join(names, "/"));
            spu.setBrandName(brandService.queryBrandById(spu.getBrandId()).getName());
        }

    }


}
