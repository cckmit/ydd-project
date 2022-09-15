package com.ydw.oa.wkflow.listener.things;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beust.jcommander.internal.Lists;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.listener.base.NoticeListener;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;

//固定资产审批流程
public class InfoDeptManagerListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 信息组负责人审批
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		String user = authFeignService.getDepartManagerByDeptName("信息组").getResult();

		delegateTask.setAssignee(user);
		this.notice(delegateTask, Lists.newArrayList(user),"notice");
	}

}
