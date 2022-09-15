package com.ydw.oa.wkflow.business_activiti.task;

import com.tmsps.fk.common.base.action.BaseAction;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_activiti.dto.TaskCommonQuery;
import com.ydw.oa.wkflow.business_activiti.service.AllTaskTodoService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.util.activiti.ActivitiTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RepositoryService;

import org.activiti.engine.repository.ProcessDefinition;

import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "所有待办任务管理")
@RestController
public class AllTodoTaskController extends BaseAction {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private AllTaskTodoService allTaskTodoService;
	@Autowired
	private IFormService formService;

	@ApiOperation(value = "我的所有待办任务列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "名称"),
			@ApiImplicitParam(name = "current", value = "当前页"), @ApiImplicitParam(name = "size", value = "每页条数") })
	@PostMapping("/alltodoTaskList")
	public Wrapper<Map<String, Object>> alltodoTaskList(@RequestBody TaskCommonQuery taskCommonQuery) {
		// 创建查询对象
		NativeTaskQuery nativeTaskQuery = allTaskTodoService.createSql(taskCommonQuery);
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



}