package com.ydw.oa.wkflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
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
	public Docket activitiApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.ydw.oa.wkflow.business_activiti")).paths(allowPaths())
				.build().groupName("activiti接口");
	}

	@Bean
	public Docket businessApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.ydw.oa.wkflow.business_main")).paths(allowPaths())
				.build().groupName("业务接口");
	}

	@Bean
	public Docket wkflowApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.ydw.oa.wkflow.business_wkflow")).paths(allowPaths())
				.build().groupName("表单接口");
	}

	private ApiInfo apiInfo() {
		Contact contact = new Contact("工作流系统", "http://localhost:17210/oa-wkflow", null);
		return new ApiInfoBuilder().contact(contact).title("工作流系统 API").description("activiti接口及工作流相关业务接口")
				.version("1.0.0").build();
	}

	private Predicate<String> allowPaths() {
		return PathSelectors.any();
	}
}