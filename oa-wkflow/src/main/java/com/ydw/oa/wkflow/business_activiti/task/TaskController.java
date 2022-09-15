package com.ydw.oa.wkflow.business_activiti.task;

import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.util.activiti.ActivitiTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "流程明细管理")
@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private HistoryService historyService;
	@Autowired
	private JdbcTemplate jt;
	
	@ApiOperation(value = "流程实例明细列表")
	@ApiImplicitParam(name = "processInstanceId", value = "流程实例id")
	@GetMapping("/get_list_by_instance")
	public List<Map<String, Object>> get_list_by_instance(String processInstanceId) {
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).orderByTaskCreateTime().asc();
		List<HistoricTaskInstance> list2 = historicTaskInstanceQuery.list();
		List<Map<String,Object>> list = ActivitiTools.turnHistoricTaskInstance(list2);
		for (Map<String, Object> historicTaskInstance : list) {
			if(ChkUtil.isNull(historicTaskInstance.get("assignee"))) {
				continue;
			}
			Map<String, Object> map;
			try {
				map = jt.queryForMap("select t.REAL_NAME from view_t_au_usr t where t.OBJECT_ID=?", historicTaskInstance.get("assignee"));
				historicTaskInstance.put("assignee", map.get("REAL_NAME"));
			} catch (DataAccessException e) {
				map = null;
			}
			if(ChkUtil.isNull(map)) {
				try {
					map = jt.queryForMap("select t.NAME from view_t_au_dept t where t.OBJECT_ID=?", historicTaskInstance.get("assignee"));
					historicTaskInstance.put("assignee", map.get("NAME"));
				} catch (DataAccessException e) {
					map = null;
				}
			}
		}
		return list;
	}
}