package com.ydw.oa.wkflow.listener.common;

import java.util.List;

import com.ydw.oa.wkflow.listener.base.NoticeListener;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;

public class RoleReviewListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 特定角色审批
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		String name = delegateTask.getName();
		String role = null;
		if(name.indexOf("审批")>0||name.indexOf("签字")>0) {
			role = name.substring(0, name.length()-2);
		}
		List<String> usrs = authFeignService.getRoleUsers(role).getResult();
		if (usrs.size() == 1) {
			delegateTask.setAssignee(usrs.get(0));
		} else {
			delegateTask.addCandidateUsers(usrs);
		}
		this.notice(delegateTask, usrs, "notice");
	}

}
