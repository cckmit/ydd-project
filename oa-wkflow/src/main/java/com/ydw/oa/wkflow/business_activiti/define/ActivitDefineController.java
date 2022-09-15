package com.ydw.oa.wkflow.business_activiti.define;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.base.action.BaseAction;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.activiti.ActivitiTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "流程定义管理")
@RestController
public class ActivitDefineController extends BaseAction {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;

	@ApiOperation(value = "流程定义列表-分页")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "名称"),
			@ApiImplicitParam(name = "category_id", value = "类型id"), @ApiImplicitParam(name = "current", value = "当前页"),
			@ApiImplicitParam(name = "size", value = "每页条数") })
	@GetMapping("/activitDefineListPage")
	public Wrapper<Map<String, Object>> activitDefineListPage(String name, String category_id, int current, int size) {
		// 创建查询对象
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		// 使用流程定义的名称模糊查询
		if (ChkUtil.isNotNull(name)) {
			processDefinitionQuery.processDefinitionNameLike("%" + name + "%");
		}
		if (ChkUtil.isNotNull(category_id)) {
			processDefinitionQuery.processDefinitionCategory(category_id);
		}
		long total = processDefinitionQuery.count();
		List<ProcessDefinition> list = processDefinitionQuery.orderByProcessDefinitionKey().asc().listPage((current - 1) * size, size);
		List<Map<String, Object>> definitions = ActivitiTools.turnProcessDefinitions(list);
		for (Map<String, Object> map : definitions) {
			Model model = repositoryService.createModelQuery().deploymentId(map.get("deploymentId")+"").singleResult();
			if(ChkUtil.isNotNull(model)) {
				map.put("modelId", model.getId());
			}else {
				map.put("modelId", "");
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("records", definitions);
		map.put("current", current);
		map.put("size", size);
		map.put("total", total);
		return WrapMapper.ok(map);
	}

	@ApiOperation(value = "流程定义列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "名称"),
			@ApiImplicitParam(name = "category_id", value = "类型id") })
	@GetMapping("/activitDefineList")
	public Wrapper<List<Map<String, Object>>> activitDefineList(String name, String category_id) {
		// 创建查询对象
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		// 使用流程定义的名称模糊查询
		if (name == null) {
			name = "";
		}
		if (ChkUtil.isNotNull(category_id)) {
			processDefinitionQuery.processDefinitionCategory(category_id);
		}
		processDefinitionQuery.orderByDeploymentId().desc();
		List<ProcessDefinition> list = processDefinitionQuery.processDefinitionNameLike("%" + name + "%").list();
		return WrapMapper.ok(ActivitiTools.turnProcessDefinitions(list));
	}

	@ApiOperation(value = "删除流程定义")
	@ApiImplicitParam(name = "deploymentId", value = "流程deploymentId")
	@GetMapping("/activiti_define_delete")
	public Wrapper<String> activiti_define_delete(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "启动流程-以流程定义id")
	@ApiImplicitParam(name = "id", value = "流程定义Id")
	@GetMapping("/activiti_define_start")
	public Wrapper<String> activiti_define_start(String id) {
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();

		ProcessInstance pi = runtimeService.startProcessInstanceById(id);
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		task.setAssignee(SessionTool.getSessionAdminId());
		taskService.saveTask(task);

		logger.info("启动流程实例,获取id-->{},实例名称-->{}", pi.getId(), pd.getName());
		return WrapMapper.ok(pi.getId());
	}

	@ApiOperation(value = "启动流程-以key")
	@ApiImplicitParam(name = "keyName", value = "key")
	@GetMapping("/start")
	public Wrapper<String> startProcess(String keyName) {
		// 在流程启动之前设置发起人
		identityService.setAuthenticatedUserId(SessionTool.getSessionAdminId());
		// 启动流程
		ProcessInstance process = runtimeService.startProcessInstanceByKey(keyName);

		// 用于开启第一个 task任务
		Task task = taskService.createTaskQuery().processInstanceId(process.getId()).singleResult();
		task.setAssignee(SessionTool.getSessionAdminId());
		taskService.saveTask(task);

		return WrapMapper.ok(process.getId() + " : " + process.getProcessDefinitionId());
	}

	@ApiOperation(value = "判断流程定义key的唯一性")
	@GetMapping("/check_define_key")
	public Wrapper<String> check_define_key(String modelId, String key) {
		Model modelData = repositoryService.getModel(modelId);
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.deploymentId(modelData.getDeploymentId()).singleResult();
		if (ChkUtil.isNotNull(pd) && pd.getKey().equals(key)) {
			return WrapMapper.ok("TRUE");
		}

		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(key);
		if (m.matches()) {
			return WrapMapper.ok("NUM");
		}
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.list();
		if (list.size() > 0) {
			return WrapMapper.ok("FALSE");
		}
		return WrapMapper.ok("TRUE");
	}
	
	@ApiOperation(value = "删除流程定义及相关工作流")
	@ApiImplicitParam(name = "deploymentId", value = "流程deploymentId")
	@GetMapping("/delete_define_and_tasks")
	public Wrapper<String> delete_define_and_tasks(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
		return WrapMapper.ok("删除成功");
	}
}
