package com.ydw.oa.wkflow.business_activiti.instance;

import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.util.activiti.ActivitiTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "流程实例管理")
@RestController
public class ActivitiInstanceController {

	@Autowired
	private RuntimeService runtimeService;

	@ApiOperation(value = "流程实例列表")
	@ApiImplicitParam(name = "name", value = "名称")
	@GetMapping("/activitiInstanceList")
	public List<Map<String, Object>> activitiInstanceList(String name) {
		// 创建查询对象
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		if (name != null && !"".equals(name)) {
			processInstanceQuery.processInstanceNameLike("%" + name + "%");
		}
		processInstanceQuery.orderByProcessInstanceId().desc();
		
		List<ProcessInstance> list = processInstanceQuery.list();
		return ActivitiTools.turnProcessInstances(list);
	}

	@ApiOperation(value = "流程挂起")
	@ApiImplicitParam(name = "processInstanceId", value = "流程实例id")
	@GetMapping("/instance/suspend")
	public Wrapper<String> suspend(String processInstanceId) {
		runtimeService.suspendProcessInstanceById(processInstanceId);
		return WrapMapper.ok("挂起成功");
	}

	@ApiOperation(value = "流程重新激活")
	@ApiImplicitParam(name = "processInstanceId", value = "流程实例id")
	@GetMapping("/instance/gorun")
	public Wrapper<String> gorun(String processInstanceId) {
		runtimeService.activateProcessInstanceById(processInstanceId);
		return WrapMapper.ok("重新激活成功");
	}
	
	@ApiOperation(value = "判断流程是否存在")
	@GetMapping("/instance/isHave")
	public Wrapper<Boolean> isHave(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		if (ChkUtil.isNull(processInstance)) {
			return WrapMapper.ok(false);
		}
		return WrapMapper.ok(true);
	}
	
	@ApiOperation(value = "结束流程")
	@GetMapping("/instance/delete")
	public Wrapper<Boolean> delete(String processInstanceId) {
		runtimeService.deleteProcessInstance(processInstanceId, null);
		return WrapMapper.ok(true);
	}
}
