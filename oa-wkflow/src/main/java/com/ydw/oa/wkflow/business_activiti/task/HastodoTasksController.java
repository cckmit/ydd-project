package com.ydw.oa.wkflow.business_activiti.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_activiti.dto.TaskCommonQuery;
import com.ydw.oa.wkflow.business_main.reject.dto.RejectQuery;
import com.ydw.oa.wkflow.business_main.reject.entity.Reject;
import com.ydw.oa.wkflow.business_main.reject.mapper.RejectMapper;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.activiti.ActivitiTools;
import com.ydw.oa.wkflow.util.date.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Api(description = "已办任务管理")
@RestController
public class HastodoTasksController {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RejectMapper rejectMapper;

	@ApiOperation(value = "我的已办任务列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "名称"),
			@ApiImplicitParam(name = "current", value = "当前页"), @ApiImplicitParam(name = "size", value = "每页条数") })
	@PostMapping("/hastodoTasksList")
	public Wrapper<Map<String, Object>> HastodoTasksList(@RequestBody TaskCommonQuery taskCommonQuery) {
		// 创建查询对象
		String name = taskCommonQuery.getName();
		int size = taskCommonQuery.getSize();
		int current = taskCommonQuery.getCurrent();
		String userId = taskCommonQuery.getUserId();
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
		if (ChkUtil.isNotNull(name)) {
			historicTaskInstanceQuery.taskNameLike("%" + name + "%");
		}
		if (ChkUtil.isNotNull(taskCommonQuery.getCategory_id())) {
			List<String> category = new ArrayList<>();
			category.add(taskCommonQuery.getCategory_id());
			historicTaskInstanceQuery.processCategoryIn(category);
		}
		historicTaskInstanceQuery.taskAssignee(userId==null?SessionTool.getSessionAdminId():userId);
		Date startDate = DateTools.strToDate2(taskCommonQuery.getStartTime());
		Date endDate = DateTools.strToDate2(taskCommonQuery.getEndTime());
		historicTaskInstanceQuery.taskCreatedAfter(startDate).taskCreatedBefore(endDate);
		QueryWrapper<Reject> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("START_USR_ID", userId == null ? SessionTool.getSessionAdminId() : userId);
		queryWrapper.eq("REJECT_TYPE", "撤销");
		queryWrapper.eq("IS_DELETED", 0);
		queryWrapper.orderByDesc("CREATE_TIME");
		List<Reject> rejects = rejectMapper.selectList(queryWrapper);
		List<String> pids = new ArrayList<>();
		if (rejects != null && !rejects.isEmpty()) {
			pids = rejects.stream().map(Reject::getPid).collect(Collectors.toList());
		}
		// Date finishStartDate =
		// DateTools.strToDate2(taskCommonQuery.getFinishStartTime());
		// Date finishEndDate =
		// DateTools.strToDate2(taskCommonQuery.getFinishEndTime());
		// historicTaskInstanceQuery.taskDueAfter(finishStartDate).taskDueBefore(finishEndDate);
		historicTaskInstanceQuery.finished();
		long total = historicTaskInstanceQuery.count();
		if (ChkUtil.isNotNull(name) || ChkUtil.isNotNull(startDate) || ChkUtil.isNotNull(endDate)) {
			current = 1;
		}
		List<HistoricTaskInstance> list = historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().desc()
				.listPage((current - 1) * size, size);
		List<Map<String, Object>> listEnd = ActivitiTools.turnHistoricTaskInstance(list);
		List<Map<String, Object>> noRejectList = new ArrayList<>();
		if (listEnd != null && !listEnd.isEmpty()) {
			List<String> pidsCopy = pids;
			noRejectList = listEnd.stream().filter(map -> !pidsCopy.contains(MapUtils.getString(map, "processInstanceId"))).collect(Collectors.toList());
			noRejectList.forEach(map -> {
				ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(map.get("processDefinitionId") + "").singleResult();
				map.put("processInstanceName", processDefinition.getName());
			});
		}
		Map<String, Object> map = new HashMap<>();
		map.put("records", noRejectList);
		map.put("current", current);
		map.put("size", size);
		map.put("total", total);
		return WrapMapper.ok(map);
	}

	@ApiOperation(value = "流程监控(查询已经发起的流程实例以及已经执行完成的流程实例)")
	@ApiImplicitParam(name = "name", value = "名称")
	@GetMapping("/queryHistoricInstance")
	public Wrapper<Page<HistoricProcessInstance>> queryHistoricInstance(String name, int current, int size, String category_id) {
		// 根据条件查询所有的流程实例
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();

		if (ChkUtil.isNotNull(name)) {
			historicProcessInstanceQuery.processInstanceNameLike("%" + name + "%");
			current = 1;
		}
		historicProcessInstanceQuery.processDefinitionCategory(category_id);
		long total = historicProcessInstanceQuery.count();
		List<HistoricProcessInstance> list = historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc()
				.listPage((current - 1) * size, size);
		// 判断流程实例是否走完
		for (int i = 0; i < list.size(); i++) {
			ProcessInstanceQuery createProcessInstanceQuery = runtimeService.createProcessInstanceQuery();
			ProcessInstanceQuery processInstanceId = createProcessInstanceQuery.processInstanceId(list.get(i).getId());
			ProcessInstance singleResult = processInstanceId.singleResult();
			if (singleResult == null) {
				list.get(i).setLocalizedDescription("已完成");
			} else {
				list.get(i).setLocalizedDescription("进行中");
			}
		}

		Page<HistoricProcessInstance> page = new Page<HistoricProcessInstance>();
		page.setTotal(total);
		page.setRecords(list);
		page.setCurrent(current);
		page.setSize(size);
		return WrapMapper.ok(page);
	}
}
