package com.ydw.oa.wkflow.listener.mutisign.role;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

//通用角色会签
public class CommonRoleCommitteeListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 角色会签
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		String name = delegateTask.getName();
		if(name.indexOf("会签")>0) {
			List<String> usrs = authFeignService.getRoleUsers(name).getResult();
			delegateTask.setVariable("assigneeList", usrs);
		}

	}

}
