package com.peanut.auth.controller;

import com.peanut.auth.config.JwtProperties;
import com.peanut.auth.service.AuthService;
import com.peanut.common.pojo.UserInfo;
import com.peanut.common.utils.CookieUtils;
import com.peanut.common.utils.JwtUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ljn
 * @date 2019/8/30.
 */
@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties properties;

    @ApiOperation("授权")
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username")String username,
                                         @RequestParam("password")String password,
                                         HttpServletRequest request,
                                         HttpServletResponse response
                                         ) {
        String token = authService.accredit(username, password);
        if (StringUtils.isBlank(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CookieUtils.setCookie(request, response, properties.getCookieName(), token, properties.getExpire() * 60);
        return ResponseEntity.ok(null);
    }

    /**
     * @CookieValue springMVC中使用该注解可以获取cookie中的参数
     * @return
     */
    @ApiOperation("验证用户信息")
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("PEANUT_TOKEN")String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        UserInfo user = null;
        try {
            // 解析jwt，获取用户信息
            user = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            // 刷新jwt和cookie的有效时间
            token = JwtUtils.generateToken(user, properties.getPrivateKey(), properties.getExpire());
            CookieUtils.setCookie(request, response, properties.getCookieName(), token, properties.getExpire() * 60);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
