package com.wan.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.wan")
public class WechatWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatWebsocketApplication.class, args);
    }

}
