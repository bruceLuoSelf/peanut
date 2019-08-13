package com.peanut.search.controller;

import com.peanut.search.pojo.Goods;
import com.peanut.search.pojo.SearchRequest;
import com.peanut.search.service.ISearchService;
import com.peanut.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ljn
 * @date 2019/8/12.
 */
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private ISearchService searchService;

    @ApiOperation("搜索")
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request) {
        PageResult<Goods> result = searchService.search(request);
        return ResponseEntity.ok(result);
    }
}
