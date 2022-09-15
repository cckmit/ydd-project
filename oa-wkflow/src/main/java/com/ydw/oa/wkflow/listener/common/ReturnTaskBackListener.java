package com.ydw.oa.wkflow.listener.common;

import com.beust.jcommander.internal.Lists;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.Execution;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;

public class ReturnTaskBackListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -6054576435306127485L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 回退到已做过的任务节点
		if (ChkUtil.isNull(delegateTask.getAssignee())) {
			// 获取流程id
			String excId = delegateTask.getExecutionId();
			RuntimeService runtimeService = SpringContextUtil.getBean(RuntimeService.class);
			// 获取流程
			Execution execution = runtimeService.createExecutionQuery().executionId(excId).singleResult();
			if(ChkUtil.isNull(execution)) {
				return ;
			}
			// 获取节点id
			String recourseId = execution.getActivityId();
			
			// 查询之前任务记录
			IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
			QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("ACTI_PROC_INST_ID", delegateTask.getProcessInstanceId());
			queryWrapper.eq("RESOURCE_ID", recourseId);
			Datas datas = datasService.getOne(queryWrapper);
			// 设置任务执行人为之前任务的执行人
			delegateTask.setAssignee(datas.getAssigner());
			this.notice(delegateTask, Lists.newArrayList(datas.getAssigner()),"todo");
		}
	}

}
