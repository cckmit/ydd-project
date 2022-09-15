package com.ydw.oa.wkflow.listener.mutisign;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//所有部长会签
public class AllDeptManagerReviewListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = 9187588174260956505L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 所有部长/部门负责人审批
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		List<Map<String, Object>> deptManagers = authFeignService.getDeptManagers();

		if (deptManagers.size() == 0) {
			return;
		}
		List<String> users = new ArrayList<>();
		for (Map<String, Object> deptManager : deptManagers) {
			String objectId = (String) deptManager.get("value");
			if (ChkUtil.isNotNull(objectId)) {
				users.add(objectId);
			}
		}
		if (users.size() > 0) {
			delegateTask.setVariable("assigneeList", users);
		}
	}
}
