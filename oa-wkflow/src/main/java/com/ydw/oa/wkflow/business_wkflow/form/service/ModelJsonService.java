package com.ydw.oa.wkflow.business_wkflow.form.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;

@Service
public class ModelJsonService {
	
	@Autowired
	private JdbcTemplate jt;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ObjectMapper objectMapper;

	// 根据完成时间,自动设置提醒方式.
	public void dueNextTaskPushHandle(String processInstanceId) {
		// 先找到当前 Task
		List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		if (list.size() == 0) {
			return;
		}
		for (Task task : list) {
			handleTask(task);
		}

	}

	private void handleTask(Task task) {
		// 1.定位资源id
		String resourceId = task.getTaskDefinitionKey();
		String doduedatedefinition = "0";// 限制时间
		String handledatedefinition = "短信提醒";// 提醒方式

		// 2. 查找model中的json数据
		String processDefinitionId = task.getProcessDefinitionId();// 例如 qingjia:4:55011
		String processName = processDefinitionId.split(":")[0];
		String sql = "select t.ID_ id from ACT_RE_MODEL t where t.NAME_=?";
		List<Map<String, Object>> list = jt.queryForList(sql, processName);
		if (ChkUtil.isNull(list)) {
			return;
		}
		String modelId = list.get(0).get("id") + "";

		byte[] bytes = repositoryService.getModelEditorSource(modelId);
		ObjectNode editorJsonNode;
		try {
			editorJsonNode = (ObjectNode) objectMapper.readTree(new String(bytes, "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		JSONObject json = JsonUtil.jsonStrToJsonObject(editorJsonNode.toString());
		JSONArray childShapes = json.getJSONArray("childShapes");
		if (childShapes == null || childShapes.size() == 0) {
			return;
		}
		for (int i = 0; i < childShapes.size(); i++) {
			JSONObject node = childShapes.getJSONObject(i);
			if (resourceId.equals(node.getString("resourceId"))) {
				JSONObject propJson = node.getJSONObject("properties");
				doduedatedefinition = propJson.getString("doduedatedefinition");
				handledatedefinition = propJson.getString("handledatedefinition");
				break;
			}
		}

		int doduedatedefinitionHour = ChkUtil.getInteger(doduedatedefinition);
		int handledatedefinitionType = "短信提醒".equals(handledatedefinition) ? 1 : 2;
		if (doduedatedefinitionHour <= 0) {
			return;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, doduedatedefinitionHour);
		// 3.查找下个 Task 并设置完成时限
		task.setDueDate(cal.getTime());
		task.setPriority(handledatedefinitionType);
		taskService.saveTask(task);

	}

}
