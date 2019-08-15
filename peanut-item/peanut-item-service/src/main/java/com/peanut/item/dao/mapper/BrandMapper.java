package com.peanut.item.dao.mapper;

import com.peanut.item.entity.Brand;
import com.peanut.item.entity.BrandCategory;
import com.peanut.item.entity.Category;
import com.peanut.mapper.MyMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BrandMapper extends MyMapper<Brand> {

    /**
     * 新增分类品牌
     * @param cid
     * @param brandId
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid}, #{brandId})")
    int addCategoryBrand(@Param("cid") Long cid, @Param("brandId") Long brandId);

    /**
     * 根据品牌id获取所有的品牌分类
     * @param bid
     * @return
     */
    List<BrandCategory> getBrandCategoryByBrandId(@Param("bid")Long bid);

    /**
     * 根据牌品id和品牌分类id删除品牌分类信息
     * @param bid
     * @param cid
     */
    @Delete("DELETE FROM tb_category_brand where brand_id = #{bid} and category_id = #{cid}")
    void deleteBrandCategoryByBrandCategoryId(@Param("bid")Long bid, @Param("cid")Long cid);

    @Select("select a.* from tb_category a inner join tb_category_brand b on a.id = b.category_id and b.brand_id = #{id}")
    List<Category> queryCategoryByBid(Long id);

    @Select("select a.* from tb_brand a inner join tb_category_brand b on a.id = b.brand_id  and b.category_id = #{cid}")
    List<Brand> queryBrandByCategoryId(Long cid);
}