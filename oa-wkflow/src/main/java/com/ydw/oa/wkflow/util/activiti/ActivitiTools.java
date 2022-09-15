/**
 * 
 */
package com.ydw.oa.wkflow.util.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * TODO 工作流表结构转换
 * @author hxj
 *	
 */
public class ActivitiTools {

	public static List<Map<String, Object>> turnModels(List<Model> models) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Model model : models) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", model.getId());
			map.put("name", model.getName());
			map.put("key", model.getKey());
			map.put("category", model.getCategory());
			map.put("createTime", model.getCreateTime());
			map.put("lastUpdateTime", model.getLastUpdateTime());
			map.put("version", model.getVersion());
			map.put("metaInfo", model.getMetaInfo());
			map.put("deploymentId", model.getDeploymentId());
			map.put("tenanId", model.getTenantId());
			list.add(map);
		}
		return list;
	}
	
	public static List<Map<String, Object>> turnProcessDefinitions(List<ProcessDefinition> models) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (ProcessDefinition model : models) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", model.getId());
			map.put("category", model.getCategory());
			map.put("name", model.getName());
			map.put("key", model.getKey());
			map.put("description", model.getDescription());
			map.put("version", model.getVersion());
			map.put("resourceName", model.getResourceName());
			map.put("deploymentId", model.getDeploymentId());
			map.put("diagramResourceName", model.getDiagramResourceName());
			map.put("hasStartFormKey", model.hasStartFormKey());
			map.put("isGraphicalNotationDefined", model.hasGraphicalNotation());
			map.put("suspensionState", model.isSuspended());
			map.put("tenanId", model.getTenantId());
			list.add(map);
		}
		return list;
	}
	
	public static List<Map<String, Object>> turnTasks(List<Task> models) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Task model : models) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", model.getId());
			map.put("name", model.getName());
			map.put("description", model.getDescription());
			map.put("priority", model.getPriority());
			map.put("owner", model.getOwner());
			map.put("assignee", model.getAssignee());
			map.put("processInstanceId", model.getProcessInstanceId());
			map.put("executionId", model.getExecutionId());
			map.put("processDefinitionId", model.getProcessDefinitionId());
			map.put("createTime", model.getCreateTime());
			map.put("taskDefinitionKey", model.getTaskDefinitionKey());
			map.put("dueDate", model.getDueDate());
			map.put("category", model.getCategory());
			map.put("parentTaskId", model.getParentTaskId());
			map.put("tenantId", model.getTenantId());
			map.put("formKey", model.getFormKey());
			map.put("delegationState", model.getDelegationState());
			map.put("suspended", model.isSuspended());
			map.put("taskLocalVariables", model.getTaskLocalVariables());
			map.put("processVariables", model.getProcessVariables());
			list.add(map);
		}
		return list;
	}
	
	public static Map<String, Object> turnProcessInstance(ProcessInstance model) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", model.getId());
		map.put("isSuspended", model.isSuspended());
		map.put("isEnded", model.isEnded());
		map.put("activityId", model.getActivityId());
		map.put("processInstanceId", model.getProcessInstanceId());
		map.put("parentId", model.getParentId());
		map.put("superExecutionId", model.getSuperExecutionId());
		map.put("tenantId", model.getTenantId());
		map.put("name", model.getName());
		map.put("description", model.getDescription());
		map.put("processDefinitionId", model.getProcessDefinitionId());
		map.put("processDefinitionName", model.getProcessDefinitionName());
		map.put("processDefinitionKey", model.getProcessDefinitionKey());
		map.put("processDefinitionVersion", model.getProcessDefinitionVersion());
		map.put("deploymentId", model.getDeploymentId());
		map.put("businessKey", model.getBusinessKey());
		map.put("processVariables", model.getProcessVariables());
		map.put("localizedName", model.getLocalizedName());
		map.put("localizedDescription", model.getLocalizedDescription());
		return map;
	}
	
	public static List<Map<String, Object>> turnProcessInstances(List<ProcessInstance> models) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (ProcessInstance model : models) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", model.getId());
			map.put("isSuspended", model.isSuspended());
			map.put("isEnded", model.isEnded());
			map.put("activityId", model.getActivityId());
			map.put("processInstanceId", model.getProcessInstanceId());
			map.put("parentId", model.getParentId());
			map.put("superExecutionId", model.getSuperExecutionId());
			map.put("tenantId", model.getTenantId());
			map.put("name", model.getName());
			map.put("description", model.getDescription());
			map.put("processDefinitionId", model.getProcessDefinitionId());
			map.put("processDefinitionName", model.getProcessDefinitionName());
			map.put("processDefinitionKey", model.getProcessDefinitionKey());
			map.put("processDefinitionVersion", model.getProcessDefinitionVersion());
			map.put("deploymentId", model.getDeploymentId());
			map.put("businessKey", model.getBusinessKey());
			map.put("processVariables", model.getProcessVariables());
			map.put("localizedName", model.getLocalizedName());
			map.put("localizedDescription", model.getLocalizedDescription());
			list.add(map);
		}
		return list;
	}

	public static List<Map<String, Object>> turnHistoryProcessInstances(List<HistoricProcessInstance> models) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (HistoricProcessInstance model : models) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", model.getId());
			map.put("businessKey", model.getBusinessKey());
			map.put("processDefinitionId", model.getProcessDefinitionId());
			map.put("processDefinitionName", model.getProcessDefinitionName());
			map.put("processDefinitionKey", model.getProcessDefinitionKey());
			map.put("processDefinitionVersion", model.getProcessDefinitionVersion());
			map.put("deploymentId", model.getDeploymentId());
			map.put("startTime", model.getStartTime());
			map.put("endTime", model.getEndTime());
			map.put("durationInMillis", model.getDurationInMillis());
			map.put("startUserId", model.getStartUserId());
			map.put("startActivityId", model.getStartActivityId());
			map.put("deleteReason", model.getDeleteReason());
			map.put("superProcessInstanceId", model.getSuperProcessInstanceId());
			map.put("tenantId", model.getTenantId());
			map.put("name", model.getName());
			map.put("description", model.getDescription());
			map.put("processVariables", model.getProcessVariables());
			list.add(map);
		}
		return list;
	}
	
	public static List<Map<String, Object>> turnHistoricTaskInstance(List<HistoricTaskInstance> models) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (HistoricTaskInstance model : models) {
			Map<String, Object> map = new HashMap<>();
			map.put("assignee", model.getAssignee());
			map.put("category", model.getCategory());
			map.put("claimTime", model.getClaimTime());
			map.put("createTime", model.getCreateTime());
			map.put("deleteReason", model.getDeleteReason());
			map.put("description", model.getDescription());
			map.put("dueDate", model.getDueDate());
			map.put("drationInMillis", model.getDurationInMillis());
			map.put("endTime", model.getEndTime());
			map.put("executionId", model.getExecutionId());
			map.put("formKey", model.getFormKey());
			map.put("id", model.getId());
			map.put("name", model.getName());
			map.put("owner", model.getOwner());
			map.put("ParentTaskId", model.getParentTaskId());
			map.put("priority", model.getPriority());
			map.put("processDefinitionId", model.getProcessDefinitionId());
			map.put("processInstanceId", model.getProcessInstanceId());
			map.put("processVariables", model.getProcessVariables());
			map.put("startTime", model.getStartTime());
			map.put("taskDefinitionKey", model.getTaskDefinitionKey());
			map.put("taskLocalVariables", model.getTaskLocalVariables());
			list.add(map);
		}
		return list;
	}
}
