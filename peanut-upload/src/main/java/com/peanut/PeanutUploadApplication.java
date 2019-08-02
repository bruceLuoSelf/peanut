package com.peanut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ljn
 * @date 2019/8/2.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
public class PeanutUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeanutUploadApplication.class);
    }
}
