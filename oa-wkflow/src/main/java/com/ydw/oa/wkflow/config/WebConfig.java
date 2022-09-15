package com.ydw.oa.wkflow.config;

import org.springframework.context.annotation.Configuration;

/**
 * Web全局配置
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
@Configuration
public class WebConfig {
	/**
	 * 长度需要>8
	 */
	public static final String DESKEY = "des4TbAdminId";
	/**
	 * 记录 cookie中的session值
	 */
	public static final String ADMINSESSION = "ADMINSESSION";
	/**
	 * 记录 cookie中的session值
	 */
	public static final String MEMBERSESSION = "MEMBERSESSION";

}
