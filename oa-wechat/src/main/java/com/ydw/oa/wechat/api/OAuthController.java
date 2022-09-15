package com.ydw.oa.wechat.api;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wechat.client.OAApiClient;
import com.ydw.oa.wechat.client.WechatApiProxyClient;
import com.ydw.oa.wechat.config.WxCorpConfig;
import com.ydw.oa.wechat.entity.wechat.WxCorpOAuthResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

    @Autowired
    private WechatApiProxyClient wechatApiProxyClient;
    @Autowired
    private OAApiClient oaApiClient;

    @GetMapping("/user")
    public Wrapper<Map<String,Object>> user(String code,String agentId) {
        WxCorpOAuthResult wxUserInfo = wechatApiProxyClient.getUserInfo(agentId, code);

        if (wxUserInfo.getErrcode() == 40029) {
            return WrapMapper.wrap(40001, "无效的code码");
        }

        if (wxUserInfo.getErrcode() != 0) {
            return WrapMapper.error("获取企业微信用户信息失败");
        }

        String wxUserId = wxUserInfo.getUserId();
        if (wxUserId == null) {
            return WrapMapper.wrap(40002, "该用户不是企业微信用户");
        }
        //通过企业微信userId换取OA用户
        Wrapper<Map<String, Object>> userInfo = oaApiClient.getUserInfo(wxUserId);
        if (userInfo.getCode() != 200) {
            return WrapMapper.error("获取账户信息失败");
        }
        Map<String, Object> result = userInfo.getResult();
        if (MapUtil.isEmpty(result)) {
            return WrapMapper.wrap(40003, "该用户未绑定企业微信");
        }
        return WrapMapper.ok(result);
    }

    @GetMapping("/attendance_log")
    public Wrapper<Map<String,Object>> attendance_log(String attendanceLogId) {
        Wrapper<Map<String, Object>> attendanceLog = oaApiClient.getAttendanceLog(attendanceLogId);
        Map<String, Object> result = attendanceLog.getResult();
        String statuz = MapUtil.getStr(result, "statuz");
        if (!"待审核".equals(statuz)) {
            return WrapMapper.wrap(5001, "该任务已审核");
        }
        return attendanceLog;
    }

    @GetMapping("/notice")
    public Wrapper<Map<String,Object>> notice(String msgOutboxId, String usrId) {
        Wrapper<Map<String, Object>> notice = oaApiClient.getNotice(msgOutboxId,usrId);
        return notice;
    }

    @GetMapping("/attendance_log_agree")
    public Wrapper<String> attendance_log_agree(String attendanceLogId) {
        return oaApiClient.agreeAttendanceLog(attendanceLogId);
    }

    @GetMapping("/attendance_log_disagree")
    public Wrapper<String> attendance_log_disagree(String attendanceLogId) {
        return oaApiClient.disagreeAttendanceLog(attendanceLogId);
    }


}
