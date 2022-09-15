package com.ydw.oa.auth.business.car.controller;

import cn.hutool.core.map.MapUtil;
import com.ydw.oa.auth.business_wkflow.WkflowFeignService;
import com.ydw.oa.auth.util.DateTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.car.dto.CarSubsidyQuery;
import com.ydw.oa.auth.business.car.entity.CarSubsidy;
import com.ydw.oa.auth.business.car.service.ICarSubsidyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-08-10
 */
@Api(description = "车辆补助管理")
@RestController
@RequestMapping("/cp/car-subsidy")
public class CarSubsidyController {

	@Autowired
	private ICarSubsidyService carSubsidyService;
	@Autowired
	private WkflowFeignService wkflowFeignService;

	@ApiOperation(value = "补助列表")
	@PostMapping("/list")
	public Wrapper<JSONObject> list(@RequestBody CarSubsidyQuery<CarSubsidy> query) {
		if (ChkUtil.isNull(query.getTime())) {
			return WrapMapper.ok();
		}
		JSONObject result = carSubsidyService.getSubsidyList(query.getTime());
		return WrapMapper.ok(result);
	}

	@ApiOperation(value = "重新生成补助列表")
	@PostMapping("/reload_list")
	public Wrapper<JSONObject> reload_list(@RequestBody CarSubsidyQuery<CarSubsidy> query) {
		if (ChkUtil.isNull(query.getTime())) {
			return WrapMapper.ok();
		}
		JSONObject result = carSubsidyService.reloadSubsidyList(query.getTime());
		return WrapMapper.ok(result);
	}

	@ApiOperation(value = "补助列表")
	@GetMapping("/export")
	public void export(String time) {
		if (ChkUtil.isNull(time)) {
			time =  DateTools.getToday("yyyy-MM");
		}
		Wrapper<Map<String, Object>> mapWrapper = wkflowFeignService.get();
		Map<String, Object> result = mapWrapper.getResult();
		String dataPath = MapUtil.getStr(result, "value");
		carSubsidyService.export(time,dataPath);
	}
}
