package com.wan.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.wan")
@EnableEurekaClient
public class WechatServiceChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatServiceChatApplication.class, args);
    }

}
