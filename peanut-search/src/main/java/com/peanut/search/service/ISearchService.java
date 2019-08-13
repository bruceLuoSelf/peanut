package com.peanut.search.service;

import com.peanut.item.entity.Spu;
import com.peanut.search.pojo.Goods;
import com.peanut.search.pojo.SearchRequest;
import com.peanut.vo.PageResult;

/**
 * @author ljn
 * @date 2019/8/13.
 */
public interface ISearchService {


    Goods buildGoods(Spu spu);

    PageResult<Goods> search(SearchRequest request);
}
