package com.ydw.oa.wkflow.listener.meal;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class JudgeSignOrder implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		String oatextdept = object.getString("oatextdept");
		if ("政治（综合）工作部".equals(oatextdept)) {
			JSONObject reviewinfo = new JSONObject();
			reviewinfo.put("key", "deputymanager");
			reviewinfo.put("index", 1);
			object.put("reviewinfo",reviewinfo);
		}
	}

}
