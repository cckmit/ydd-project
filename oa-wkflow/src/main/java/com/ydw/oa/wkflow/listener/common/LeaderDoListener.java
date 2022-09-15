package com.ydw.oa.wkflow.listener.common;

import com.beust.jcommander.internal.Lists;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class LeaderDoListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;


	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 各部门分管领导审批
		String name = delegateTask.getName();
		if (name.indexOf("分管领导审批")>0||name.indexOf("分管领导签字")>0) {
			name = name.substring(0, name.length()-6);
		}
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		String user = authFeignService.getDepartLeaderByDeptName(name).getResult();
		delegateTask.setAssignee(user);
		this.notice(delegateTask, Lists.newArrayList(user),"notice");
	}

}
