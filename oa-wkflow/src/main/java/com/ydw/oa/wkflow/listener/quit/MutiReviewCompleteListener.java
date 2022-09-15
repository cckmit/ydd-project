package com.ydw.oa.wkflow.listener.quit;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.SpringContextUtil;

// 人员离职
public class MutiReviewCompleteListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 生成表单编码
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		List<String> users = new ArrayList<String>();
		String user1 = authFeignService.getDepartManagerByDeptName("结算业务部").getResult();
		users.add(user1);
		String user2 = authFeignService.getDepartManagerByDeptName("信贷业务部").getResult();
		if(users.contains(user2)) {
			users.add(user2);
		}
		String user3 = authFeignService.getDepartManagerByDeptName("风险合规部").getResult();
		if(users.contains(user3)) {
			users.add(user3);
		}
		String user4 = authFeignService.getDepartManagerByDeptName("投资业务部").getResult();
		if(users.contains(user4)) {
			users.add(user4);
		}
		String user5 = authFeignService.getDepartManagerByDeptName("金融市场业务部").getResult();
		if(users.contains(user5)) {
			users.add(user5);
		}
		String user6 = authFeignService.getDepartManagerByDeptName("担保业务部").getResult();
		if(users.contains(user6)) {
			users.add(user6);
		}
		String user7 = authFeignService.getDepartManagerByDeptName("信息组").getResult();
		if(users.contains(user7)) {
			users.add(user7);
		}
		Object reviewresult = delegateTask.getVariable("reviewresult");
		if("0".equals(reviewresult)) {// 通过
			delegateTask.setVariable("deptList", users);
//			ReviewTool.notice(String.join(",", users), delegateTask);
		} else {
			FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
			Datas datas = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
			delegateTask.setVariable("tableApplyer", datas.getAssigner());
		}
	}

}
