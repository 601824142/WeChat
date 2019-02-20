package com.wan.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  //指明该项目为注册中心
public class WechatEruekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatEruekaApplication.class, args);
    }

}

