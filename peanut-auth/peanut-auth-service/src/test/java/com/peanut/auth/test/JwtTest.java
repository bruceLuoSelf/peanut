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
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU2NzYwNDkwM30.U8wxFDy0zeUgBDDfsMv9ldd63XVA_1pgngffxjFIYrSHECMpWLOHDqw76ZE0jLGE6QblzOdpDaQ7L3F8PgK4Vk425xLUPdfTJbiJGn3BbWyrNe-5oKezMw7oGMVO_s0dY5_9F65uEuc9JRufM8nvAP_FSchP2AmtfDCnSxajbaU";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
