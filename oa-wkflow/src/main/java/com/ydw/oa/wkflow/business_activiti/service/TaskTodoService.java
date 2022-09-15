package com.ydw.oa.wkflow.business_activiti.service;

import java.util.Date;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.NativeTaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.business_activiti.dto.TaskCommonQuery;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.date.DateTools;

/**
 * 待办任务注入类，动态修改 Task的assignee等
 * 
 * @author 冯晓东
 *
 */
@Service
public class TaskTodoService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TaskService taskService;

	/**	
	 * 	查询待办任务 拼接sql  包含待办任务和委托任务
	 * @param taskCommonQuery
	 * @return
	 */
	public NativeTaskQuery createSql(TaskCommonQuery taskCommonQuery) {
		Date start = DateTools.strToDate2(taskCommonQuery.getStartTime());
		Date end = DateTools.strToDate2(taskCommonQuery.getEndTime());
		NativeTaskQuery nativeTaskQuery = taskService.createNativeTaskQuery();
		StringBuilder sb = new StringBuilder();
		sb.append(
				"select distinct RES.* from ACT_RU_TASK RES left join ACT_RU_IDENTITYLINK I on I.TASK_ID_ = RES.ID_ inner join ACT_RE_PROCDEF D on RES.PROC_DEF_ID_ = D.ID_ ");
		sb.append(" left join view_t_au_usr_dept vud on RES.ASSIGNEE_=vud.DEPT_ID  ");
		sb.append(" left join t_delegate_business db on res.PROC_DEF_ID_=db.BUSINESS_KEY and db.IS_DELETED=0 ");
		sb.append(" left join t_delegate de on db.DELEGATE_ID=de.OBJECT_ID and de.STATUZ='启用' and de.START<=#{current} and de.END>=#{currentTime} and de.USERD=#{userd}  ");
		sb.append(" left join view_t_au_usr_dept vd on de.USER=vd.USR_ID  ");
		sb.append(" WHERE ");
		//任务名称
		if (ChkUtil.isNotNull(taskCommonQuery.getName())) {
			sb.append(" RES.NAME_ LIKE #{taskName} and  ");
			nativeTaskQuery = nativeTaskQuery.parameter("taskName", "%"+taskCommonQuery.getName()+"%");
		}
		
		//日期查询
		if (ChkUtil.isNotNull(taskCommonQuery.getStartTime())) {
			sb.append(" RES.CREATE_TIME_ >= #{starttime} and  ");
			nativeTaskQuery = nativeTaskQuery.parameter("starttime", start);
		}
		if (ChkUtil.isNotNull(taskCommonQuery.getEndTime())) {
			sb.append(" RES.CREATE_TIME_ <= #{endtime} and  ");
			nativeTaskQuery = nativeTaskQuery.parameter("endtime", end);
		}

		sb.append(
				" (RES.ASSIGNEE_=#{assignee} or vud.USR_ID=#{deptUser} or (RES.ASSIGNEE_ is null and I.TYPE_ = 'candidate' and (I.USER_ID_=#{user_id} or vud.USR_ID=#{dept_user}))) ");
		sb.append(
				" or (RES.ASSIGNEE_ = de.USER or RES.ASSIGNEE_=vd.DEPT_ID or (RES.ASSIGNEE_ is null and I.TYPE_ = 'candidate' and (I.USER_ID_ = de.USER or I.USER_ID_=vd.DEPT_ID))) ");
		sb.append(" order by RES.CREATE_TIME_ desc ");
		long now = System.currentTimeMillis();
		String userId = taskCommonQuery.getUserId();
		nativeTaskQuery = nativeTaskQuery.parameter("current", now).parameter("currentTime", now).parameter("userd", userId==null?SessionTool.getSessionAdminId():userId)
				.parameter("assignee", userId==null?SessionTool.getSessionAdminId():userId).parameter("deptUser", userId==null?SessionTool.getSessionAdminId():userId)
				.parameter("user_id", userId==null?SessionTool.getSessionAdminId():userId).parameter("dept_user", userId==null?SessionTool.getSessionAdminId():userId);
		nativeTaskQuery = nativeTaskQuery.sql(sb.toString());

		return nativeTaskQuery;

	}

}
