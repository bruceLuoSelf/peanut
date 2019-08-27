package com.peanut.sms.mq;

import com.peanut.common.utils.JsonUtils;
import com.peanut.sms.config.SmsProperties;
import com.peanut.sms.utils.SmsUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author ljn
 * @date 2019/8/27.
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties properties;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name="sms.verify.code.queue", durable = "true"),
            exchange = @Exchange(name="peanut.sms.exchange", type = ExchangeTypes.TOPIC),
            key={"sms.verify.code"}
    ))
    public void listenSms(Map<String, String> msg) {
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.remove("phone");
        if (StringUtils.isEmpty(phone)) {
            return;
        }
        smsUtils.sendSms(phone, properties.getSignName(), properties.getTemplateCode(), JsonUtils.serialize(msg));
    }
}
