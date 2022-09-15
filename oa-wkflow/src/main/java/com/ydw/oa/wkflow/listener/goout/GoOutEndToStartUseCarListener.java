package com.ydw.oa.wkflow.listener.goout;

import java.util.List;

import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_wx.WxFeignService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.repository.Model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//因公外出
public class GoOutEndToStartUseCarListener implements ExecutionListener{

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO 开启行车命令单流程
		FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
		RepositoryService repositoryService = SpringContextUtil.getBean(RepositoryService.class);

		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		String pid = execution.getProcessInstanceId();
		// 获取表单数据
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("TYPE", "审批单");
		queryWrapper.eq("ACTI_PROC_INST_ID", pid);
		queryWrapper.orderByDesc("SORTZ");
		List<Datas> list = datasService.list(queryWrapper);
		if(list.isEmpty()) {
			return ;
		}
		JSONObject object = JsonUtil.jsonStrToJsonObject(list.get(0).getFormValsJson());
		Datas datas = formValsService.getFirstForm(pid);


		//通知消息
		//外出地点
		String outAddr = object.getString("oa-text-filed-3");
		String creator = object.getString("oa-text-creator-0");
		String dept = object.getString("oa-text-dept-0");
		//外出时间
		String outTime = object.getString("oa-text-filed-1");

		try {
			// 通知主任
			AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
			String role = "政治（综合）工作部主任";
			List<String> usrs = authFeignService.getRoleUsers(role).getResult();
			List<String> wxUsers = authFeignService.getWxUsrIds(String.join(",", usrs)).getResult();

			WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
			String touser = String.join("|", wxUsers);
			JSONObject obj = new JSONObject();
			obj.put("touser", touser);
			//XX部室xx（人）于x月x日-x月x日前往xx（某地），请周知。
			obj.put("content","因公外出：" .concat(dept.concat(creator).concat("于").concat(outTime).concat("前往").concat(outAddr).concat("，请周知")));
			wxFeignService.text(obj);
		} catch (Exception e) {

		}




		Model model = null;
//		if("汽车".equals(object.getString("oatextfiled6"))) {
//			model = repositoryService.createModelQuery().modelId("8").singleResult();
//		}
		if("培训".equals(object.getString("oatextfiled8"))) {
			model = repositoryService.createModelQuery().modelId("57542").singleResult();
		}
		if("差旅".equals(object.getString("oatextfiled8"))) {
			model = repositoryService.createModelQuery().modelId("70248").singleResult();
		}
		if(ChkUtil.isNotNull(model)) {
			// 启动流程并为第一个任务赋代理人
			formValsService.startProcessAndSetAssignee(model.getDeploymentId(), datas.getAssigner());
		}

		
	}

}
