package com.peanut.user.service;

import com.peanut.common.utils.NumberUtils;
import com.peanut.enums.ExceptionEnum;
import com.peanut.exception.PeanutException;
import com.peanut.user.dao.mapper.UserMapper;
import com.peanut.user.entity.User;
import com.peanut.user.utils.CodeUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ljn
 * @date 2019/8/28.
 */
@Service
public class UserService {

    private static final String verify_code_key = "user:verify:phone:";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate template;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     *
     * @param data
     * @param type 1：校验用户名；2：校验手机号
     * @return
     */
    public Boolean checkData(String data, Integer type) {
        // 判断数据类型
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new PeanutException(ExceptionEnum.PARAM_ERROR);
        }
        return userMapper.selectCount(user) == 0;
    }

    public void sendCode(String phone) {
        // 生成验证码
        String code =NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("code", code);
        msg.put("phone", phone);
        // 发送验证码
        template.convertAndSend("peanut.sms.exchange", "sms.verify.code", msg);
        // 保存验证码
        redisTemplate.opsForValue().set(verify_code_key + phone, code, 1L, TimeUnit.MINUTES);
    }

    public void register(User user, String code) {
        String verifyCode = redisTemplate.opsForValue().get(verify_code_key + user.getPhone());
        if (!code.equals(verifyCode)) {
            throw new PeanutException(ExceptionEnum.VERIFY_CODE_ERROR);
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        if (userMapper.selectCount(newUser) > 0) {
            throw new PeanutException(ExceptionEnum.USER_ALREADY_EXIST);
        }
        String salt = CodeUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodeUtils.md5Hex(user.getPassword(), salt));
        user.setCreated(new Date());
        userMapper.insert(user);
    }
}
