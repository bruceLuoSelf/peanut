package com.peanut.item.service;

import com.peanut.item.entity.Sku;
import com.peanut.item.entity.Spu;
import com.peanut.item.entity.SpuDetail;
import com.peanut.vo.PageResult;

import java.util.List;

public interface IGoodsService {

    PageResult<Spu> queryGoods(Integer page, Integer rows, Boolean saleable, String key);

    void saveGoods(Spu spu);

    SpuDetail queryDetailById(Long id);

    List<Sku> querySkuList(Long id);

    void updateGoods(Spu spu);

    Spu querySpuById(Long id);
}
