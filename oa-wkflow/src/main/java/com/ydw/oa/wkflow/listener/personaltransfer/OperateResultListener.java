package com.ydw.oa.wkflow.listener.personaltransfer;

import java.util.List;

import com.beust.jcommander.internal.Lists;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;

//人员岗位调动
public class OperateResultListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = 9187588174260956505L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 落实情况说明,办公室人员操作
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		String role = "办公室人员";
		List<String> usrs = authFeignService.getRoleUsers(role).getResult();
		if (usrs.size() == 1) {
			delegateTask.setAssignee(usrs.get(0));
		} else {
			delegateTask.addCandidateUsers(usrs);
		}
		this.notice(delegateTask, usrs,"todo");
	}
}
