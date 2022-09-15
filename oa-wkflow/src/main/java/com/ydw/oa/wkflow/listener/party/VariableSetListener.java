package com.ydw.oa.wkflow.listener.party;

import java.util.HashSet;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//党建经费使用审批
public class VariableSetListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 设置会签人员列表和编号
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		IFormService formService = SpringContextUtil.getBean(IFormService.class);
		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);
		// 获取会签人员
		List<String> usrs = authFeignService.getRoleUsers("董事长").getResult();
		List<String> usrs1 = authFeignService.getRoleUsers("总经理").getResult();
		List<String> usrs2 = authFeignService.getRoleUsers("副总经理").getResult();
		usrs.addAll(usrs1);
		usrs.addAll(usrs2);
		HashSet<String> h = new HashSet<String>(usrs);   
		usrs.clear();   
		usrs.addAll(h);  
		delegateTask.setVariable("partylist", usrs);

//		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
//		Datas datas = datasService.getOne(queryWrapper);
//		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
//		// 获取编码
////		Form form = formService.getById(datas.getFormKid());
////		String code = codeService.getReviewCode(form.getName());
////		object.put("oa-text-code-0", "编号：" + code);// 编号
//		// 保存
//		datas.setFormValsJson(object.toJSONString());
//		datasService.saveOrUpdate(datas);
	}

}
