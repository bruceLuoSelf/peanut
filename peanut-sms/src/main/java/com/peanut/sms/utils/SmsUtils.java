package com.peanut.sms.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.peanut.sms.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ljn
 * @date 2019/8/27.
 */
@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtils {

    static final String DO_MAIN = "dysmsapi.aliyuncs.com";

    @Autowired
    private SmsProperties properties;

    public void sendSms(String phoneNumber, String signName, String templateCode, String templateParam) {
        // 处理消息
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", properties.getAccessKey(), properties.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(DO_MAIN);
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", templateParam);
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            log.info("发送短信失败：" + request);
            e.printStackTrace();
        } catch (ClientException e) {
            log.info("发送短信失败：" + request);
            e.printStackTrace();
        }
    }
}
