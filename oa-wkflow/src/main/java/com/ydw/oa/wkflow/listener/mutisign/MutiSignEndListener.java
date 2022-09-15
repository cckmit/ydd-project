package com.ydw.oa.wkflow.listener.mutisign;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.reject.service.IRejectService;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//业务招待费会签
public class MutiSignEndListener implements ExecutionListener{

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO 会签结束，变更状态
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
		IRejectService rejectService = SpringContextUtil.getBean(IRejectService.class);
		String pid = execution.getProcessInstanceId();
		// 获取表单数据
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("TYPE", "审批单");
		queryWrapper.eq("ACTI_PROC_INST_ID", pid);
		queryWrapper.orderByDesc("SORTZ");
		List<Datas> list = datasService.list(queryWrapper);
		if(list.isEmpty()) {
			return ;
		}
		JSONObject object = JsonUtil.jsonStrToJsonObject(list.get(0).getFormValsJson());
		JSONArray array = object.getJSONArray("oa-list-0");
		// 判断是否有驳回
		boolean isReject = rejectService.checkReject(pid);
		// 会签结束，变更状态
		authFeignService.endRecordSign(Joiner.on(",").join(array), isReject);
	}

}
