package com.peanut.item.dao.mapper;

import com.peanut.item.entity.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {

    /**
     * 新增分类品牌
     * @param cid
     * @param brandId
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid}, #{brandId})")
    int addCategoryBrand(@Param("cid") Long cid, @Param("brandId") Long brandId);
}