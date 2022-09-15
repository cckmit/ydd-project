package com.ydw.oa.wechat.config;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @Title WxCorpConfig
 * @Description 企业微信配置类
 * @Author ZhangKai
 * @Date 2020/4/1 0001
 * @Version 1.0
 * @Email 410618538@qq.com
 */

@Getter
@Setter
@Slf4j
@Configuration
public class WxCorpConfig implements InitializingBean {
    @Value("${weixin.corp.corpid}")
    private String corpid;
    @Value("${weixin.corp.agentid}")
    private String agentid;
    @Value("${weixin.corp.notice-agentid}")
    private String noticeAgentId;


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
