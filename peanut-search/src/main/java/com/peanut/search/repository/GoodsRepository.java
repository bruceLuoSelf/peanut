package com.peanut.search.repository;

import com.peanut.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author ljn
 * @date 2019/8/12.
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
