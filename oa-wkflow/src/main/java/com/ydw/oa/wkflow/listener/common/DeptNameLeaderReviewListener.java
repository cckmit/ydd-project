package com.ydw.oa.wkflow.listener.common;

import com.beust.jcommander.internal.Lists;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;

import java.util.ArrayList;

public class DeptNameLeaderReviewListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = 9187588174260956505L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 部门分管领导审批   xxx审批
		String dept = delegateTask.getName().replace("审批", "");
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		String user = authFeignService.getDepartLeaderByDeptName(dept).getResult();

		delegateTask.setAssignee(user);
		this.notice(delegateTask, Lists.newArrayList(user), "notice");
	}
}
