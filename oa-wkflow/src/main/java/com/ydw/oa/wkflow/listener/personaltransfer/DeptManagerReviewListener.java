package com.ydw.oa.wkflow.listener.personaltransfer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beust.jcommander.internal.Lists;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONObject;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;

//人员岗位调动
public class DeptManagerReviewListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = 9187588174260956505L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 调入部门部长/部门负责人审批
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
		Datas datas = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		String deptName = object.getString("oa-text-field-5");
		String user = authFeignService.getDepartManagerByDeptName(deptName).getResult();
		delegateTask.setAssignee(user);
		this.notice(delegateTask, Lists.newArrayList(user),"notice");
	}
}
