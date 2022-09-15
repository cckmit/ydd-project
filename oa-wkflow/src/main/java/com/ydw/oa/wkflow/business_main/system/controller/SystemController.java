package com.ydw.oa.wkflow.business_main.system.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.system.entity.System;
import com.ydw.oa.wkflow.business_main.system.service.ISystemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-11
 */
@Api(description = "系统设置")
@RestController
@RequestMapping("/system/system")
public class SystemController {

	@Autowired
	private ISystemService iSystemService;

	@ApiOperation(value = "系统设置列表")
	@GetMapping("/list")
	public Wrapper<List<System>> list() {
		QueryWrapper<System> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderBy(true, true, "CREATE_TIME");
		List<System> list = iSystemService.list();

		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "添加系统设置")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody System system) {
		iSystemService.save(system);

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除系统设置")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		iSystemService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取系统设置数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<System> editForm(String objectId) {
		System system = iSystemService.getById(objectId);
		return WrapMapper.ok(system);
	}

	@ApiOperation(value = "编辑系统设置")
	@PostMapping("/edit")
	public Wrapper<String> edit(@Valid @RequestBody System system) {
		System systemDb = iSystemService.getById(system.getObjectId());
		systemDb.setName(system.getName());
		systemDb.setValue(system.getValue());
		systemDb.setNote(system.getNote());
		iSystemService.saveOrUpdate(systemDb);

		return WrapMapper.ok("保存成功");
	}
}
