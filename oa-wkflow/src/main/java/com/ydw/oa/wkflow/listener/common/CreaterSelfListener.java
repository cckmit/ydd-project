package com.ydw.oa.wkflow.listener.common;

import com.ydw.oa.wkflow.listener.base.NoticeListener;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.SpringContextUtil;

public class CreaterSelfListener extends NoticeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 填表人/流程开启人操作

		FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
		Datas datas = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
		delegateTask.setAssignee(datas.getAssigner());
		String name = delegateTask.getName();
		if(name.indexOf("审批")>0||name.indexOf("签字")>0) {
			this.notice(delegateTask,datas.getAssigner(),"todo");
			return;
		}
		ReviewTool.notice2(datas.getAssigner(),delegateTask);
	}

}
