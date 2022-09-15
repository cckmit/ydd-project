package com.ydw.oa.wkflow.listener.common;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

public class StaffDoListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;


	@Override
	public void notify(DelegateTask delegateTask) {
		String role = "固定资产管理";
		// TODO 办公人员
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);

		List<String> usrs = authFeignService.getRoleUsers(role).getResult();
		if (usrs.size() == 1) {
			delegateTask.setAssignee(usrs.get(0));
		} else {
			delegateTask.addCandidateUsers(usrs);
		}
		this.notice(delegateTask, usrs,"todo");
	}

}
