package com.ydw.oa.auth.business.wxapi;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.base.exception.BusinessException;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.ne4Weixin.utils.HttpClient;
import com.ydw.oa.auth.util.DateTools;

@Component
public class QyWeixinTool {

	@Value("${auth.corpid}")
	private static String corpid;
	@Value("${auth.corpsecret}")
	private static String corpsecret;
	@Value("${auth.department_id}")
	private static String department_id;

	private static String access_token;

//	static {
//		access_token = getToken();
//	}

	public static String getAccessToken() {
		// 获取access_token
		if (ChkUtil.isNull(access_token)) {
			access_token = getToken();
		}
		return access_token;
	}

	public static JSONArray getDepartmentUserList() {
		// TODO 获取部门成员
		Map<String, String> params = new HashMap<>();
		params.put("access_token", QyWeixinTool.getAccessToken());
		params.put("department_id", department_id);
		params.put("fetch_child", 1 + "");
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list";
		String resp = HttpClient.httpGet(url, params);
		JSONObject jsonObject = JSONObject.parseObject(resp);
		if (jsonObject.getInteger("errcode") != 0) {
			throw new BusinessException(jsonObject.getString("errmsg"));
		}
		return jsonObject.getJSONArray("userlist");
	}

	public static JSONArray getCheckInDataList(String starttime, String endtime, List<String> usrList) {
		// TODO 获取打卡数据
		Timestamp endTime = DateTools.strToDatestamp(endtime);
		Timestamp startTime = null;
		if (ChkUtil.isNull(starttime)) {
			startTime = DateTools.addDay(endTime, -1);
		} else {
			startTime = DateTools.strToDatestamp(starttime);
		}
		Map<String, String> params = new HashMap<>();
		params.put("access_token", QyWeixinTool.getAccessToken());
		params.put("opencheckindatatype", 3 + "");
		params.put("starttime", startTime.getTime() + "");
		params.put("endtime", endTime.getTime() + "");
		params.put("useridlist", JsonUtil.toJson(usrList));
		String url = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata";
		String resp = HttpClient.httpPost(url, params);
		JSONObject jsonObject = JSONObject.parseObject(resp);
		if (jsonObject.getInteger("errcode") != 0) {
			throw new BusinessException(jsonObject.getString("errmsg"));
		}
		return jsonObject.getJSONArray("checkindata");
	}
	
	public static JSONArray sendMessageInfo(String userId, String agentid, String content) {
		// TODO 发送应用信息
		Map<String, String> params = new HashMap<>();
		params.put("access_token", QyWeixinTool.getAccessToken());
		params.put("touser", userId);
		params.put("msgtype", "text");
		params.put("agentid", agentid);
		Map<String, String> text = new HashMap<>();
		text.put("content", content);
		params.put("text", JsonUtil.toJson(text));
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list";
		String resp = HttpClient.httpGet(url, params);
		JSONObject jsonObject = JSONObject.parseObject(resp);
		if (jsonObject.getInteger("errcode") != 0) {
			throw new BusinessException(jsonObject.getString("errmsg"));
		}
		return jsonObject.getJSONArray("userlist");
	}

	private static String getToken() {
		Map<String, String> params = new HashMap<>();
		params.put("corpid", corpid);
		params.put("corpsecret", corpsecret);
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
		String resp = HttpClient.httpGet(url, params);
		JSONObject jsonObject = JSONObject.parseObject(resp);
		if (jsonObject.getInteger("errcode") != 0) {
			throw new BusinessException(jsonObject.getString("errmsg"));
		}
		return jsonObject.getString("access_token");
	}
	
	public static String getUserId(String code) {
		// 网页授权登录，获取用户身份
		Map<String, String> params = new HashMap<>();
		params.put("access_token", QyWeixinTool.getAccessToken());
		params.put("code", code);
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";
		String resp = HttpClient.httpGet(url, params);
		JSONObject jsonObject = JSONObject.parseObject(resp);
		if (jsonObject.getInteger("errcode") != 0) {
			throw new BusinessException(jsonObject.getString("errmsg"));
		}
		return jsonObject.getString("UserId");
	}

}
