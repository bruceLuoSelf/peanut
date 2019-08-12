package com.peanut.item.api;

import com.peanut.item.entity.Category;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ljn
 * @date 2019/8/12.
 */
public interface CategoryApi {

    @ApiOperation(value = "根据id查询商品分类")
    @GetMapping("category/list/ids")
    List<Category> queryCategoryListByIds(@RequestParam("ids")List<Long> ids);
}
