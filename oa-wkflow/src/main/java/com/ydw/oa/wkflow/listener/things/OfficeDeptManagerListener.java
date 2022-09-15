package com.ydw.oa.wkflow.listener.things;

import com.beust.jcommander.internal.Lists;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;

//办公用品申请
public class OfficeDeptManagerListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 综合办公室负责人审批
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		String user = authFeignService.getDepartManagerByDeptName("政治（综合）工作部").getResult();

		delegateTask.setAssignee(user);
		if(delegateTask.getName().indexOf("填写")==-1) {
			this.notice(delegateTask, Lists.newArrayList(user),"notice");
		}else {
			this.notice(delegateTask, Lists.newArrayList(user),"todo");
		}
	}

}
