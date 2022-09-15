package com.ydw.oa.wkflow.listener.docissue;

import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.reject.entity.Reject;
import com.ydw.oa.wkflow.business_main.reject.service.IRejectService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//公文签发
@Deprecated
public class TogeReviewListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 处理会签数据
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);

		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		// 获取编码
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
		String admin = object.getString("admin");
		JSONArray deptList = object.getJSONArray("oa-list-0");
		JSONArray array = object.getJSONArray("oa-list-1");
		if(ChkUtil.isNull(array)) {
			array = new JSONArray();
		}else if(deptList.size()==array.size()) {
			array = new JSONArray();
		}
		array.add(admin);
		
		if(deptList.size()==array.size()) {
			// 判断通过/驳回
			FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
			Datas firstData = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
			
			QueryWrapper<Datas> qw = new QueryWrapper<>();
			qw.eq("ACTI_PROC_INST_ID", delegateTask.getProcessInstanceId());
			qw.eq("FORM_KID", firstData.getFormKid());
			qw.orderByDesc("CREATE_TIME");
			List<Datas> list = datasService.list(qw);
			Datas newFormData = list.get(0);
			
			IRejectService rejectService = SpringContextUtil.getBean(IRejectService.class);
			QueryWrapper<Reject> qw1 = new QueryWrapper<>();
			qw1.eq("PID", delegateTask.getProcessInstanceId());
			qw1.gt("CREATE_TIME", newFormData.getCreateTime());
			int count = rejectService.count(qw1);// 驳回数量
			if(count > 0) {
				delegateTask.setVariable("reviewresult", 1);
				delegateTask.setVariable("tableApplyer", firstData.getAssigner());
			}else {
				delegateTask.setVariable("reviewresult", 0);
			}
		}
		object.put("oa-list-1", Joiner.on("<br>").join(array));
		object.put("admin", "");
		datas.setFormValsJson(object.toJSONString());
		datasService.saveOrUpdate(datas);
	}

}
