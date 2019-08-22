package com.peanut.page.service;

import com.peanut.item.entity.*;
import com.peanut.page.client.BrandClient;
import com.peanut.page.client.CategoryClient;
import com.peanut.page.client.GoodsClient;
import com.peanut.page.client.SpecificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljn
 * @date 2019/8/21.
 */
@Service
public class PageService {
    @Autowired
    GoodsClient goodsClient;

    @Autowired
    SpecificationClient specificationClient;

    @Autowired
    BrandClient brandClient;

    @Autowired
    CategoryClient categoryClient;

    public Map<String, Object> loadModel(Long spuId) {
        Spu spu = goodsClient.querySpuById(spuId);
        List<Sku> skus = spu.getSkus();
        SpuDetail spuDetail = spu.getSpuDetail();
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        List<Category> categories = categoryClient.queryCategoryListByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<SpecGroup> specParams = specificationClient.queryGroupsByCid(spu.getCid3());
        Map<String, Object> model = new HashMap<>();
        model.put("title", spu.getTitle());
        model.put("subTitle", spu.getSubTitle());
        model.put("skus", skus);
        model.put("detail", spuDetail);
        model.put("brand", brand);
        model.put("categories", categories);
        model.put("specs", specParams);
        return model;
    }
}
