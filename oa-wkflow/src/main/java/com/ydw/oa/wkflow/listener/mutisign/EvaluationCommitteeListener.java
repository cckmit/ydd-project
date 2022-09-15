package com.ydw.oa.wkflow.listener.mutisign;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.List;

//评审委员会会签
public class EvaluationCommitteeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		// 审批人员获取——结算部部长、会计部长、信贷部长、风险部长、担保部分管副总、风险部分管副总
		List<String> usrLeaders = new ArrayList<>();
		//部长
		String user = authFeignService.getDepartManagerByDeptName("计划经营部").getResult();
		usrLeaders.add(user);
		user = authFeignService.getDepartManagerByDeptName("风险合规部").getResult();
		usrLeaders.add(user);
		user = authFeignService.getDepartManagerByDeptName("结算业务部").getResult();
		usrLeaders.add(user);
		user = authFeignService.getDepartManagerByDeptName("信贷业务部").getResult();
		usrLeaders.add(user);
		//分管领导
		user = authFeignService.getDepartLeaderByDeptName("风险合规部").getResult();
		usrLeaders.add(user);
		user = authFeignService.getDepartLeaderByDeptName("担保业务部").getResult();
		usrLeaders.add(user);
		delegateTask.setVariable("assigneeList", usrLeaders);
	}

}
