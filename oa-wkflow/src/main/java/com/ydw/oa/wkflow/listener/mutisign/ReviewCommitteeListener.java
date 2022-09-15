package com.ydw.oa.wkflow.listener.mutisign;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.List;

//审贷委员会会签
public class ReviewCommitteeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		// 审批人员获取——信贷分管副总、副总经理、会计部长、结算部长、风险部长、风险专员（程子淇）
		//副总经理
		List<String> usrLeaders = authFeignService.getRoleUsers("副总经理").getResult();
		//部长
		String user = authFeignService.getDepartManagerByDeptName("计划经营部").getResult();
		usrLeaders.add(user);
		user = authFeignService.getDepartManagerByDeptName("风险合规部").getResult();
		usrLeaders.add(user);
		user = authFeignService.getDepartManagerByDeptName("结算业务部").getResult();
		usrLeaders.add(user);
		//分管领导
//		user = authFeignService.getDepartLeaderByDeptName("信贷业务部").getResult();
//		usrLeaders.add(user);
		//风险专员
		List<String> usrs = authFeignService.getRoleUsers("风险专员").getResult();
		usrLeaders.addAll(usrs);
		delegateTask.setVariable("assigneeList", usrLeaders);
	}

}
