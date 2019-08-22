package com.peanut.item.api;

import com.peanut.item.entity.SpecGroup;
import com.peanut.item.entity.SpecParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ljn
 * @date 2019/8/12.
 */
public interface SpecificationApi {

    @ApiOperation(value = "查询规格参数")
    @GetMapping("spec/params")
    List<SpecParam> queryGroupParams(@RequestParam(value = "gid",required = false)Long gid,
                                                            @RequestParam(value = "cid",required = false)Long cid,
                                                            @RequestParam(value = "searching",required = false)Boolean searching);

    @ApiOperation("查询规格组")
    @GetMapping("spec/group")
    List<SpecGroup> queryGroupsByCid(@RequestParam("id")Long cid);
}
