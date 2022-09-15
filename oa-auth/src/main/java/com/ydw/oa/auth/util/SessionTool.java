package com.ydw.oa.auth.util;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.CookieUtil;
import com.tmsps.fk.common.util.DesUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.auth.config.WebConfig;

/**
 * Session业务相关工具类
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
public class SessionTool {

	// Session 分割符
	private static final String SPLIT = ":";
	// 短信验证码
	public static final String CODE = "SMS_CODE";

	/**
	 * 设置 or 取消设置 登录key
	 * 
	 * @param sessionKey
	 */
	public static void setSessionAdminLoginKey(String userJson) {
		String sessionKey = DesUtil.encrypt(WebConfig.ADMINSESSION + SPLIT + userJson, WebConfig.DESKEY);
		CookieUtil.setCookie(WebUtil.getReponse(), WebConfig.ADMINSESSION, sessionKey, 1 * 24 * 60 * 60);
	}
	
	public static JSONObject getSessionAdmin() {
		String sessionKey = CookieUtil.getCookie(WebUtil.getRequest(), WebConfig.ADMINSESSION);
		if (ChkUtil.isNull(sessionKey)) {
			return null;
		} else {
			String key = DesUtil.decrypt(sessionKey, WebConfig.DESKEY);
			if (!key.startsWith(WebConfig.ADMINSESSION + SPLIT)) {
				return null;
			}
			String userJson = key.substring((WebConfig.ADMINSESSION + SPLIT).length());
			JSONObject user = JsonUtil.jsonStrToJsonObject(userJson);
			return user;
		}
	}

	public static String getSessionAdminId() {
		JSONObject userJSON = getSessionAdmin();
		if(ChkUtil.isNotNull(userJSON)) {
			return userJSON.getString("objectId");
		}
		return null;
	}
}
