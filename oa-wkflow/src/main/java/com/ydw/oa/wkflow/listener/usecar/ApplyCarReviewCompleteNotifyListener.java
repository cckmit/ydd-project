package com.ydw.oa.wkflow.listener.usecar;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.business_wx.WxFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.sms.SmsAliTools;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;
import java.util.Map;

//行车命令单领导审批完成发送通知
public class ApplyCarReviewCompleteNotifyListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);

		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());

		if(ChkUtil.isNull(object)) {
			return ;
		}
		// 生成行车记录单
		authFeignService.save(object);

		//发送给司机的提醒
		WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
		String user = object.getString("user");

		if (ChkUtil.isNull(user)) {
			return;
		}
		Wrapper<List<String>> wxUsrIds = authFeignService.getWxUsrIds(user);
		String touser = String.join("|", wxUsrIds.getResult());
		String dept = object.getString("oa-text-dept-0");
		String creator = object.getString("creator");
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		// 尊敬的司机，xx年x月x日xx时xx分，  xxx部室的xx（用车人）前往xx，请周知
		obj.put("content", "尊敬的司机，".concat(object.getString("use_time")).concat(",").concat(dept).concat("用车人").concat(object.getString("owner")).concat("将前往").concat(object.getString("end_addr")).concat("，请周知"));
		wxFeignService.text(obj);

		Map<String, Object> one = authFeignService.getOne(user).getResult();
		String phone = (String) one.get("MOBILE");
		// 调用短信通知方法通知司机
		SmsAliTools.notifyCar(phone,object.getString("use_time"),dept,object.getString("owner"),object.getString("end_addr"));


		//发送给车辆管理员
		String role = "车辆管理员";
		List<String> usrs = authFeignService.getRoleUsers(role).getResult();
		if (usrs.isEmpty()) {
			return;
		}
		obj = new JSONObject();
		wxUsrIds = authFeignService.getWxUsrIds(String.join(",",usrs));
		touser = String.join("|", wxUsrIds.getResult());
		obj.put("touser", touser);
		// XXXX部室的XXXX于XX年X月X日X时X分，申请用车。车牌号为XXX；驾驶员为XXX（驾驶员）；乘车人：XXX；用车事由：XXXX；目的地：XXX。
		obj.put("content", dept.concat(creator).concat("于").concat(object.getString("use_time"))
				.concat("，申请用车。车牌号为：").concat(object.getString("$carno"))
				.concat("；驾驶员为：").concat(object.getString("$user"))
				.concat("；乘车人为：").concat(object.getString("owner"))
				.concat("；用车事由：").concat(object.getString("reason"))
				.concat("；目的地:").concat(object.getString("end_addr"))
		);

		wxFeignService.text(obj);

	}

}
