package com.ydw.oa.wkflow.listener.usecar;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_wx.WxFeignService;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//行车命令单
public class ApplyCarReviewEndListener implements ExecutionListener{

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO 生成行车记录单
		String pid = execution.getProcessInstanceId();
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		// 获取表单数据
		JSONObject result = datasService.getFormData(pid);
		if(ChkUtil.isNull(result)) {
			return ;
		}
		// 通知主任
		try {
			AuthFeignService authFeignService = SpringContextUtil.getBean(AuthFeignService.class);
			String role = "政治（综合）工作部主任";
			List<String> usrs = authFeignService.getRoleUsers(role).getResult();
			List<String> wxUsers = authFeignService.getWxUsrIds(String.join(",", usrs)).getResult();

			WxFeignService wxFeignService = SpringContextUtil.getBean(WxFeignService.class);
			String touser = String.join("|", wxUsers);
			JSONObject obj = new JSONObject();
			obj.put("touser", touser);
			//xx（人）用xx（车）出车完成/因行程变更未出车/因车辆因素未出车。
			obj.put("content", result.getString("use_time").substring(0, 11).concat("：").concat(result.getString("owner")).concat("用").concat(result.getString("$carno")).concat(result.getString("circs")));
			wxFeignService.text(obj);
		}catch (Exception e){

		}
	}

}
