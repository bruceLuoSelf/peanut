package com.peanut.item.controller;

import com.peanut.item.entity.Category;
import com.peanut.item.service.ICategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @ApiOperation(value="查询所有商品分类", notes="查询所有商品分类")
    @GetMapping("list")
    public ResponseEntity<List<Category>> getCategoryList(@RequestParam("pid") Long pid) {
        return ResponseEntity.ok(categoryService.getCategoryList(pid));
    }
}
