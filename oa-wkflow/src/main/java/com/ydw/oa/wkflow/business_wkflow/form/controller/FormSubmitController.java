package com.ydw.oa.wkflow.business_wkflow.form.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.base.action.BaseAction;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_wkflow.form.dto.FormSubmitDto;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Api(description = "表单提交处理")
@RestController
@RequestMapping("/form/workflow")
public class FormSubmitController extends BaseAction {

	@Resource
	private TaskService taskService;
	@Autowired
	private FormValsService formValsService;

	@ApiOperation(value = "保存草稿")
	@PostMapping("/saveDoc")
	public Wrapper<String> saveDoc(@Valid @RequestBody FormSubmitDto formSubmitDto) {
		String form = formSubmitDto.getForm();

		logger.info("表单保存草稿...");
		JSONObject formJson = JsonUtil.jsonStrToJsonObject(form);
		logger.info("formJson--{}", formJson);
		// 保存表单以及表单的值
		formValsService.saveFormVals(formSubmitDto, null);

		logger.info("表单保存草稿成功!");
		return WrapMapper.ok("保存成功!");
	}

	@ApiOperation(value = "提交")
	@PostMapping("/submit")
	public Wrapper<String> submit(@Valid @RequestBody FormSubmitDto formSubmitDto) {
		String result = "";
		if (ChkUtil.isNotNull(formSubmitDto.getTask_id()) && !"null".equals(formSubmitDto.getTask_id())) {
			// 通过任务进入任务页面提交
			result = formValsService.taskSubmit(formSubmitDto);
		}else {
			// 直接通过菜单进入任务页面提交
			result = formValsService.startTask(formSubmitDto);
		}
		return WrapMapper.ok(result);
	}
	
	@Deprecated
	@ApiOperation(value = "驳回提交")
	@GetMapping("/reject")
	public Wrapper<String> rejectSubmit(String task_id, String reason, String current_usr_id) {
		formValsService.taskRejectSubmit(task_id, reason, current_usr_id);
		return WrapMapper.ok();
	}
	
}
