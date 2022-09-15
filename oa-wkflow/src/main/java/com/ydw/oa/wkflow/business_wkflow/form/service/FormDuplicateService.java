package com.ydw.oa.wkflow.business_wkflow.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.business_wkflow.task.service.TaskInjectService;
import com.ydw.oa.wkflow.util.SessionTool;

/**
 * 表单去重
 * 
 * @author Administrator
 *
 */
@Service
public class FormDuplicateService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TaskService taskService;
	@Autowired
	private TaskInjectService taskInjectService;
	@Autowired
	private JdbcTemplate jt;

	// 表单去重
	public void doDuplicateTask(String processInstanceId) {
		// 1. 找到当前task
		List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		// 0 或者 会签任务 多task 直接返回
		if (list.size() == 0 || list.size() > 1) {
			return;
		}
		Task task = list.get(0);
		logger.info("新打开的TaskId-->{}", task.getId());

		// 3.对比执行人是否一样 || 判断角色是否含有当前人员
		boolean isSamplePerson = isSamplePerson(task);

		if (isSamplePerson) {
			logger.info("检测到执行人相同,自动完成审批.");

			task.setAssignee(SessionTool.getSessionAdminId());
			task.setDescription("自动去重跳过");
			taskService.saveTask(task);
			// 关闭task
			taskService.complete(task.getId());
			// 动态注入下个节点变量
//			taskInjectService.injectDynamicData(task);
		}
	}

	private boolean isSamplePerson(Task task) {
		String adminId = SessionTool.getSessionAdminId();
		if (ChkUtil.isNull(adminId) || task == null) {
			return false;
		}
		if (adminId.equals(task.getAssignee())) {
			return true;
		}
		List<String> groupList = findGroupList(task.getId());
		for (String groupId : groupList) {
			if (findInfo(groupId, adminId)) {
				return true;
			}
		}
		return false;
	}

	// 查询角色列表
	public List<String> findGroupList(String taskId) {
		String sql = "select t.GROUP_ID_ groupId from ACT_HI_IDENTITYLINK t where t.TASK_ID_=? ";
		List<Map<String, Object>> list = jt.queryForList(sql, taskId);

		List<String> groupList = new ArrayList<>();
		for (Map<String, Object> map : list) {
			groupList.add(map.get("groupId") + "");
		}
		return groupList;
	}

	/**
	 * 根据角色号+用户号, 查询此人是否是同一个人
	 * 
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public boolean findInfo(String roleId, String userId) {
		if (ChkUtil.isNull(roleId) || ChkUtil.isNull(userId)) {
			return false;
		}
		String sql = "select * from t_role_user t where t.status=0 and t.role_id=? and t.user_id=? ";
		List<Map<String, Object>> list = jt.queryForList(sql, roleId, userId);
		return list.size() > 0;
	}

}
