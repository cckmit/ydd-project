package com.ydw.oa.auth.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * web层相关工具类
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
public class WebUtil {

	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	public static HttpServletResponse getReponse() {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
		return response;
	}

	public static HttpSession getSession() {
		HttpSession sn = WebUtil.getRequest().getSession();
		return sn;
	}

	public static ServletContext getServletContext() {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		if (webApplicationContext == null) {
			return null;
		}
		ServletContext servletContext = webApplicationContext.getServletContext();
		return servletContext;
	}

	/**
	 * 同步获取token值并移除
	 * 
	 * @param string
	 * @return
	 */
	public static synchronized String getAsyncToken(String key) {
		HttpSession sn = WebUtil.getRequest().getSession();
		String val = (String) sn.getAttribute(key);
		sn.removeAttribute(key);
		return val;
	}

}
