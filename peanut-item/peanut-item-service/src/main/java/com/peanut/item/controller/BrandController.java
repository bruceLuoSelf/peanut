package com.peanut.item.controller;

import com.peanut.item.entity.Brand;
import com.peanut.item.entity.BrandVo;
import com.peanut.item.service.IBrandService;
import com.peanut.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam(value="key", required = false) String key) {
        PageResult<Brand> result = brandService.queryBrandByPage(page, rows, sortBy, desc, key);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @ApiOperation(value="增加品牌")
    public ResponseEntity addBrand(Brand brand, @RequestParam("cids")List<Long> cids) {
        brandService.addBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    @ApiOperation(value = "修改品牌")
    public ResponseEntity updateBrand(BrandVo brandVo) {
        brandService.updateBrand(brandVo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("cid/{cid}")
    @ApiOperation(value = "根据分类id查询品牌")
    public ResponseEntity<List<Brand>> queryBrandByCategoryId(@PathVariable("cid")Long cid) {
        return ResponseEntity.ok(brandService.queryBrandByCategoryId(cid));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据品牌id查询品牌")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id")Long id) {
        return ResponseEntity.ok(brandService.queryBrandById(id));
    }

    @GetMapping("list")
    @ApiOperation(value = "根据品牌id集合查询品牌列表")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids")List<Long> ids) {
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }

}
