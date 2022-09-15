package com.ydw.oa.wechat.client;

import com.ydw.oa.wechat.entity.wechat.*;
import com.ydw.oa.wechat.entity.wechat.oa.WxCorpOAClockInInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name="jwechat-api-proxy",url = "${corp.url}")
public interface WechatApiProxyClient {

    // 根据code获取userid
    @GetMapping("/corp/oauth/userinfo")
    WxCorpOAuthResult getUserInfo(@RequestParam("agentId") String agentId, @RequestParam("code") String code);

    // 获取打卡数据
    @RequestMapping(value = "/corp/clockin/data",method = RequestMethod.POST,consumes = "application/json")
    WxCorpOAClockInResult getClockInData(@RequestBody Map wxCorpOAClockIn);

    // 获取审批数据
    @RequestMapping(value = "/corp/approve/sp_no_list",method = RequestMethod.POST,consumes = "application/json")
    WxCorpOAApproveResult getApproveSpNoList(@RequestBody Map wxCorpOAApprove);

    // 获取审批数据详情
    @RequestMapping(value = "/corp/approve/detail")
    WxCorpOAApproveDetailResult getApproveDetail(@RequestParam String spNo);

    //推送文本卡片
    @RequestMapping(value = "/corp/messages/textcard",method = RequestMethod.POST,consumes = "application/json")
    WxCorpMessageResult textcard(@RequestBody Map jsonObj);
    //推送文本消息
    @RequestMapping(value = "/corp/messages/text",method = RequestMethod.POST,consumes = "application/json")
    WxCorpMessageResult text(@RequestBody Map jsonObj);
}
