package com.peanut.user.api;

import com.peanut.user.entity.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ljn
 * @date 2019/8/30.
 */
public interface UserApi {

    @ApiOperation("根据用户名和密码查询用户")
    @GetMapping("/query")
    User queryUser(@RequestParam("username")String username, @RequestParam("password")String password);
}
