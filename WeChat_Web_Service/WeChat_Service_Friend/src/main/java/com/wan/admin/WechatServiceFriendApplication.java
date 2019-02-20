package com.wan.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.wan")
@EnableFeignClients("com.wan")
@MapperScan("com.wan.dao")
@EnableEurekaClient
public class WechatServiceFriendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatServiceFriendApplication.class, args);
    }

}
