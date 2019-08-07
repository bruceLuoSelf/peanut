package com.peanut.item.dao.mapper;

import com.peanut.item.entity.Sku;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface SkuMapper extends Mapper<Sku> ,MySqlMapper<Sku> {
}