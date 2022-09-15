package com.ydw.oa.wkflow.listener.common;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class SelectDeptLeaderReviewListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = 9187588174260956505L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 部门分管领导审批
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
		Datas datas = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		String dept = object.getString("dept");
		String user = authFeignService.getDepartLeaderByDeptName(dept).getResult();
		delegateTask.setAssignee(user);
		this.notice(delegateTask, Lists.newArrayList(user),"notice");
	}
}
