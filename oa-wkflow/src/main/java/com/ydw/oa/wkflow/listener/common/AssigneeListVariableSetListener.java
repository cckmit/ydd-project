package com.ydw.oa.wkflow.listener.common;

import java.util.List;

import com.tmsps.fk.common.util.ChkUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//经理部全员会签
public class AssigneeListVariableSetListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 设置会签人员列表
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		// 审批人员获取——两个副总一个总经理
		List<String> usrLeaders = authFeignService.getRoleUsers("副总经理").getResult();
		List<String> usrManager = authFeignService.getRoleUsers("总经理").getResult();
		if (ChkUtil.isNotNull(usrLeaders)) {
			usrLeaders.addAll(usrManager);
		}else{
			usrLeaders = usrManager;
		}
		delegateTask.setVariable("assigneeList", usrLeaders);

//		ReviewTool.notice(String.join(",", usrs), delegateTask);
	}

}
