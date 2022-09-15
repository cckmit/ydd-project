package com.ydw.oa.wkflow.listener.gobusiness;

import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//业务招待费
public class CreateCodeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 生成表单编码，填表部门，人员，时间格式化
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		IFormService formService = SpringContextUtil.getBean(IFormService.class);
		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);

		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		Form form = formService.getById(datas.getFormKid());
		// 获取编码
//		String code = codeService.getReviewCode(form.getName());
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
//		object.put("oa-text-code-0", "编号：" + code);// 编号
		Map<String, Object> one = authFeignService.getOne(SessionTool.getSessionAdminId()).getResult();
		object.put("oa-text-creator-0", one.get("REAL_NAME"));// 经办人
		object.put("oa-text-dept-0", one.get("dept"));// 接待部室
		if (ChkUtil.isNotNull(object.get("oa-text-field-7"))) {
			JSONArray array = object.getJSONArray("oa-text-field-7");
			object.put("oa-text-date-0", array.get(0) + "~" + array.get(1));// 住宿接待时间字符串类型
			object.put("oa-text-date-apply", array.get(0));// 住宿接待时间开始时间
		}
		// 保存编码
		datas.setFormValsJson(object.toJSONString());
		datasService.saveOrUpdate(datas);
	}

}
