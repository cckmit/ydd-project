package com.ydw.oa.wkflow.util.activiti;

import java.util.List;
import java.util.Map;

import com.tmsps.fk.common.util.ChkUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_wx.WxFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.date.DateTools;
import com.ydw.oa.wkflow.util.sms.SmsAliTools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

@Slf4j
public class ReviewTool {

	public static void notice(String usrs, DelegateTask delegateTask,String usrName,String usrDept) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		Wrapper<List<String>> wxUsrIds = authFeignService.getWxUsrIds(usrs);
		String touser = String.join("|", wxUsrIds.getResult());
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		obj.put("title", "审批通知");
		RepositoryService repositoryService = SpringContextUtil.getBean(RepositoryService.class);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(delegateTask.getProcessDefinitionId()).singleResult();
		obj.put("description", "<div class=\"gray\">".concat(DateTools.getTodayCn()).concat("</div>您好，您有来自").concat(usrDept)+"办公人员".concat(usrName).concat("申请的审批任务（")
				+ processDefinition.getName() + ")待审批！请及时登录流程管控系统或通过企业微信进行办理");
		JSONObject params = new JSONObject();
		params.put("taskId", delegateTask.getId());
		obj.put("params", params);
		WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
		String resp = wxFeignService.textcard(obj);
		// 短信通知
		log.info("=======================审批=================================");
//		for (String usrId : usrs.split(",")) {
//			log.info("usrId-->",usrId);
//			Map<String, Object> one = authFeignService.getOne(usrId).getResult();
//			if (ChkUtil.isNotNull(one)) {
//				if(ChkUtil.isNotNull(one.get("MOBILE"))){
//					SmsAliTools.reviewInfo(MapUtils.getString(one,"MOBILE"), processDefinition.getName(),usrName,usrDept);
//				}
//			}
//		}
	}

	public static void notice2(String usrs, DelegateTask delegateTask) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		Wrapper<List<String>> wxUsrIds = authFeignService.getWxUsrIds(usrs);
		String touser = String.join("|", wxUsrIds.getResult());
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		obj.put("title", "签字确认通知");
		RepositoryService repositoryService = SpringContextUtil.getBean(RepositoryService.class);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(delegateTask.getProcessDefinitionId()).singleResult();
		obj.put("description", "<div class=\"gray\">" + DateTools.getTodayCn() + "</div>您好，您有("
				+ processDefinition.getName() + ")待签字确认");
		JSONObject params = new JSONObject();
		params.put("taskId", delegateTask.getId());
		obj.put("params", params);
		WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
		String resp = wxFeignService.textcard(obj);
	}
	
	public static void reject(String usrs, Task task) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		Wrapper<List<String>> wxUsrIds = authFeignService.getWxUsrIds(usrs);
		String touser = String.join("|", wxUsrIds.getResult());
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		obj.put("title", "审批驳回通知");
		RepositoryService repositoryService = SpringContextUtil.getBean(RepositoryService.class);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(task.getProcessDefinitionId()).singleResult();
		obj.put("description", "<div class=\"gray\">" + DateTools.getTodayCn() + "</div>您好，您申请的办公任务("
				+ processDefinition.getName() + ")已被驳回！请及时登录流程管控系统查看");
		JSONObject params = new JSONObject();
		params.put("taskId", task.getId());
		obj.put("params", params);
		WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
		String resp = wxFeignService.textcard(obj);
		// 短信通知
		log.info("=======================驳回=================================");
//		for (String usrId : usrs.split(",")) {
//			Map<String, Object> one = authFeignService.getOne(usrId).getResult();
//			if(ChkUtil.isNotNull(one.get("MOBILE"))){
//				SmsAliTools.rejectInfo(one.get("MOBILE").toString(), processDefinition.getName());
//			}
//		}
	}
	
	public static void todo(String usrs, DelegateTask delegateTask,String usrName,String usrDept) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		Wrapper<List<String>> wxUsrIds = authFeignService.getWxUsrIds(usrs);
		String touser = String.join("|", wxUsrIds.getResult());
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		obj.put("title", "待办通知");
		RepositoryService repositoryService = SpringContextUtil.getBean(RepositoryService.class);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(delegateTask.getProcessDefinitionId()).singleResult();
		obj.put("description", "<div class=\"gray\">".concat(DateTools.getTodayCn()).concat("</div>您好，您有来自").concat(usrDept)+"办公人员".concat(usrName).concat("申请的审批任务（")
				+ processDefinition.getName() + ")待办理！请及时登录流程管控系统进行办理");
		JSONObject params = new JSONObject();
		params.put("taskId", delegateTask.getId());
		obj.put("params", params);
		WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
		String resp = wxFeignService.textcard(obj);
	}
	
	public static void finish(String usrs, String processInstanceName) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		Wrapper<List<String>> wxUsrIds = authFeignService.getWxUsrIds(usrs);
		String touser = String.join("|", wxUsrIds.getResult());
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		obj.put("title", "受理通知");
		obj.put("description", "<div class=\"gray\">" + DateTools.getTodayCn() + "</div>您好，您申请的办公任务("
				+ processInstanceName + ")已成功受理！请及时登录流程管控系统查看");
		JSONObject params = new JSONObject();
		params.put("taskId", "");
		obj.put("params", params);
		WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
		String resp = wxFeignService.textcard(obj);
	}

}
