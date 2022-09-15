package com.ydw.oa.wkflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 * 
 * Api: http://localhost:17210/oa-wkflow/swagger-ui.html
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EnableAutoConfiguration(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		org.activiti.spring.boot.SecurityAutoConfiguration.class })
@MapperScan("com.ydw.oa.wkflow.business.*.mapper")
@EnableScheduling
public class BatWkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatWkflowApplication.class, args);
	}

}
