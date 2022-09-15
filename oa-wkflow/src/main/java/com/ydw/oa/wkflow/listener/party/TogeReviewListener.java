package com.ydw.oa.wkflow.listener.party;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

//党建经费使用审批
public class TogeReviewListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 处理会签数据
//		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
//
//		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
//		Datas datas = datasService.getOne(queryWrapper);
//		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
//		String admin = object.getString("party");
//		JSONArray array = object.getJSONArray("partylist");
//		if(ChkUtil.isNull(array)) {
//			array = new JSONArray();
//		}
//		array.add(admin);
//		object.put("partylist", Joiner.on("<br>").join(array));
//		object.put("party", "");
//		datas.setFormValsJson(object.toJSONString());
//		datasService.saveOrUpdate(datas);
	}

}
