package com.ydw.oa.auth.business.car.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.car.dto.CarQuery;
import com.ydw.oa.auth.business.car.entity.Car;
import com.ydw.oa.auth.business.car.service.ICarService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-06-30
 */
@Api(description = "车辆管理")
@RestController
@RequestMapping("/cp/car")
public class CarController {

	@Autowired
	private ICarService carService;

	@ApiOperation(value = "车辆列表")
	@PostMapping("/list")
	public Wrapper<IPage<Car>> list(@RequestBody CarQuery<Car> carQuery) {
		IPage<Car> page = carService.page(carQuery, carQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加车辆")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody Car carDto) {
		carService.save(carDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取车辆数据")
	@GetMapping("/edit_form")
	public Wrapper<Car> editForm(String objectId) {
		Car carPo = carService.getById(objectId);
		return WrapMapper.ok(carPo);
	}

	@ApiOperation(value = "修改车辆")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody Car carDto) {
		Car carPo = carService.getById(carDto.getObjectId());
		carPo.setBusNo(carDto.getBusNo());
		carPo.setBuyTime(carDto.getBuyTime());
		carPo.setCarno(carDto.getCarno());
		carPo.setEngineNo(carDto.getEngineNo());
		carPo.setFrameNo(carDto.getFrameNo());
		carPo.setNumber(carDto.getNumber());
		carPo.setStatuz(carDto.getStatuz());
		carService.saveOrUpdate(carPo);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除车辆数据")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		carService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
	
	
	@ApiOperation(value = "可用车辆列表")
	@GetMapping("/use_list")
	public Wrapper<List<Car>> use_list() {
		QueryWrapper<Car> qw = new QueryWrapper<>();
		qw.eq("STATUZ", "在库");
		qw.orderByAsc("CARNO");
		List<Car> list = carService.list(qw);
		return WrapMapper.ok(list);
	}
}

