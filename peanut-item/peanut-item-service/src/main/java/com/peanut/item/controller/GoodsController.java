package com.peanut.item.controller;

import com.peanut.item.entity.Spu;
import com.peanut.item.service.IGoodsService;
import com.peanut.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spu")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    @ApiOperation(value = "分页查询商品")
    @GetMapping("/page")
    public ResponseEntity<PageResult<Spu>> queryGoods(
            @RequestParam(value="page", defaultValue = "1") Integer page,
            @RequestParam(value="rows", defaultValue = "5") Integer rows,
            @RequestParam(value="saleable", required = false) Boolean saleable,
            @RequestParam(value="key", required = false) String key
    ) {
        PageResult<Spu> pageResult = goodsService.queryGoods(page, rows, saleable, key);
        return ResponseEntity.ok(pageResult);
    }
}
