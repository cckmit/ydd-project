package com.ydw.oa.wkflow.business_activiti.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.base.action.BaseAction;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_activiti.dto.TaskCommonQuery;
import com.ydw.oa.wkflow.business_activiti.service.TaskTodoService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.activiti.ActivitiTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "待办任务管理")
@RestController
public class TodoTaskController extends BaseAction {

	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private TaskTodoService taskTodoService;
	@Autowired
	private IFormService formService;

	@ApiOperation(value = "我的待办任务列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "名称"),
			@ApiImplicitParam(name = "current", value = "当前页"), @ApiImplicitParam(name = "size", value = "每页条数") })
	@PostMapping("/todoTaskList")
	public Wrapper<Map<String, Object>> todoTaskList(@RequestBody TaskCommonQuery taskCommonQuery) {
		// 创建查询对象
		NativeTaskQuery nativeTaskQuery = taskTodoService.createSql(taskCommonQuery);
		int current = taskCommonQuery.getCurrent();
		int size = taskCommonQuery.getSize();

		long total = nativeTaskQuery.list().size();
		if (ChkUtil.isNotNull(taskCommonQuery.getName()) || ChkUtil.isNotNull(taskCommonQuery.getStartTime())
				|| ChkUtil.isNotNull(taskCommonQuery.getEndTime())) {
			current = 1;
		}
		// 获取查询列表
		List<Task> list = nativeTaskQuery.listPage((current - 1) * size, size);
		List<Map<String, Object>> listEnd = ActivitiTools.turnTasks(list);
		for (Map<String, Object> map : listEnd) {
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(map.get("processDefinitionId")+"").singleResult();
			map.put("processInstanceName", processDefinition.getName());
			Form form = formService.getById(map.get("formKey")+"");
			map.put("formType", form.getFormType());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("records", listEnd);
		map.put("current", current);
		map.put("size", size);
		map.put("total", total);
		return WrapMapper.ok(map);
	}

	@ApiOperation(value = "改派任务")
	@ApiImplicitParams({ @ApiImplicitParam(name = "taskId", value = "任务id"),
			@ApiImplicitParam(name = "assignee", value = "改派人员") })
	@GetMapping("/changeAssignee")
	public Wrapper<String> run(String taskId, String assignee) {
		taskService.setAssignee(taskId, assignee);

		logger.info("task {} find " + taskId);
		return WrapMapper.ok("改派成功");
	}

	@ApiOperation(value = "完成任务", notes = "流程单个节点任务，直接完成任务")
	@ApiImplicitParam(name = "taskId", value = "任务id")
	@GetMapping("/completeTask")
	public Wrapper<String> completeTask(String taskId) {
		logger.info("taskId--> " + taskId);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (ChkUtil.isNull(task.getAssignee())) {
			taskService.claim(taskId, SessionTool.getSessionAdminId());
		}
		taskService.complete(taskId);

		return WrapMapper.ok("提交成功");
	}

	@ApiOperation(value = "提交完成任务", notes = "流程单个节点任务，直接完成任务")
	@ApiImplicitParam(name = "processInstanceId", value = "流程实例id")
	@GetMapping("/run")
	public Wrapper<String> run(String processInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		if (ChkUtil.isNull(task.getAssignee())) {
			taskService.claim(task.getId(), SessionTool.getSessionAdminId());
		}
		// 设置扩展json属性
		task.getDescription();

		logger.info("task {} find " + task.getId());
		taskService.complete(task.getId());
		return WrapMapper.ok("提交成功");
	}

	@ApiOperation(value = "认领并完成任务", notes = "审批结果为审批拒绝，则结束流程")
	@ApiImplicitParams({ @ApiImplicitParam(name = "taskId", value = "任务id"),
			@ApiImplicitParam(name = "result", value = "审批结果") })
	@GetMapping("/claimAndComplete")
	public Wrapper<ProcessInstance> claimAndComplete(String taskId, String result) {
		// 创建查询对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String pid = task.getProcessInstanceId();
		if (ChkUtil.isNull(task.getAssignee())) {
			taskService.claim(taskId, SessionTool.getSessionAdminId());
		}
		taskService.complete(taskId);
		if ("审批拒绝".equals(result)) {
			runtimeService.deleteProcessInstance(pid, "审批拒绝");
		}
		// 获取流程实例
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(pid).singleResult();
		return WrapMapper.ok(pi);
	}
}