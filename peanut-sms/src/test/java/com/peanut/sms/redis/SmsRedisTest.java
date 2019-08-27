package com.peanut.sms.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ljn
 * @date 2019/8/27.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsRedisTest {

    @Autowired
    SmsRedis smsRedis;

    @Test
    public void addSendTime() {
        smsRedis.saveSmsPhone("18827065959");
    }
}