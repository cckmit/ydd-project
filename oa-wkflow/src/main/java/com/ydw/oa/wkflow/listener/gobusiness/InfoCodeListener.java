package com.ydw.oa.wkflow.listener.gobusiness;

import java.math.BigDecimal;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.util.NumberToCN;
import com.ydw.oa.wkflow.util.SpringContextUtil;

//业务招待费
public class InfoCodeListener implements TaskListener {

	private static final long serialVersionUID = -1375463595675408879L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO 生成表单编码， 编码不管位置在那字段都为oa-text-code-1
		IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
		IFormService formService = SpringContextUtil.getBean(IFormService.class);
		ICodeService codeService = SpringContextUtil.getBean(ICodeService.class);
		
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
//		Form form = formService.getById(datas.getFormKid());
//		// 获取编码
//		String code = codeService.getReviewCode(form.getName());
//		object.put("oa-text-code-1", "编号："+code);// 编号
		// 获取花费金额
		double num1 = object.getDoubleValue("oa-text-field-16");// 就餐支付金额
		double num2 = object.getDoubleValue("oa-text-field-19");// 其他人员就餐支付金额
		double num3 = object.getDoubleValue("oa-text-field-20");// 住宿支付金额
		double total = num1 + num2 + num3;
		BigDecimal numberOfMoney = new BigDecimal(total);
		String totalBig = NumberToCN.number2CNMontrayUnit(numberOfMoney);
		object.put("oa-text-field-22", totalBig);// 大写
		object.put("oa-text-field-23", total);// 小写
		// 获取房间数
		int num4 = object.getIntValue("oa-text-field-10");// 单间个数
		int num5 = object.getIntValue("oa-text-field-11");// 标间个数
		int sum = num4 + num5;
		object.put("oa-text-field-21", sum);//房间数
		// 保存
		datas.setFormValsJson(object.toJSONString());
		datasService.saveOrUpdate(datas);
	}

}
