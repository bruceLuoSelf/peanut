package com.peanut.search.repository;

import com.peanut.search.pojo.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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
}