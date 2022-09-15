package com.ydw.oa.wkflow.business_wkflow.form.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.exception.BusinessException;
import com.tmsps.fk.common.util.ChkUtil;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.base.action.BaseAction;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@RequestMapping("/form/vals")
public class FormValsController extends BaseAction {

	@Resource
	private TaskService taskService;
	@Autowired
	private IDatasService datasService;
	@Autowired
	private FormValsService formValsService;
	@Autowired
	private IFormService formService;

	@ApiOperation(value = "获取已填写的表单数量")
	@ApiImplicitParam(name = "task_id", value = "任务id")
	@GetMapping("/get_forms")
	public Wrapper<List<Map<String, Object>>> get_forms(String task_id) {
		long s = System.currentTimeMillis();
		long start = System.currentTimeMillis();
		Task task = taskService.createTaskQuery().taskId(task_id).singleResult();
		if (task == null) {
			return WrapMapper.ok();
		}

		String form_key = task.getFormKey();
		Form formPo = formService.getById(form_key);

		Datas formValsPo = formValsService.getFormValByTaskId(task_id);
		long end = System.currentTimeMillis();
		System.err.println("执行时间1-->:"+(end-start));
		start = System.currentTimeMillis();
		List<Map<String, Object>> allBeforFormsList;
		if (formValsPo != null) {
			allBeforFormsList = formValsService.getAllBeforForms(task.getProcessInstanceId(), formValsPo.getSortz(),
					formPo.getShowKey());
		} else {
			allBeforFormsList = formValsService.getAllBeforForms(task.getProcessInstanceId(), task_id,
					formPo.getShowKey());
		}
		end = System.currentTimeMillis();
		System.err.println("执行时间2-->:"+(end-start));
		long e = System.currentTimeMillis();
		System.err.println("总执行时间-->:"+(e-s));
		return WrapMapper.ok(allBeforFormsList);
	}

	@ApiOperation(value = "获取已填写的表单")
	@ApiImplicitParam(name = "form_val_kid", value = "表单提交数据对象id")
	@GetMapping("/get_json_formval")
	public Wrapper<JSONObject> get_json_formval(String form_val_kid) {
		Datas formVals = datasService.getById(form_val_kid);

		JSONObject result = new JSONObject();
		result.put("formVals", formVals);
		return WrapMapper.ok(result);
	}
	
	@ApiOperation(value = "获取已填写的表单")
	@ApiImplicitParam(name = "form_val_kid", value = "表单提交数据对象id")
	@GetMapping("/getHtmlReadonly")
	public String getHtmlReadonly(String form_val_kid) {
		Datas formVals = datasService.getById(form_val_kid);
		return formVals.getHtmlReadonly();
	}

	@ApiOperation(value = "根据pid和form_key获取已完成的表单数据")
	@GetMapping("/getData")
	public Wrapper<String> getData(String pid,String form_key) {
		QueryWrapper<Datas> datasQueryWrapper = new QueryWrapper<>();
		datasQueryWrapper.eq("ACTI_PROC_INST_ID", pid).orderByDesc("SORTZ");
		List<Datas> list = datasService.list(datasQueryWrapper);
		String data = null;
		if(ChkUtil.isNull(list) || list.size()==0){
			Datas formValsPo = formValsService.getFirstForm(pid);
			if (ChkUtil.isNotNull(formValsPo)) {
				data = formValsPo.getFormValsJson();
			}
		}else{
			data = list.get(0).getFormValsJson();
		}

		return WrapMapper.ok(data);
	}
}
