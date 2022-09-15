package com.ydw.oa.wkflow.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置表
 * 
 * @author 冯晓东 398479251@qq.com
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		 registry.addInterceptor(new LoginAdminInterceptor()).addPathPatterns("/cp/**");
	}

}
