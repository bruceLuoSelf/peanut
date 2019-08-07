package com.peanut.item.dao.mapper;

import com.peanut.item.entity.Stock;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface StockMapper extends Mapper<Stock>, InsertListMapper<Stock>, IdListMapper<Stock, Long> {
}