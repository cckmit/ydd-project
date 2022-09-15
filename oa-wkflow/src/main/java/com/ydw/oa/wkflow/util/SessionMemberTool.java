package com.ydw.oa.wkflow.util;

import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.CookieUtil;
import com.tmsps.fk.common.util.DesUtil;
import com.ydw.oa.wkflow.config.WebConfig;

/**
 * Session业务相关工具类
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
public class SessionMemberTool {

	// Session 分割符
	private static final String SPLIT = ":";

	/**
	 * 设置 or 取消设置 登录key
	 *
	 * 加密 memberId
	 * 
	 * @param sessionKey
	 * @return
	 */
	public static String setSessionMemberLoginKey(String memberId) {
		String sessionKey = DesUtil.encrypt(WebConfig.MEMBERSESSION + SPLIT + memberId, WebConfig.DESKEY);
		CookieUtil.setCookie(WebUtil.getReponse(), WebConfig.MEMBERSESSION, sessionKey, 10 * 365 * 24 * 60 * 60);
		return sessionKey;
	}

	/**
	 * 解密 memberId
	 * 
	 * @param loginKey
	 * @return
	 */
	public static String getMemberIdFromKey(String loginKey) {
		if (ChkUtil.isNull(loginKey) || "null".equals(loginKey)) {
			return null;
		}
		String key = DesUtil.decrypt(loginKey, WebConfig.DESKEY);
		if (key == null || !key.startsWith(WebConfig.MEMBERSESSION + SPLIT)) {
			return null;
		}
		String memberId = key.substring((WebConfig.MEMBERSESSION + SPLIT).length());
		return memberId;
	}

	public static void setSessionMemberId(String memberId) {
		WebUtil.getSession().setAttribute("MEMBER_ID", memberId);
	}

	public static String getSessionMemberId() {
		return (String) WebUtil.getSession().getAttribute("MEMBER_ID");
	}
}
