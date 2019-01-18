package com.mxcg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 22:07
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.mxcg.entity","com.mxcg.entity.enums","com.mxcg.Repository"})
public class AccountApplicaion {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplicaion.class, args);
    }
}
