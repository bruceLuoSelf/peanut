package com.peanut.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.peanut.common.utils.JsonUtils;
import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import com.peanut.item.entity.*;
import com.peanut.search.client.BrandClient;
import com.peanut.search.client.CategoryClient;
import com.peanut.search.client.GoodsClient;
import com.peanut.search.client.SpecificationClient;
import com.peanut.search.pojo.Goods;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ljn
 * @date 2019/8/12.
 */
@Service
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specClient;

    public Goods buildGoods(Spu spu) {
        List<Category> categoryList = categoryClient.queryCategoryListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new PeanutException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> nameList = categoryList.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new PeanutException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //搜索字段
        String all = spu.getTitle() + StringUtils.join(nameList, " ") + brand.getName();

        //查询sku
        List<Sku> skuList = goodsClient.querySkuList(spu.getId());
        if (skuList == null) {
            throw new PeanutException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //对sku进行处理
        List<Map<String, Object>> skus = new ArrayList<>();
        Set<Long> priceSet = new HashSet<>();
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.substringBefore(sku.getImages(), ","));
            skus.add(map);
            priceSet.add(sku.getPrice());
        }

        // 查询规格参数
        List<SpecParam> specParams = specClient.queryGroupParams(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(specParams)) {
            throw new PeanutException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        //查询商品详情
        SpuDetail spuDetail = goodsClient.queryGoodsDetail(spu.getId());
        // 获取通用规格参数
        Map<String, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), String.class, String.class);
        // 获取特有的规格参数
        String json = spuDetail.getSpecialSpec();
        Map<String, List<String>> specialSpec = JsonUtils
                .nativeRead(json, new TypeReference<Map<String, List<String>>>() {});
        // 规格参数 key是规格参数的名字，value是规格参数的值
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam specParam : specParams) {
            // 规格名称
            String key = specParam.getName();
            Object value = "";
            // 判断是否是通用规格参数
            if (specParam.getGeneric()) {
                value = genericSpec.get(specParam.getId().toString());
                // 判断是否是数值类型
                if (specParam.getNumeric()) {
                    value = chooseSegment(value.toString(), specParam);
                }
            } else {
                value = specialSpec.get(specParam.getId().toString());
            }
            specs.put(key, value);
        }

        //构建goods对象
        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        // 搜索字段
        goods.setAll(all);
        // 所有sku的价格几何
        goods.setPrice(priceSet);
        // 所有sku的集合的json格式
        goods.setSkus(JsonUtils.serialize(skus));
        // 所有可搜索的规格集合
        goods.setSpecs(specs);
        goods.setSubTitle(spu.getSubTitle());
        return goods;

    }

    /**
     * 将规格参数为数值型的参数划分为段
     *
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
