package com.ydw.oa.wkflow.business_wkflow.task.service;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 待办任务注入类，动态修改 Task的assignee等
 * 
 * @author 冯晓东
 *
 */
@Service
public class TaskInjectService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;

	// 动态注入变量参数
	@SuppressWarnings("unused")
	public void injectDynamicData(Task task) {
		String processInstanceId = task.getProcessInstanceId();
		String executionId = task.getExecutionId();
		// 获取流程实例发起人
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		String startUserId = processInstance.getStartUserId();
		// 存办理人
		String handleper;
		try {

			handleper = runtimeService.getVariable(executionId, "handleper") + "";
			handleper = "null".equals(handleper) ? "" : handleper;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			// 任务已经结束，此时返回
			return;
		}

	}
}
