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
        pageResult.setTotalPage(info.getPages());
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

            this.batchAddSkuAndStock(spu);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoods(Spu spu) {
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        List<Long> skuIds = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        stockMapper.deleteByIdList(skuIds);
        skuMapper.deleteByIdList(skuIds);

        batchAddSkuAndStock(spu);
        spu.setCreateTime(null);
        spu.setLastUpdateTime(new Date());
        spu.setSaleable(null);
        spu.setValid(null);
        spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public Spu querySpuById(Long id) {
        // 查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new PeanutException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        // 查询sku
        List<Sku> skus = this.querySkuList(id);
        spu.setSkus(skus);
        // 查询detail
        SpuDetail spuDetail = this.queryDetailById(id);
        spu.setSpuDetail(spuDetail);
        return spu;
    }

    private void batchAddSkuAndStock(Spu spu) {
        List<Sku> skuList = spu.getSkus();
        List<Stock> stockList = new ArrayList<>();
        for (Sku s : skuList) {
            s.setSpuId(spu.getId());
            s.setCreateTime(new Date());
            s.setLastUpdateTime(new Date());
            skuMapper.insert(s);

            Stock stock = new Stock();
            stock.setSkuId(s.getId());
            stock.setStock(s.getStock());
            stockList.add(stock);
        }
        stockMapper.insertList(stockList);
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
