package com.ydw.oa.wkflow.listener.common;

import com.ydw.oa.wkflow.listener.base.NoticeListener;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ydw.oa.wkflow.util.activiti.ReviewTool;

// 通知会签人员
public class MutiSignSingleAssigneeSetListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 通知会签人员
		this.notice(delegateTask,delegateTask.getAssignee(),"notice");
	}

}
