package com.ydw.oa.auth.business.reception.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.reception.entity.ReceptionRecord;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hxj
 * @since 2020-09-21
 */
public interface IReceptionRecordService extends IService<ReceptionRecord> {

	// 保存审批记录
	void saveRecords(JSONObject records);

	// 选择会签记录,更改状态
	String choseRecord(String objectIds);

	// 接待会签结束，变更状态
	void endRecordSign(String objectIds, boolean isReject);

	void export(String month, String datePath);

}
