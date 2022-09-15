package com.ydw.oa.wkflow.listener.docissue;

import java.util.Map;

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
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//公文签发
public class CreateInfoListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 生成主办部室和撰稿人
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		IFormService formService = SpringContextUtil.getBean(IFormService.class);
		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);

		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		// 获取编码
//		Form form = formService.getById(datas.getFormKid());
//		String code = codeService.getReviewCode(form.getName());
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		Map<String, Object> one = authFeignService.getOne(SessionTool.getSessionAdminId()).getResult();
		object.put("oa-text-field-0", one.get("dept")+"&nbsp;&nbsp;&nbsp;&nbsp;"+one.get("REAL_NAME"));// 接待部室
//		object.put("oa-text-code-0", "编号：" + code);// 编号
		datas.setFormValsJson(object.toJSONString());
		datasService.saveOrUpdate(datas);
	}

}
