package com.ydw.oa.wkflow.listener.gobusiness;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.date.DateTools;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;
import java.util.Map;

// 业务接待费审批
public class MutiReviewCompleteListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 数据处理
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);

		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		// 选择领导
		String leader = object.getString("oatextfield14");
		if (ChkUtil.isNotNull(leader)) {
			delegateTask.setVariable("leader", leader);
		}

	}

}
