package com.ydw.oa.auth.business.car.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.car.entity.CarSubsidyStandard;
import com.ydw.oa.auth.business.car.service.ICarSubsidyStandardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-08-10
 */
@Api(description = "行车补助标准")
@RestController
@RequestMapping("/cp/car-subsidy-standard")
public class CarSubsidyStandardController {

	@Autowired
	private ICarSubsidyStandardService carSubsidyStandardService;

	@ApiOperation(value = "行车补助标准列表")
	@GetMapping("/list")
	public Wrapper<List<CarSubsidyStandard>> list() {
		List<CarSubsidyStandard> list = carSubsidyStandardService.list();
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "添加行车补助标准")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody CarSubsidyStandard carDto) {
		carSubsidyStandardService.save(carDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取行车补助标准数据")
	@GetMapping("/edit_form")
	public Wrapper<CarSubsidyStandard> editForm(String objectId) {
		CarSubsidyStandard carPo = carSubsidyStandardService.getById(objectId);
		return WrapMapper.ok(carPo);
	}

	@ApiOperation(value = "修改行车补助标准")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody CarSubsidyStandard carDto) {
		CarSubsidyStandard carPo = carSubsidyStandardService.getById(carDto.getObjectId());
		carPo.setType(carDto.getType());
		carPo.setStandard(carDto.getStandard());
		carPo.setNote(carDto.getNote());
		carSubsidyStandardService.saveOrUpdate(carPo);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除行车补助标准")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		carSubsidyStandardService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
}
