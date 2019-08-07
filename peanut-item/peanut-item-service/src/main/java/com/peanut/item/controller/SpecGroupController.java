package com.peanut.item.controller;

import com.peanut.item.entity.SpecGroup;
import com.peanut.item.entity.SpecParam;
import com.peanut.item.service.IBrandService;
import com.peanut.item.service.ISpecGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ljn
 * @date 2019/8/6.
 */
@RestController
@RequestMapping("spec")
public class SpecGroupController {

    @Autowired
    private ISpecGroupService specGroupService;

    @Autowired
    private IBrandService brandService;

    @ApiOperation(value = "根据分类id查询分组信息")
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid")Long cid) {
        return ResponseEntity.ok(specGroupService.queryGroupByCid(cid));
    }

    @ApiOperation(value = "新增规格分组")
    @PostMapping("group")
    public ResponseEntity addGroup(@RequestBody SpecGroup specGroup) {
        specGroupService.addSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "删除规格分组")
    @DeleteMapping("group/{id}")
    public ResponseEntity deleteGroup(@PathVariable("id")Long id) {
        specGroupService.deleteSpecGroup(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "修改规格分组")
    @PutMapping("group")
    public ResponseEntity updateGroup(@RequestBody SpecGroup specGroup) {
        specGroupService.updateSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "查询规格参数")
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryGroupParams(@RequestParam(value = "gid",required = false)Long gid,
                                                            @RequestParam(value = "cid",required = false)Long cid,
                                                            @RequestParam(value = "searching",required = false)Boolean searching) {
        List<SpecParam> list = specGroupService.queryGroupParams(gid, cid, searching);
        return ResponseEntity.ok(list);
    }


}
