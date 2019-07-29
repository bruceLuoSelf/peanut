//package com.peanut.item.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class ServletContextConfig implements WebMvcConfigurer {
//    /**
//     * 发现如果继承了WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。
//     * 需要重新指定静态资源
//     * @param registry
//     */
//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//                registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//                registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//                registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//            }
//
//    /**
//     * 配置servlet处理
//     */
//     @Override
//     public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//         configurer.enable();
//     }
//}
