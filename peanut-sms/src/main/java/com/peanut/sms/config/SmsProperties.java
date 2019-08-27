package com.peanut.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ljn
 * @date 2019/8/27.
 */
@ConfigurationProperties(prefix = "peanut.sms")
public class SmsProperties {

    String accessKey;

    String accessSecret;

    String signName;

    String templateCode;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
}

