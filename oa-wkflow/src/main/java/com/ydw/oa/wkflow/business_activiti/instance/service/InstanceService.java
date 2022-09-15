package com.ydw.oa.wkflow.business_activiti.instance.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstanceService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private HistoryService historyService;

	public byte[] getProcessImage(String processInstanceId) throws Exception {
		// TODO 获取流程图像，已执行节点和流程线高亮显示
		// 获取历史流程实例
		HistoricProcessInstance historicProcessInstance = queryHistoricProcessInstance(processInstanceId);
		if (historicProcessInstance == null) {
			throw new Exception();
		} else {
			// 获取流程定义
			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
					.getProcessDefinition(historicProcessInstance.getProcessDefinitionId());

			// 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
			List<HistoricActivityInstance> historicActivityInstanceList = historyService
					.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
					.orderByHistoricActivityInstanceId().asc().list();

			// 已执行的节点ID集合
			List<String> executedActivityIdList = new ArrayList<String>();
			int index = 1;
			logger.info("获取已经执行的节点ID");
			for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
				executedActivityIdList.add(activityInstance.getActivityId());
				logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : "
						+ activityInstance.getActivityName());
				index++;
			}
			// 获取流程图图像字符流
			BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
			DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
			InputStream imageStream = generator.generateDiagram(bpmnModel, "png", executedActivityIdList,
					new ArrayList<String>(), "宋体", "宋体", "宋体", null, 1.0);
			byte[] buffer = new byte[imageStream.available()];
			imageStream.read(buffer);
			imageStream.close();
			return buffer;
		}
	}

	private HistoricProcessInstance queryHistoricProcessInstance(String processInstanceId) {
		// TODO 通过流程id查询流程
		return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}
}
