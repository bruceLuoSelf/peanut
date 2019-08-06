package com.peanut.item.service;

import com.peanut.item.entity.Spu;
import com.peanut.vo.PageResult;

import java.util.List;

public interface IGoodsService {

    PageResult<Spu> queryGoods(Integer page, Integer rows, Boolean saleable, String key);
}
