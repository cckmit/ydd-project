package com.ydw.oa.wkflow.listener.mutisign;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

//投委会会签
public class InvestmentCommitteeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		// 审批人员获取——副总经理、会计部部长、风险部部长、结算部部长，主任委员：投资部分管领导
		//副总经理
		List<String> usrLeaders = authFeignService.getRoleUsers("副总经理").getResult();
		//各个部长
		String user = authFeignService.getDepartManagerByDeptName("计划经营部").getResult();
		usrLeaders.add(user);
		user = authFeignService.getDepartManagerByDeptName("风险合规部").getResult();
		usrLeaders.add(user);
		user = authFeignService.getDepartManagerByDeptName("结算业务部").getResult();
		usrLeaders.add(user);
		//分管领导
		user = authFeignService.getDepartLeaderByDeptName("投资业务部").getResult();
		usrLeaders.add(user);
		delegateTask.setVariable("assigneeList", usrLeaders);
	}

}
