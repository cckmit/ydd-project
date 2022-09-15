package com.ydw.oa.auth.business.reception.api;

import java.util.List;
import java.util.Map;

import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ydw.oa.auth.business.reception.entity.ReceptionRecord;
import com.ydw.oa.auth.business.reception.service.IReceptionRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "接待记录Api接口")
@RestController
@RequestMapping("/api/reception")
public class ReceptionRecordApiController {

	@Autowired
	private IReceptionRecordService receptionRecordService;

	@ApiOperation(value = "form表单调用接口，获取待会签的记录列表")
	@GetMapping("/unSignList")
	public List<Map<String, Object>> unSignList() {
		QueryWrapper<ReceptionRecord> query = new QueryWrapper<ReceptionRecord>();
		query.select("PID value, REVIEW_CODE label");
		query.eq("MUTI_REVIEW", "待会签");
		query.groupBy("PID");
		List<Map<String, Object>> list = receptionRecordService.listMaps(query);
		return list;
	}

	@ApiOperation(value = "form表单调用接口，获取待会签的记录列表")
	@GetMapping("/one")
	public Wrapper<ReceptionRecord> one(String objectId) {
		ReceptionRecord receptionRecord = receptionRecordService.getById(objectId);
		return WrapMapper.ok(receptionRecord);
	}
	
}
