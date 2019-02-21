package com.wan.admin;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.wan")
@EnableEurekaClient
@Import(FdfsClientConfig.class) //FastDFS注解
@EnableFeignClients(basePackages = "com.wan")
public class WechatServiceResourcesApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatServiceResourcesApplication.class, args);
    }

}
