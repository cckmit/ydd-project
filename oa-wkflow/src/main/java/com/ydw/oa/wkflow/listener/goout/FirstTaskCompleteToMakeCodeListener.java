package com.ydw.oa.wkflow.listener.goout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.SpringContextUtil;

public class FirstTaskCompleteToMakeCodeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 生成表单编码
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		IFormService formService = SpringContextUtil.getBean(IFormService.class);
		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		// 获取编码
		Form form = formService.getById(datas.getFormKid());
//		String code = codeService.getReviewCode(form.getName());
//		object.put("oa-text-code-0", "编号：" + code);// 编号
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		Map<String, Object> one = authFeignService.getOne(SessionTool.getSessionAdminId()).getResult();
		object.put("oa-text-creator-0", one.get("REAL_NAME"));// 经办人
		object.put("oa-text-dept-0", one.get("dept"));// 经办部室
		delegateTask.setVariable("oatextdept", one.get("dept"));// 经办部室

		List<String> usr = null;
		JSONArray array = object.getJSONArray("oa-list-0");
		if (array!=null && array.size() > 0) {
			usr = new ArrayList<>();
			for (Object object2 : array) {
				JSONObject obj = (JSONObject) object2;
				String dept = obj.getString("oa-list-0-col-1");
				if ("经理部".equals(dept) || "财务公司".equals(dept)) {
					continue;
				}
				String user = authFeignService.getDepartLeaderByDeptName(dept).getResult();
				if(!usr.contains(user)) {
					usr.add(user);
				}
			}
			if (usr.size() == 0) {
				usr = null;
			}
		}

		if (usr == null) {
			JSONObject reviewinfo = new JSONObject();
			reviewinfo.put("key", "deptreview");
			reviewinfo.put("index", 1);
			object.put("reviewinfo",reviewinfo);
		}
		delegateTask.setVariable("deptList", usr);
		datas.setFormValsJson(object.toJSONString());
		datasService.saveOrUpdate(datas);
		//获取领导
		JSONArray array2 = object.getJSONArray("oatextfiled5");
		List<String> leaders = null;
		if (array2!=null && array2.size() > 0) {
			leaders = new ArrayList<>();
			for (Object o : array2) {
				String leader = (String)o;
				if ("董事长".equals(leader) || "总经理".equals(leader) || "财务分管领导".equals(leader)) {
					List<String> usrManager = authFeignService.getRoleUsers(leader).getResult();
					leaders.addAll(usrManager);
				}
			}
			if (leaders.size() == 0) {
				leaders = null;
			}
		}
		delegateTask.setVariable("leaderList", leaders);
	}

}
