package com.peanut.item.controller;

import com.peanut.item.entity.Brand;
import com.peanut.item.service.IBrandService;
import com.peanut.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ljn
 * @date 2019/7/31.
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @GetMapping("page")
    @ApiOperation(value="查询品牌列表")
    public ResponseEntity<PageResult<Brand>> getBrandList(
            @RequestParam(value="page", defaultValue = "1") Integer page,
            @RequestParam(value="rows", defaultValue = "5") Integer rows,
            @RequestParam(value="sortBy", required = false) String sortBy,
            @RequestParam(value="desc", defaultValue = "false") Boolean desc,
            @RequestParam(value="key", required = false) String key
    ) {
        PageResult<Brand> result = brandService.queryBrandByPage(page, rows, sortBy, desc, key);
        return ResponseEntity.ok(result);
    }
}
