package com.ydw.oa.wkflow.listener.usecar;

import java.util.Map;

import com.ydw.oa.wkflow.util.SessionTool;
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
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.date.DateTools;

//行车命令单
public class ApplyCarCodeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 生成表单编码
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		IFormService formService = SpringContextUtil.getBean(IFormService.class);
		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
		
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		// 获取编码
//		Form form = formService.getById(datas.getFormKid());
//		String code = codeService.getReviewCode(form.getName());
//		object.put("oa-text-code-0", "编号：" + code);// 编号
		Datas firstDatas = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
		Map<String, Object> one = authFeignService.getOne(firstDatas.getAssigner()).getResult();
		String creator = (String) object.get("creator");
		object.put("creator", creator);// 申请人
		object.put("oa-text-creator-0", one.get("REAL_NAME"));// 经办人
		object.put("oa-text-dept-0", one.get("dept"));// 经办部室
		object.put("depert", one.get("dept"));// 部室
		// 保存编码
		datas.setFormValsJson(object.toJSONString());
		datasService.saveOrUpdate(datas);
	}

}
