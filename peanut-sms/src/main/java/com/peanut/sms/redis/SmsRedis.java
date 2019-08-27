package com.peanut.sms.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author ljn
 * @date 2019/8/27.
 */
@Service
public class SmsRedis {

    static final String SMS_TIMES_KEY_PREFIX = "sms:times:";

    @Autowired
    StringRedisTemplate redisTemplate;

    public void saveSmsPhone(String phoneNumber) {
        redisTemplate.opsForValue().set(SMS_TIMES_KEY_PREFIX + phoneNumber, phoneNumber, 1L, TimeUnit.MINUTES);
    }

    public String getSmsPhone(String phoneNumber) {
        return redisTemplate.opsForValue().get(SMS_TIMES_KEY_PREFIX + phoneNumber);
    }


}
