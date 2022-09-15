package com.ydw.oa.wkflow.util.token;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import com.tmsps.fk.common.base.exception.BusinessException;
import com.ydw.oa.wkflow.util.WebUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 重复提交aop
 * 
 * @author 冯晓东
 */
@Aspect
@Component
public class TokenAspect {

	private static final Logger logger = LoggerFactory.getLogger(TokenAspect.class);

	/**
	 * @param jp
	 * 
	 *           经测试,会按照单个浏览器,并行执行. 无需担心同步问题.
	 */
	@Before("@annotation(com.tmsps.fk.common.token.TokenCheck)")
	public void before(JoinPoint jp) throws Throwable {
		String token = WebUtil.getRequest().getParameter("token");
		logger.info("token --> {}", token);
		if (token == null || "".equals(token.trim())) {
			throw new BusinessException("500:Parameter <token> can not be null.");
		}
		if (!token.contains("@@")) {
			throw new BusinessException("500:Parameter <token> is invalid key.");
		}

		String key = token.split("@@")[0];
		String snToken = WebUtil.getAsyncToken("token@@" + key);
		
		logger.info("session token --> {}", snToken);
		if (!token.equals(snToken)) {
			throw new BusinessException("500:Token is invalid.");
		}

	}

}
