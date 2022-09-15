package com.ydw.oa.wkflow.listener.common;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.business_wx.WxFeignService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

public class CarApplyFormDoListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;


	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 办公室主任
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
		Datas datas = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
		WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
		String role = "车辆填写人";
		List<String> usrs = authFeignService.getRoleUsers(role).getResult();
		if (usrs.isEmpty() || usrs.size() < 1) {
			return;
		}
		if (usrs.size() == 1) {
			delegateTask.setAssignee(usrs.get(0));
		} else {
			delegateTask.addCandidateUsers(usrs);
		}
		this.notice(delegateTask, usrs, "todo");
		//企业微信消息提醒
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		//申请人
		String creator = object.getString("creator");
		//申请部室
		String dept = object.getString("oa-text-dept-0");
		JSONObject obj = new JSONObject();
		List<String> wxUsrs = authFeignService.getWxUsrIds(String.join(",", usrs)).getResult();
		obj.put("touser", String.join("|", wxUsrs));
		obj.put("content", dept.concat(creator).concat("申请用车"));
		wxFeignService.text(obj);
	}

}
