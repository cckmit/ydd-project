package com.ydw.oa.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.tmsps.fk.common.base.exception.BusinessException;
import com.ydw.oa.auth.util.SessionTool;

/**
 * 登陆校验
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
@Component
public class LoginAdminInterceptor implements HandlerInterceptor {

//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//
//		String adminId = SessionTool.getSessionAdminId();
//		if (adminId == null) {
//			throw new BusinessException(4001, "登录超时");
//		}
//
//		return true;
//	}

}
