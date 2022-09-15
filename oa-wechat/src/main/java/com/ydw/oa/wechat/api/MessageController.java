package com.ydw.oa.wechat.api;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wechat.client.WechatApiProxyClient;
import com.ydw.oa.wechat.config.WxCorpConfig;
import com.ydw.oa.wechat.consts.WechatConfigConstant;
import com.ydw.oa.wechat.entity.wechat.WxCorpMessageResult;
import com.ydw.oa.wechat.enums.MsgTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
@Slf4j
public class MessageController {

    @Autowired
    private WechatApiProxyClient wechatApiProxyClient;
    @Autowired
    private WxCorpConfig wxCorpConfig;

    @PostMapping("/text")
    public Wrapper text(@RequestBody Map<String,Object> map) {
        Map<String, Object> jsonObj = getTextMessage(map);
        log.info("text消息：{}", JSONUtil.toJsonStr(jsonObj));
        WxCorpMessageResult wxCorpMessageResult = wechatApiProxyClient.text(jsonObj);
        return WrapMapper.ok(wxCorpMessageResult);
    }

    private Map<String, Object> getTextMessage(Map<String, Object> map) {
        Map<String,Object> textMessageMap = new HashMap<>();
        textMessageMap.put("touser", MapUtil.getStr(map, "touser"));
        textMessageMap.put("msgtype", MsgTypeEnum.TEXT);
        textMessageMap.put("agentid", wxCorpConfig.getAgentid());
        textMessageMap.put("text", getText(map));
        return textMessageMap;
    }

    private Object getText(Map<String, Object> map) {
        Map<String,Object> textMap = new HashMap<>();
        textMap.put("content", MapUtil.getStr(map, "content"));
        return textMap;
    }

    @PostMapping("/textcard")
    public Wrapper textcard(@RequestBody Map<String,Object> map) {
        Map<String, Object> jsonObj = getTextCardMessage(map,wxCorpConfig.getAgentid());
        log.info("textcard消息：{}", JSONUtil.toJsonStr(jsonObj));
        WxCorpMessageResult wxCorpMessageResult = wechatApiProxyClient.textcard(jsonObj);
        return WrapMapper.ok(wxCorpMessageResult);
    }

    @PostMapping("/textcard_notice")
    public Wrapper textcard_notice(@RequestBody Map<String,Object> map) {
        Map<String, Object> jsonObj = getTextCardMessage(map,wxCorpConfig.getNoticeAgentId());
        log.info("textcard-notice消息：{}", JSONUtil.toJsonStr(jsonObj));
        WxCorpMessageResult wxCorpMessageResult = wechatApiProxyClient.textcard(jsonObj);
        return WrapMapper.ok(wxCorpMessageResult);
    }

    private Map<String, Object> getTextCardMessage(Map<String, Object> map,String agentId) {
        Map<String,Object> textcardMessageMap = new HashMap<>();
        textcardMessageMap.put("touser", MapUtil.getStr(map, "touser"));
        textcardMessageMap.put("msgtype", MsgTypeEnum.TEXTCARD);
        textcardMessageMap.put("agentid", agentId);
        textcardMessageMap.put("textcard", getTextCard(map,agentId));
        return textcardMessageMap;
    }

    private Map<String,Object> getTextCard(Map<String, Object> textcardMessageMap,String agentId) {
        String title = MapUtil.getStr(textcardMessageMap, "title");
        String description = MapUtil.getStr(textcardMessageMap, "description");
        Map<String, Object> params =  MapUtil.get(textcardMessageMap, "params",HashMap.class);
        String redirectUrl = getRedirectUrl(params,agentId);
        Map<String,Object> textCardMap = new HashMap<>();
        textCardMap.put("title", title);
        textCardMap.put("description", description);
        textCardMap.put("url", redirectUrl);
        textCardMap.put("btntxt", WechatConfigConstant.TEXT_CARD_BTN_TXT);
        return textCardMap;
    }

    private String getRedirectUrl(Map<String, Object> params,String agentId) {
        params.put("agentId", agentId);
        String oauthApiUri = WechatConfigConstant.OAUTH_API_URI;
        String redirectUrl = URLUtil.encodeAll(WechatConfigConstant.REDIRECT_URI.concat("?").concat(URLUtil.buildQuery(params, Charset.forName("utf-8"))));
        return String.format(oauthApiUri, wxCorpConfig.getCorpid(), redirectUrl);
    }

}
