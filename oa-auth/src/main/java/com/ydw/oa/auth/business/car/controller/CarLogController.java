package com.ydw.oa.auth.business.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.car.entity.CarLog;
import com.ydw.oa.auth.business.car.service.ICarLogService;
import com.ydw.oa.auth.business.car.dto.CarLogQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-06-30
 */
@Api(description = "车辆申请/使用/维护/保养/归还 记录管理")
@RestController
@RequestMapping("/cp/car-log")
public class CarLogController {

	@Autowired
	private ICarLogService carLogService;

	@ApiOperation(value = "记录列表")
	@PostMapping("/list")
	public Wrapper<IPage<CarLog>> list(@RequestBody CarLogQuery<CarLog> query) {
		IPage<CarLog> page = carLogService.page(query, query.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "生成记录")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody CarLog log) {
		carLogService.save(log);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除记录")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		carLogService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
}
