package com.ydw.oa.wkflow.business_activiti.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_activiti.service.TaskRollBackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "任务回退")
@RestController
public class TaskRollBackController {

	@Autowired
	private TaskRollBackService taskRollBackService;

	@ApiOperation(value = "代办任务回退")
	@ApiImplicitParams({ @ApiImplicitParam(name = "currentTaskId", value = "当前任务id"),
			@ApiImplicitParam(name = "backToTaskId", value = "回退到任务id") })
	@GetMapping("/rollBack")
	public Wrapper<String> dbBackTo(String currentTaskId, String backToTaskId) throws Exception {

		String result = taskRollBackService.rollBack(currentTaskId, backToTaskId);

		return WrapMapper.ok(result);

	}

}
