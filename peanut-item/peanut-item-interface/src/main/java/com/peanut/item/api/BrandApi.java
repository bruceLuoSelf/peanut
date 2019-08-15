package com.peanut.item.api;

import com.peanut.item.entity.Brand;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ljn
 * @date 2019/8/12.
 */
public interface BrandApi {

    @GetMapping("brand/{id}")
    @ApiOperation(value = "根据品牌id查询品牌")
    Brand queryBrandById(@PathVariable("id")Long id);

    @GetMapping("brand/list")
    @ApiOperation(value = "根据品牌id集合查询品牌列表")
    List<Brand> queryBrandByIds(@RequestParam("ids")List<Long> ids);


}
