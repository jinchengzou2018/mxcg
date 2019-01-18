package com.mxcg.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 */

@EnableZuulProxy
@EnableFeignClients
@EnableEurekaClient
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启权限注解
@SpringBootApplication(scanBasePackages = {"com.mxcg"})
public class GatewayApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApiApplication.class, args);
    }
}