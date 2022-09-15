package com.ydw.oa.wkflow.business_main.system.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.system.entity.System;
import com.ydw.oa.wkflow.business_main.system.service.ISystemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "参数feign接口")
@RestController
@RequestMapping("/api/system")
public class SystemFeignController {

	@Autowired
	private ISystemService iSystemService;

	@ApiOperation(value = "获取对象")
	@GetMapping("/get")
	public Wrapper<Map<String, Object>> getDataPath() {
		QueryWrapper<System> queryWrapper = new QueryWrapper<System>();
		queryWrapper.eq("NAME", "DATA-PATH");
		System system = iSystemService.getOne(queryWrapper);
		Map<String, Object> map = JsonUtil.objToMap(system);
		return WrapMapper.ok(map);
	}

}
