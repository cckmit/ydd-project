package com.ydw.oa.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置
 * 
 * @author 冯晓东 398479251@qq.com
 * 
 *         swagger-ui.html
 *
 */
@Configuration
@EnableSwagger2
//@ConditionalOnProperty(prefix = "ydw", value = "swagger", havingValue = "false")
public class SwaggerConfig {

	@Bean
	public Docket swaggerSpringMvcPlugin() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(
				new ApiInfoBuilder().title("权限子系统 API").description("菜单/角色/用户/机构等权限管理子系统").version("1.0.0").build())
				.select().apis(RequestHandlerSelectors.basePackage("com.ydw.oa.auth.business")).build();
	}

}