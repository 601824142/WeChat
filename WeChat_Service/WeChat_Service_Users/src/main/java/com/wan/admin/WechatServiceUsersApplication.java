package com.wan.admin;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.wan")
@EnableEurekaClient
@MapperScan("com.wan.dao")
@Import(FdfsClientConfig.class) //FastDFS注解
@EnableFeignClients("com.wan")
public class WechatServiceUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatServiceUsersApplication.class, args);
    }

}
