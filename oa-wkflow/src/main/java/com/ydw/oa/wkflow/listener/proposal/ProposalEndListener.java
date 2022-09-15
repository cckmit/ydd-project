package com.ydw.oa.wkflow.listener.proposal;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.business_wx.WxFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.repository.Model;

import java.util.List;

//投资部-议案报备审批 自动生成编号
public class ProposalEndListener implements ExecutionListener{

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);
		String pid = execution.getProcessInstanceId();
		// 生成编码
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_PROC_INST_ID", pid);
		queryWrapper.orderByDesc("SORTZ");
		List<Datas> list = datasService.list(queryWrapper);
		if(list.isEmpty()) {
			return ;
		}
		Datas datas = list.get(0);
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		String code = object.getString("proposal-code");
		if (ChkUtil.isNull(code)) {
			code = codeService.getProposalCode();
			object.put("proposal-code", code);
			datas.setFormValsJson(object.toJSONString());
			datasService.saveOrUpdate(datas);//保存最后一个

			if (list.size() > 1) {
				//保存first
				datas = list.get(list.size() - 1);
				object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
				object.put("proposal-code", code);
				datas.setFormValsJson(object.toJSONString());
				datasService.saveOrUpdate(datas);
			}


		}




		
	}

}
