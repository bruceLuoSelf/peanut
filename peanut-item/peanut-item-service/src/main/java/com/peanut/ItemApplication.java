package com.peanut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@EnableDiscoveryClient
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.peanut.item.dao.mapper")
@EnableTransactionManagement
public class ItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class);
    }
}
