package com.ydw.oa.wkflow.business_activiti.service;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.business_activiti.dto.TaskCommonQuery;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.date.DateTools;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.NativeTaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 待办任务注入类，动态修改 Task的assignee等
 *
 * @author 冯晓东
 *
 */
@Service
public class AllTaskTodoService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TaskService taskService;

	/**
	 * 	查询待办任务 拼接sql  包含待办任务
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

		if (ChkUtil.isNotNull(taskCommonQuery.getName())||ChkUtil.isNotNull(taskCommonQuery.getStartTime())||ChkUtil.isNotNull(taskCommonQuery.getEndTime())) {
			sb.append("WHERE");
			//任务名称
			if (ChkUtil.isNotNull(taskCommonQuery.getName())) {
				sb.append(" RES.NAME_ LIKE #{taskName}");
				nativeTaskQuery = nativeTaskQuery.parameter("taskName", "%" + taskCommonQuery.getName() + "%");
			}
			//日期查询
			if (ChkUtil.isNotNull(taskCommonQuery.getStartTime())) {
				sb.append(" RES.CREATE_TIME_ >= #{starttime} and  ");
				nativeTaskQuery = nativeTaskQuery.parameter("starttime", start);
			}
			if (ChkUtil.isNotNull(taskCommonQuery.getEndTime())) {
				sb.append(" RES.CREATE_TIME_ <= #{endtime}");
				nativeTaskQuery = nativeTaskQuery.parameter("endtime", end);
			}
		}

		sb.append("ORDER BY res.CREATE_TIME_ desc");
		nativeTaskQuery = nativeTaskQuery.sql(sb.toString());

		return nativeTaskQuery;

	}

}
