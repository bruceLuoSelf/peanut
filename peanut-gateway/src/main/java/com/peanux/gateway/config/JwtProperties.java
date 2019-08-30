package com.peanux.gateway.config;

import com.peanut.common.utils.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author ljn
 * @date 2019/8/30.
 */
@Slf4j
@Component
public class JwtProperties {

    /**
     * 公钥路径
     */
    @Value("${peanut.jwt.pubKeyPath}")
    private String pubKeyPath;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * cookie名称
     */
    @Value("${peanut.jwt.cookieName}")
    private String cookieName;

    /**
     * @PostContruct：在构造方法执行之后执行该方法
     */
    @PostConstruct
    public void init(){
        try {
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }
}
