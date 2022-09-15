package com.ydw.oa.wkflow.listener.seal;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.util.SpringContextUtil;

// 印鉴使用审批
public class ReviewCompleteListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 数据处理
		Object reviewresult = delegateTask.getVariable("reviewresult");
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
//		IFormService formService = SpringContextUtil.getBean(IFormService.class);
//		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);
		
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		if("0".equals(reviewresult)) {// 通过
//			// 获取编码
//			Form form = formService.getById(datas.getFormKid());
//			String code = codeService.getReviewCode(form.getName());
//			object.put("oa-text-code-0", "编号：" + code);// 编号
			object.put("reviewresult","通过");
		}else {
			object.put("reviewresult","驳回");
		}
		datas.setFormValsJson(object.toJSONString());
		datasService.saveOrUpdate(datas);
	}

}
