package com.peanut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@EnableDiscoveryClient
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.peanut.item.dao.mapper")
@EnableTransactionManagement
public class ItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }

    /**
     * 从数据库查询的数据没有改变时，页面会直接获取缓存中的数据
     * ETag: "0df7a9e21864420f1513dd86a730da0ed"
     * 页面请求时会携带Etag，然后与服务端返回的Etag进行对比，如果值相等，则直接走缓存
     * 用户访问快
     * 节约服务器带宽
     * @return
     */
    @Bean
    public ShallowEtagHeaderFilter shallowETagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}
