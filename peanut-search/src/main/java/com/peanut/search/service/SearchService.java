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
import com.peanut.search.pojo.SearchRequest;
import com.peanut.search.pojo.SearchResult;
import com.peanut.search.repository.GoodsRepository;
import com.peanut.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ljn
 * @date 2019/8/12.
 */
@Service
public class SearchService implements ISearchService{

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    ElasticsearchTemplate template;

    @Override
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

    @Override
    public PageResult<Goods> search(SearchRequest request) {
        // es搜索从0页开始
        Integer page = request.getPage() - 1;
        Integer size = SearchRequest.getDefaultRows();
        // 查询构建起
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        // 聚合分类
        String categoryAggName = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        // 聚合品牌
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        // 分页
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 过滤
        if (StringUtils.isBlank(request.getKey())) {
            request.setKey(null);
        }
        QueryBuilder basicQuery = this.buildBasicQuery(request);
        queryBuilder.withQuery(basicQuery);
        // 查询
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        // 解析聚合结果
        Aggregations aggs = result.getAggregations();
        List<Category> categories = this.parseCategoryAgg(aggs.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));

        // 完成规格参数聚合
        List<Map<String, Object>> specs = null;
        if (CollectionUtils.isNotEmpty(categories) && categories.size() == 1) {
            //商品分类存在并且数量为1，可以聚合规格参数
            specs = this.buildSpecificationAgg(categories.get(0).getId(), basicQuery);
        }
        return new SearchResult(total, totalPage, goodsList, categories, brands, specs);
    }

    @Override
    public void createOrUpdateIndex(Long spuId) {
        Spu spu = goodsClient.querySpuById(spuId);
        // 构建goods对象
        Goods goods = buildGoods(spu);
        // 存入索引库
        goodsRepository.save(goods);
    }

    @Override
    public void deleteIndex(Long spuId) {
        goodsRepository.deleteById(spuId);
    }

    private QueryBuilder buildBasicQuery(SearchRequest request) {
        // 创建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()));
        // 过滤条件
        Map<String, String> map = request.getFilter();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            // 处理key
            if (!"cid3".equals(key) && !"brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }
            queryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }
        return queryBuilder;
    }

    private List<Map<String, Object>> buildSpecificationAgg(Long id, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        // 查询需要进行聚合的规格参数
        List<SpecParam> specParams = specClient.queryGroupParams(null, id, true);
        // 聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(basicQuery);
        for (SpecParam specParam : specParams) {
            String name = specParam.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        }
        //获取，解析结果
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        Aggregations aggs = result.getAggregations();
        for (SpecParam specParam : specParams) {
            String name = specParam.getName();
            StringTerms terms = aggs.get(name);
            List<String> options = terms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList());
            Map<String, Object> map = new HashMap<>();
            map.put("k", name);
            map.put("options", options);
            specs.add(map);
        }
        return specs;
    }

    private List<Category> parseCategoryAgg(LongTerms terms) {
        try{
            List<Long> ids = terms.getBuckets().stream().map(c -> c.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Category> categories = categoryClient.queryCategoryListByIds(ids);
            return categories;
        }catch (Exception e) {
            return null;
        }
    }

    private List<Brand> parseBrandAgg(LongTerms terms) {
        try{
            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandByIds(ids);
            return brands;
        }catch (Exception e) {
            return null;
        }
    }
}
