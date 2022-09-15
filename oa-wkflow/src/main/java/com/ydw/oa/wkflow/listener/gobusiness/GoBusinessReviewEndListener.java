package com.ydw.oa.wkflow.listener.gobusiness;

import java.util.List;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.repository.Model;

//业务招待费
public class GoBusinessReviewEndListener implements ExecutionListener{

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO 生成接待记录
		String pid = execution.getProcessInstanceId();
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);
		IFormService formService = SpringContextUtil.getBean(IFormService.class);
		RepositoryService repositoryService = SpringContextUtil.getBean(RepositoryService.class);
		// 获取表单数据
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("TYPE", "审批单");
		queryWrapper.eq("ACTI_PROC_INST_ID", pid);
		queryWrapper.orderByDesc("SORTZ");
		List<Datas> list = datasService.list(queryWrapper);
		if(list.isEmpty()) {
			return ;
		}
		Datas firstForm = formValsService.getFirstForm(pid);
		JSONObject object = JsonUtil.jsonStrToJsonObject(firstForm.getFormValsJson());
		String code = object.getString("oa-text-code-0");
		if (ChkUtil.isNull(code)) {
			Form form = formService.getById(firstForm.getFormKid());
			Model model = repositoryService.createModelQuery().modelId(form.getModelId()).singleResult();
			code = codeService.getReviewCode(model.getName());
			object.put("oa-text-code-0", "编号：" + code);// 编号
			firstForm.setFormValsJson(object.toJSONString());
			datasService.saveOrUpdate(firstForm);
		}
		JSONObject result = JsonUtil.jsonStrToJsonObject(list.get(0).getFormValsJson());
		result.put("pid", pid);
		result.put("formId", list.get(0).getFormKid());
		result.put("oa-text-code-0", "编号：" + code);
		// 生成接待记录
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		authFeignService.saveReceptionRecords(result);
	}

}
