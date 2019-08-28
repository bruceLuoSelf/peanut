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

    /**
     * 发送短信
     * @param phone
     */
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

    /**
     * 注册账号
     * @param user
     * @param code
     */
    public void register(User user, String code) {
        // 校验验证码
        String verifyCode = redisTemplate.opsForValue().get(verify_code_key + user.getPhone());
        if (!code.equals(verifyCode)) {
            throw new PeanutException(ExceptionEnum.VERIFY_CODE_ERROR);
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        // 判断该用户名是否已注册
        if (userMapper.selectCount(newUser) > 0) {
            throw new PeanutException(ExceptionEnum.USER_ALREADY_EXIST);
        }
        // 加盐
        String salt = CodeUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodeUtils.md5Hex(user.getPassword(), salt));
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    public User queryUserByUsernameAndPassword(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user = userMapper.selectOne(user);
        if (user == null) {
            throw new PeanutException(ExceptionEnum.USER_NOT_ALREADY_EXIST);
        }
        String salt = user.getSalt();
        String pwd = CodeUtils.md5Hex(password, salt);
        if (!pwd.equals(user.getPassword())) {
            throw new PeanutException(ExceptionEnum.PASSWORD_ERROR);
        }
        return user;
    }
}
