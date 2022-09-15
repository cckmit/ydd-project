package com.ydw.oa.wechat.client;

import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wechat.entity.wechat.WxCorpOAuthResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "${wkfow.auth}",path = "/oa-auth"/*, url = "${token.url}"*/)
public interface OAApiClient {

    // 根据wxUserId获取账户信息
    @GetMapping("/api/usr/getUsr")
    Wrapper<Map<String,Object>> getUserInfo(@RequestParam("wxUserId") String wxUserId);

    // 根据wxUserId获取账户信息
    @GetMapping("/api/usr/listAll")
    Wrapper<List<Map<String,Object>>> listAll();


    // 获取公告数据
    @GetMapping("/api/notice/get")
    Wrapper<Map<String,Object>> getNotice(@RequestParam("msgOutboxId") String msgOutboxId,@RequestParam("usrId") String usrId);

    // 获取考核数据
    @GetMapping("/api/attendance/get")
    Wrapper<Map<String,Object>> getAttendanceLog(@RequestParam("objectId") String objectId);

    // 获取考核数据
    @GetMapping("/api/attendance/agree")
    Wrapper<String> agreeAttendanceLog(@RequestParam("objectId") String objectId);
    @GetMapping("/api/attendance/disagree")
    Wrapper<String> disagreeAttendanceLog(@RequestParam("objectId") String objectId);

}
