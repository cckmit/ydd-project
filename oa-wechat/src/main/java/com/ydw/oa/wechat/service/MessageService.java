package com.ydw.oa.wechat.service;

import java.util.Map;

public interface MessageService {

    /**
     * 预处理推送消息
     */
    Map<String, Object> getAnyMessage(Map<String,Object> map);
}
