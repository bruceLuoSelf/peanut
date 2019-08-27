package com.peanut.sms.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljn
 * @date 2019/8/27.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsListenerTest {

    @Autowired
    private SmsListener smsListener;

    @Test
    public void listenSms() {
        Map<String,String> msg = new HashMap<>();
        msg.put("code", "992233");
        msg.put("phone", "18827065959");
        smsListener.listenSms(msg);
    }

    @Test
    public void test() {
        System.out.println("test");
    }

}