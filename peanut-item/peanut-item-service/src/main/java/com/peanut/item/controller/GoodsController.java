package com.peanut.item.controller;

import com.peanut.item.entity.Sku;
import com.peanut.item.entity.Spu;
import com.peanut.item.entity.SpuDetail;
import com.peanut.item.service.IGoodsService;
import com.peanut.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @ApiOperation(value = "分页查询商品")
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> queryGoods(
            @RequestParam(value="page", defaultValue = "1") Integer page,
            @RequestParam(value="rows", defaultValue = "5") Integer rows,
            @RequestParam(value="saleable", required = false) Boolean saleable,
            @RequestParam(value="key", required = false) String key) {
        PageResult<Spu> pageResult = goodsService.queryGoods(page, rows, saleable, key);
        return ResponseEntity.ok(pageResult);
    }

    @ApiOperation(value = "新增商品")
    @PostMapping("goods")
    public ResponseEntity addGoods(@RequestBody Spu spu) {
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "查询商品")
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> queryGoodsDetail(@PathVariable("id")Long id) {
        return ResponseEntity.ok(goodsService.queryDetailById(id));
    }

    @ApiOperation(value = "查询商品")
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuList(@RequestParam("id")Long id) {
        return ResponseEntity.ok(goodsService.querySkuList(id));
    }

    @ApiOperation(value = "修改商品")
    @PutMapping("goods")
    public ResponseEntity updateGoods(@RequestBody Spu spu) {
        goodsService.updateGoods(spu);
        return ResponseEntity.noContent().build();
    }
}
