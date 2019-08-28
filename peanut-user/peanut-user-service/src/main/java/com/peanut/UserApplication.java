package com.peanut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author ljn
 * @date 2019/8/28.
 */
@MapperScan("com.peanut.user.dao.mapper")
@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
