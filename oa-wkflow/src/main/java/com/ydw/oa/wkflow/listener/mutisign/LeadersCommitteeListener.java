package com.ydw.oa.wkflow.listener.mutisign;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

//副总经理会签
public class LeadersCommitteeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		//审批人员获取——副总经理
		//副总经理
		List<String> usrLeaders = authFeignService.getRoleUsers("副总经理").getResult();
		delegateTask.setVariable("assigneeList", usrLeaders);
	}

}
