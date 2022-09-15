package com.ydw.oa.auth.config;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.log.entity.OperateLog;
import com.ydw.oa.auth.business.log.service.IOperateLogService;
import com.ydw.oa.auth.util.SessionTool;

/**
 * 记录用户操作
 * 
 * @author wsf
 *
 */
@Aspect
@Component
public class WebLogAspect {

	@Autowired
	private IOperateLogService operateLogService;

	// 新增文件日志
	@AfterReturning(value = "execution(public * com.ydw.oa.auth.business.*.*(..))", argNames = "rtv", returning = "rtv")
	public void logBeforeController(JoinPoint joinPoint, Wrapper<List<Map<String, Object>>> rtv) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		UserOpera oper = method.getAnnotation(UserOpera.class);
		try {
			if (ChkUtil.isNotNull(oper.value())) {
				JSONObject admin = SessionTool.getSessionAdmin();
				// 添加日志
				OperateLog log = new OperateLog();
				log.setOperate(oper.value());
				log.setType(oper.type());
				log.setUsrId(admin.getString("objectId"));
				log.setUsrName(admin.getString("usrName"));
				operateLogService.save(log);
			}

		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
		}

	}
}
