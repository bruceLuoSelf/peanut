package com.peanut.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     * @NotEmpty 非空校验
     */
    @NotEmpty(message = "用户名不能为空")
    @Length(min=4,max = 32,message="用户名长度必须在4~32位")
    private String username;

    /**
     * 密码，加密存储
     * @JsonIgnore 接口不再返回这个字段
     */
    @JsonIgnore
    @NotEmpty(message = "密码不能为空")
    @Length(min=4,max = 32,message="密码长度必须在4~32位")
    private String password;

    /**
     * 注册手机号
     */
    @Pattern(regexp = "^1[3456789]\\d{9}$",
            message = "手机号格式不正确")
    @NotEmpty(message = "手机号不能为空")
    private String phone;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 密码加密的salt值
     */
    @JsonIgnore
    private String salt;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码，加密存储
     *
     * @return password - 密码，加密存储
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码，加密存储
     *
     * @param password 密码，加密存储
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取注册手机号
     *
     * @return phone - 注册手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置注册手机号
     *
     * @param phone 注册手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取创建时间
     *
     * @return created - 创建时间
     */
    public Date getCreated() {
        return created;
    }

    /**
     * 设置创建时间
     *
     * @param created 创建时间
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * 获取密码加密的salt值
     *
     * @return salt - 密码加密的salt值
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置密码加密的salt值
     *
     * @param salt 密码加密的salt值
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }
}