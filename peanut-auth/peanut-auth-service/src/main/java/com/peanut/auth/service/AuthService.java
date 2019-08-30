package com.peanut.auth.service;

import com.peanut.auth.client.UserClient;
import com.peanut.auth.config.JwtProperties;
import com.peanut.common.pojo.UserInfo;
import com.peanut.common.utils.JwtUtils;
import com.peanut.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ljn
 * @date 2019/8/30.
 */
@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties properties;


    /**
     * 获取token
     * @param username
     * @param password
     * @return
     */
    public String accredit(String username, String password) {
        // 根据用户名和密码查询
        User user = userClient.queryUser(username, password);
        // 判断用户
        if (user == null) {
            return null;
        }
        // jwtUtils生成jwt类型的token
        String token = null;
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(user.getUsername());
        userInfo.setId(user.getId());
        try {
            token = JwtUtils.generateToken(userInfo, properties.getPrivateKey(), properties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{},[用户登录]，生成token发生异常，用户名称:{}", new Object[]{new Date(), user.getUsername()});
        }
        return token;
    }
}
