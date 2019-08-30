package com.peanut.auth.test;

import com.peanut.common.pojo.UserInfo;
import com.peanut.common.utils.JwtUtils;
import com.peanut.common.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ljn
 * @date 2019/8/30.
 */

public class JwtTest {

    private static final String pubKeyPath = "C:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "C:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Test
    public void tessstRsa() throws Exception {
        File file = new File("C:\\tmp\\rsa");
        file.mkdir();

    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU2NzE0Mzg1MH0.SbNyP5Gj3gXyn6iVahMytHOuyJn8BFmHygUlHWDx6mN3MkMujfJpg-tbdToN15eO2nnnHOtvNmAuJJSOyLM0GRRNSCvIKCfc_E33VpzvI_RrVzz7IoQZNzOzV1uz8Ts1KaQzIRZdI63NYsZlYeKXZjvZ-4FWLYzArxAeq5ZSFcc";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
