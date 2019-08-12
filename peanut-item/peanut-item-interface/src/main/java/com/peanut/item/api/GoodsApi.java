package com.peanut.item.api;

import com.peanut.item.entity.Sku;
import com.peanut.item.entity.Spu;
import com.peanut.item.entity.SpuDetail;
import com.peanut.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ljn
 * @date 2019/8/12.
 */
public interface GoodsApi {

    @ApiOperation(value = "查询商品明细")
    @GetMapping("spu/detail/{id}")
    SpuDetail queryGoodsDetail(@PathVariable("id")Long id);

    @ApiOperation(value = "查询商品列表")
    @GetMapping("sku/list")
    List<Sku> querySkuList(@RequestParam("id")Long id);

    @ApiOperation(value = "分页查询商品")
    @GetMapping("/spu/page")
    PageResult<Spu> queryGoods(
            @RequestParam(value="page", defaultValue = "1") Integer page,
            @RequestParam(value="rows", defaultValue = "5") Integer rows,
            @RequestParam(value="saleable", required = false) Boolean saleable,
            @RequestParam(value="key", required = false) String key);
}
