package com.ydw.oa.wkflow.business_wkflow.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.exception.BusinessException;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.date.DateTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "获取任务填写信息")
@RestController
@RequestMapping("/api/task")
public class TaskApiController {

	@Autowired
	private TaskService taskService;
	@Autowired
	private IFormService formService;
	@Autowired
	private FormValsService formValsService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private IDatasService datasService;
	@Autowired
	private AuthFeignService authFeignService;

	@ApiOperation(value = "流程中表单数据及类型")
	@GetMapping("/data")
	public Wrapper<JSONObject> data(String task_id, String pid) {
		JSONObject result = new JSONObject();
		String procDefId = "";
		if (ChkUtil.isNotNull(task_id)) {
			Task task = taskService.createTaskQuery().taskId(task_id).singleResult();
			if (ChkUtil.isNull(task)) {
				throw new BusinessException(5001, "该任务已完成");
			}
			String form_key = task.getFormKey();
			Form formPo = formService.getById(form_key);
			Datas formValsPo = formValsService.getFormValByTaskId(task_id);
			List<Map<String, Object>> allBeforFormsList = new ArrayList<>();
			if (formValsPo != null) {
				allBeforFormsList = formValsService.getAllBeforForms(task.getProcessInstanceId(), formValsPo.getSortz(),
						formPo.getShowKey());
			} else {
				allBeforFormsList = formValsService.getAllBeforForms(task.getProcessInstanceId(), task_id,
						formPo.getShowKey());
			}
			for (Map<String, Object> map : allBeforFormsList) {
				JSONObject object = JsonUtil.jsonStrToJsonObject(map.get("FORM_VALS_JSON") + "");
				result.putAll(object);
			}
			procDefId = task.getProcessDefinitionId();
			pid = task.getProcessInstanceId();
			result.put("taskName", task.getName());
			result.put("formId", form_key);
		} else if (ChkUtil.isNotNull(pid)) {
			HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(pid).singleResult();
			QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("ACTI_PROC_INST_ID", pid);
			List<Map<String, Object>> allBeforFormsList = datasService.listMaps(queryWrapper);
			for (Map<String, Object> map : allBeforFormsList) {
				JSONObject object = JsonUtil.jsonStrToJsonObject(map.get("FORM_VALS_JSON") + "");
				result.putAll(object);
			}
			procDefId = processInstance.getProcessDefinitionId();
			String procDefName = processInstance.getProcessDefinitionName();

			result.put("procName", procDefName);
		}
		String procDefKey = procDefId.split(":")[0];
		result.put("procDefKey", procDefKey);
		result.put("pid", pid);
		Datas firstFormVals = formValsService.getFirstForm(pid);
		Map<String, Object> user = authFeignService.getOne(firstFormVals.getAssigner()).getResult();
		result.put("assigner", user.get("REAL_NAME"));
		result.put("formName", firstFormVals.getFormName());
		result.put("createTime", DateTools.formatDateCn(firstFormVals.getCreateTime()));
		return WrapMapper.ok(result);
	}

	@ApiOperation(value = "生成pdf测试接口")
	@GetMapping("/createPdf")
	public Wrapper<Map<String, Object>> createPdf(String task_id) {
		formValsService.createPdf(task_id);
		return WrapMapper.ok();

	}

	@ApiOperation(value = "获取表格数据")
	@GetMapping("/getPdfData")
	public Wrapper<JSONObject> getPdfData(String task_id) {
		JSONObject map = formValsService.getPdfData(task_id);
		return WrapMapper.ok(map);

	}

	@ApiOperation(value = "获取任务信息")
	@GetMapping("/get")
	public Wrapper<JSONObject> get(String task_id) {
		JSONObject result = new JSONObject();
		Task task = taskService.createTaskQuery().taskId(task_id).singleResult();
		result.put("taskDefinitionKey", task.getTaskDefinitionKey());
		result.put("taskName", task.getName());
		return WrapMapper.ok(result);

	}

	public static void main(String[] args) {
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		ArrayList<JSONObject> jsonObjects1 = new ArrayList<>();
		JSONObject object1 = new JSONObject();
		object1.put("order",1);
		JSONObject object2 = new JSONObject();
		object2.put("order",2);
		jsonObjects1.add(object1);
		jsonObjects1.add(object2);
		JSONObject object3 = new JSONObject();
		object3.put("order",1);
		JSONObject object4 = new JSONObject();
		object4.put("order",3);
		ArrayList<JSONObject> jsonObjects2 = new ArrayList<>();
		jsonObjects2.add(object3);
		jsonObjects2.add(object4);
		obj1.put("leader", jsonObjects1);
		obj2.put("leader", jsonObjects2);
		JSONObject jsonObject = new JSONObject();
		jsonObject.fluentPutAll(obj2);
	}

}
