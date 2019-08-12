package com.peanut.search.repository;

import com.peanut.item.entity.Spu;
import com.peanut.search.client.GoodsClient;
import com.peanut.search.pojo.Goods;
import com.peanut.search.service.SearchService;
import com.peanut.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ljn
 * @date 2019/8/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    ElasticsearchTemplate template;

    @Test
    public void test() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }


    @Autowired
    GoodsClient goodsClient;

    @Autowired
    SearchService searchService;

    /**
     * 导入所有商品到es
     */
    @Test
    public void buildData() {
        int page = 1;
        int rows = 100;
        int size = 0;
        do{
            PageResult<Spu> result = goodsClient.queryGoods(page, rows, true, null);
            List<Spu> spuList = result.getItems();
            if (CollectionUtils.isEmpty(spuList)) {
                break;
            }
            List<Goods> goodsList = spuList.stream().map(searchService::buildGoods).collect(Collectors.toList());
            goodsRepository.saveAll(goodsList);
            page++;
            size = spuList.size();
        }while (size == 100);
    }
}