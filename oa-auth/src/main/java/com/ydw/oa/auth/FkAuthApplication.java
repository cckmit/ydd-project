package com.ydw.oa.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 * 
 * Api: http://localhost:18201/oa-auth/swagger-ui.html
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@MapperScan("com.ydw.oa.auth.business.*.mapper")
@EnableScheduling
public class FkAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(FkAuthApplication.class, args);
	}

}
