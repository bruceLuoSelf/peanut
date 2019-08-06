package com.peanut.item.controller;

import com.peanut.item.entity.Category;
import com.peanut.item.service.IBrandService;
import com.peanut.item.service.ICategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IBrandService brandService;

    @ApiOperation(value="查询所有商品分类", notes="查询所有商品分类")
    @GetMapping("list")
    public ResponseEntity<List<Category>> getCategoryList(@RequestParam("pid") Long pid) {
        return ResponseEntity.ok(categoryService.getCategoryList(pid));
    }

    @GetMapping("bid/{id}")
    @ApiOperation(value = "根据品牌id查询商品分类")
    public ResponseEntity<List<Category>> queryCategoryByBid(@PathVariable("id")Long id) {
        List<Category> categories = brandService.queryCategoryByBid(id);
        return ResponseEntity.ok(categories);
    }

    @ApiOperation(value = "删除品牌")
    @DeleteMapping("{id}")
    public ResponseEntity deleteBrand(@PathVariable("id")Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
