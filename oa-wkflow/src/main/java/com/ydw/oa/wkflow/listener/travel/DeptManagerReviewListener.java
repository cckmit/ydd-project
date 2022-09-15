package com.ydw.oa.wkflow.listener.travel;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

public class DeptManagerReviewListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = 9187588174260956505L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 选择的经办部门分管领导审批
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
		Datas datas = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		String dept = object.getString("dept");
		String admin = authFeignService.getDepartManagerByDeptName(dept).getResult();
		delegateTask.setAssignee(admin);
		this.notice(delegateTask,admin,"notice");
	}
}
