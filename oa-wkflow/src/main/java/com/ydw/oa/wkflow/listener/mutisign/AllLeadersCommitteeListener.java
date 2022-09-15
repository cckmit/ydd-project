package com.ydw.oa.wkflow.listener.mutisign;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

//副总经理+总经理会签
public class AllLeadersCommitteeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		//审批人员获取——担保公司副总
		//担保公司副总
		List<String> usrLeaders = authFeignService.getRoleUsers("担保公司副总").getResult();
//		List<String> managers = authFeignService.getRoleUsers("总经理").getResult();
//		usrLeaders.addAll(managers);
		delegateTask.setVariable("assigneeList", usrLeaders);
	}

}
