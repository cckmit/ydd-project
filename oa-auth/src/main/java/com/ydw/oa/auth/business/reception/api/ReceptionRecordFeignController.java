package com.ydw.oa.auth.business.reception.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.reception.service.IReceptionRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "接待记录feign接口")
@RestController
@RequestMapping("/api/reception")
public class ReceptionRecordFeignController {

	@Autowired
	private IReceptionRecordService receptionRecordService;

	@ApiOperation(value = "接待记录保存")
	@PostMapping("/save")
	public Wrapper<String> save(@RequestBody JSONObject records) {
		receptionRecordService.saveRecords(records);

		return WrapMapper.ok();
	}

	@ApiOperation(value = "更改所选择会签记录的状态")
	@GetMapping("/choseRecord")
	public Wrapper<String> choseRecord(String pids) {
		String totalMoney = receptionRecordService.choseRecord(pids);
		return WrapMapper.ok(totalMoney);
	}
	
	@ApiOperation(value = "接待会签结束，变更状态")
	@GetMapping("/endRecordSign")
	public Wrapper<String> endRecordSign(String pids, boolean isReject) {
		receptionRecordService.endRecordSign(pids, isReject);
		return WrapMapper.ok();
	}
}
