package com.ydw.oa.wkflow.listener.seal;

import java.util.List;
import java.util.Map;

import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.date.DateTools;

// 印鉴使用审批
public class MutiReviewCompleteListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 数据处理
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
//		IFormService formService = SpringContextUtil.getBean(IFormService.class);
//		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);

		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		Map<String, Object> one = authFeignService.getOne(SessionTool.getSessionAdminId()).getResult();
		object.put("oa-text-creator-0", one.get("REAL_NAME"));// 经办人
		object.put("oa-text-dept-0", one.get("dept"));// 经办部室
		delegateTask.setVariable("oatextdept", one.get("dept"));// 经办部室
		object.put("oa-text-date-0", DateTools.getTodayCn());// 经办部室
		// 保存编码
		// 获取编码
//		Form form = formService.getById(datas.getFormKid());
//		String code = codeService.getReviewCode(form.getName());
//		object.put("oa-text-code-0", "编号：" + code);// 编号
		datas.setFormValsJson(object.toJSONString());
		datasService.saveOrUpdate(datas);
		// 处理选择的审批部门
		String obj = object.getString("oa-text-field-3");
		List<String> usrs = null;
		if("董事长".equals(obj)) {
			usrs = authFeignService.getRoleUsers("董事长").getResult();
		}else if("总经理".equals(obj)) {
			usrs = authFeignService.getRoleUsers("总经理").getResult();
		}else if("分管领导".equals(obj)) {
			usrs = authFeignService.getDepartLeader(SessionTool.getSessionAdminId()).getResult();
		}else if("部室负责人".equals(obj)) {
			usrs = authFeignService.getDepartManager(SessionTool.getSessionAdminId()).getResult();
		}else if("党支部书记".equals(obj)){
			usrs=authFeignService.getRoleUsers("党支部书记").getResult();
		}else if("青工委员".equals(obj)){
			usrs=authFeignService.getRoleUsers("青工委员").getResult();
		}else if("组织委员".equals(obj)){
			usrs=authFeignService.getRoleUsers("组织委员").getResult();
		}else if("宣传委员".equals(obj)){
			usrs=authFeignService.getRoleUsers("宣传委员").getResult();
		}else if("纪检委员".equals(obj)){
			usrs=authFeignService.getRoleUsers("纪检委员").getResult();
		}
		if (usrs.size() > 0) {
			delegateTask.setVariable("dept", usrs.get(0));
		}
//		ReviewTool.notice(String.join(",", users), delegateTask);
		
	}

}
