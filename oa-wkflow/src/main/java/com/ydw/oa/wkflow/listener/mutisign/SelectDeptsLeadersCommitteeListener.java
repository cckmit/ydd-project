package com.ydw.oa.wkflow.listener.mutisign;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.List;

//发送所选部门所有分管领导会签
public class SelectDeptsLeadersCommitteeListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = 9187588174260956505L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 分管领导会签
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
		Datas datas = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		List<String> depts = (List<String>) object.get("depts");
		List<String> usrs = new ArrayList<>();
		for (String dept : depts) {
			String user = authFeignService.getDepartLeaderByDeptName(dept).getResult();
			usrs.add(user);
		}
		delegateTask.setVariable("assigneeLeadersList", usrs);
	}
}
