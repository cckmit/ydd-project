package com.ydw.oa.wkflow.business_wkflow.form.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
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
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.business_wkflow.form.dto.FormJsonDto;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 获取表单数据
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Api(description = "获取表单数据")
@RestController
@RequestMapping("/form/json")
public class FormJsonController extends BaseAction {

	@Resource
	private TaskService taskService;
	@Autowired
	private IFormService formService;
	@Autowired
	private FormValsService formValsService;

	@ApiOperation(value = "获取表单")
	@ApiImplicitParam(name = "kid", value = "表单id")
	@GetMapping("/init_form")
	public Wrapper<String> init_form(String kid) {
		Form formPo = formService.getById(kid);
		if (ChkUtil.isNull(formPo.getWidgetJson())) {
			formPo.setWidgetJson(formPo.getJson());
			formService.updateById(formPo);
		}
		String jsonStr = formPo.getWidgetJson();
		JSONObject json = JsonUtil.jsonStrToJsonObject(jsonStr);
		if (json == null) {
			json = new JSONObject();
		}
		return WrapMapper.ok(json.toJSONString());
	}

	@ApiOperation(value = "保存表单")
	@PostMapping("/save")
	public Wrapper<String> save(@Valid @RequestBody FormJsonDto formJsonDto) {
		Form formPo = formService.getById(formJsonDto.getKid());
		formPo.setJson(formJsonDto.getJson());
		formPo.setWidgetJson(formJsonDto.getWidget_json());
		formService.updateById(formPo);
		return WrapMapper.ok("表单设置成功");
	}

	@ApiOperation(value = "获取表单提交数据")
	@ApiImplicitParam(name = "task_id", value = "任务id")
	@GetMapping("/get_json_new")
	public Wrapper<JSONObject> get_json_new(String task_id, String form_id) {
		Form formPo = null;
		String values = "{}";
		String procDefKey = "";
		if (ChkUtil.isNotNull(task_id) && !"null".equals(task_id)) {
			Task task = taskService.createTaskQuery().taskId(task_id).singleResult();
			String procDefId = task.getProcessDefinitionId();
			procDefKey = procDefId.split(":")[0];
			// 1. 获取表单参数
			String form_key = task.getFormKey();
			formPo = formService.getById(form_key);
			// 2.1 加载草稿
			Datas formValsPo = formValsService.getFormValByTaskId(task_id);

			// 2.2 获取上次表单提交数据 -->改为-->如果是被打回来的表单,加载上一次添加的值,用于修改
			if (formValsPo == null) {
				formValsPo = formValsService.getPreFormVals(task.getProcessInstanceId(), form_key);
			}
			if (formValsPo != null) {
				values = formValsPo.getFormValsJson();
			}
		} else {
			formPo = formService.getById(form_id);
		}

		// 3.组合
		JSONObject result = new JSONObject();
		result.put("form", formPo);
		result.put("defKey", procDefKey);
		result.put("values", values);
		return WrapMapper.ok(result);
	}

	@ApiOperation(value = "获取表单")
	@ApiImplicitParam(name = "form_key", value = "表单id")
	@GetMapping("/get_json_formkey")
	public Wrapper<JSONObject> get_json_formkey(String form_key) {
		Form formPo = formService.getById(form_key);
		// 2.获取上次表单提交数据
		Map<String, Object> variables = new HashMap<>();

		// 3.组合
		JSONObject result = new JSONObject();
		result.put("form", formPo);
		result.put("values", variables);
		return WrapMapper.ok(result);
	}

	@ApiOperation(value = "获取表单html")
	@ApiImplicitParams({ @ApiImplicitParam(name = "task_id", value = "任务id"),
		@ApiImplicitParam(name = "form_id", value = "表单id") })
	@GetMapping("/getHtml")
	public String getHtml(String task_id, String form_id) {
		if (ChkUtil.isNotNull(task_id) && !"null".equals(task_id)) {
			Task task = taskService.createTaskQuery().taskId(task_id).singleResult();
			form_id = task.getFormKey();
		}
		Form formPo = formService.getById(form_id);
		
		return formPo.getHtml();
	}

	@ApiOperation(value = "根据某个表单id获取所有table类型的表单数据")
	@ApiImplicitParam(name = "formKey", value = "表单id")
	@GetMapping("/get_table_list")
	public Wrapper<List<Form>> get_table_list(String formKey) {
		Form formPo = formService.getById(formKey);
		QueryWrapper<Form> formQueryWrapper = new QueryWrapper<>();
		formQueryWrapper.eq("MODEL_ID",formPo.getModelId()).eq("FORM_TYPE","table");
		List<Form> result = formService.list(formQueryWrapper);
		return WrapMapper.ok(result);
	}

}
