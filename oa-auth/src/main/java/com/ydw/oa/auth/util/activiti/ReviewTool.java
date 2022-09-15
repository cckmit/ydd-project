package com.ydw.oa.auth.util.activiti;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.msg.entity.MsgOutbox;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.mapper.AuUsrMapper;
import com.ydw.oa.auth.business_wx.WxFeignService;
import com.ydw.oa.auth.util.DateTools;
import com.ydw.oa.auth.util.SpringContextUtil;
import com.ydw.oa.auth.util.sms.SmsAliTools;

import java.util.List;
import java.util.Map;

public class ReviewTool {



	public static void attendanceApprove(String usrs, String yearMonth,String deptId,String deptName) {
		AuUsrMapper auUsrMapper = SpringContextUtil.getBean(AuUsrMapper.class);
		List<String> wxUsrIds = auUsrMapper.getWxUsrIds(usrs);
		String touser = String.join("|", wxUsrIds);
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		obj.put("title", "考核信息确认");
		obj.put("description", "<div class=\"gray\">" + DateTools.getTodayCn() + "</div>尊敬的领导您好，您有"+deptName+"员工考核信息（"+yearMonth+"）待确认");
		JSONObject params = new JSONObject();
		params.put("deptId", deptId);
		params.put("month", yearMonth);
		obj.put("params", params);
		WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
		String resp = wxFeignService.textcard(obj);
		System.err.println("微信端调用-->"+resp);

		// 短信通知
		for (String usrId : usrs.split(",")) {
			AuUsr auUsr = auUsrMapper.selectById(usrId);
			SmsAliTools.reviewInfo(auUsr.getMobile(), deptName,yearMonth);
		}
	}

	public static void notice(String touser, String userName,MsgOutbox msgOutbox,String needReply) {
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		obj.put("title", "公告通知");
		obj.put("description", "<div class=\"gray\">".concat(DateTools.getTodayCn()).concat("</div> ")
		.concat("<div class=\"normal\">").concat("标题：").concat(msgOutbox.getTitle()).concat("</div>")
		.concat("<div class=\"normal\">").concat("类型：").concat(msgOutbox.getNoticeType()).concat("</div>")
		.concat("<div class=\"normal\">").concat("发布人：").concat(userName).concat("</div>")
		.concat("<div class=\"highlight\">").concat(needReply).concat("</div>")
		);
		JSONObject params = new JSONObject();
		params.put("msgOutboxId", msgOutbox.getObjectId());
		obj.put("params", params);
		WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
		String resp = wxFeignService.textcard_notice(obj);
		System.err.println("微信端调用-->"+resp);

//		// 短信通知
//		for (String usrId : usrs.split(",")) {
//			AuUsr auUsr = auUsrMapper.selectById(usrId);
//			SmsAliTools.reviewInfo(auUsr.getMobile(), "员工考核信息确认");
//		}
	}


}
