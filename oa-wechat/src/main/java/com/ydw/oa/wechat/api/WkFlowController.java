package com.ydw.oa.wechat.api;

import cn.hutool.core.map.MapUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wechat.client.OAApiClient;
import com.ydw.oa.wechat.client.WechatApiProxyClient;
import com.ydw.oa.wechat.client.WkFlowApiClient;
import com.ydw.oa.wechat.config.WxCorpConfig;
import com.ydw.oa.wechat.entity.wechat.WxCorpOAuthResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/wkflow")
public class WkFlowController {

    @Autowired
    private WechatApiProxyClient wechatApiProxyClient;
    @Autowired
    private WkFlowApiClient wkFlowApiClient;
    @Autowired
    private WxCorpConfig wxCorpConfig;

    @GetMapping("/tasks/{taskId}")
    public Wrapper<Map<String,Object>> tasks(@PathVariable String taskId) {
        Wrapper<Map<String, Object>> wkFlowInfo = wkFlowApiClient.getWkFlowInfo(taskId, "");
        return wkFlowInfo;
    }

}
