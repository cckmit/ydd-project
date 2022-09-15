package com.ydw.oa.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.ydw.oa.wechat.mapper")
@EnableScheduling
public class OaWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(OaWechatApplication.class, args);
    }

}
