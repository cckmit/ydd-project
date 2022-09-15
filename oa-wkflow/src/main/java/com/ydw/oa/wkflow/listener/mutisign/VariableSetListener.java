package com.ydw.oa.wkflow.listener.mutisign;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ydw.oa.wkflow.util.SessionTool;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.util.NumberToCN;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//业务招待费会签
public class VariableSetListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 变更已选审批单的状态并设置会签人员列表
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		IFormService formService = SpringContextUtil.getBean(IFormService.class);
		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);
		// 审批人员获取——两个副总一个总经理
		List<String> usrLeaders = authFeignService.getRoleUsers("副总经理").getResult();
		List<String> usrManager = authFeignService.getRoleUsers("总经理").getResult();
		usrLeaders.addAll(usrManager);
		delegateTask.setVariable("assigneeList", usrLeaders);

		// 变更已选审批单的状态
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		Map<String, Object> one = authFeignService.getOne(SessionTool.getSessionAdminId()).getResult();
		object.put("oa-text-creator-0", one.get("REAL_NAME"));// 经办人
		object.put("oa-text-dept-0", one.get("dept"));// 经办部室
		JSONArray array = object.getJSONArray("oa-list-0");
		String totalMoney = authFeignService.choseRecord(Joiner.on(",").join(array)).getResult();
		object.put("oa-text-field-0", totalMoney);
		BigDecimal numberOfMoney = new BigDecimal(totalMoney);
		String totalBig = NumberToCN.number2CNMontrayUnit(numberOfMoney);
		object.put("oa-text-field-1", totalBig);
		// 获取编码
//		Form form = formService.getById(datas.getFormKid());
//		String code = codeService.getReviewCode(form.getName());
//		object.put("oa-text-code-0", "编号：" + code);// 编号
		// 保存
		datas.setFormValsJson(object.toJSONString());
		datasService.saveOrUpdate(datas);
	}

}
