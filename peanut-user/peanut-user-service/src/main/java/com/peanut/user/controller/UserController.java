package com.peanut.user.controller;

import com.peanut.user.entity.User;
import com.peanut.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ljn
 * @date 2019/8/28.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("校验用户参数")
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data")String data, @PathVariable("type")Integer type) {
        return ResponseEntity.ok(userService.checkData(data, type));
    }

    @ApiOperation("发送短信验证码")
    @PostMapping("/code")
    public ResponseEntity<Void> sendCode(String phone) {
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation("注册账号")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code) {
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
