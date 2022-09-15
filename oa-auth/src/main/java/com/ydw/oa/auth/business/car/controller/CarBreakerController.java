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
import com.ydw.oa.auth.business.car.dto.CarBreakerQuery;
import com.ydw.oa.auth.business.car.entity.CarBreaker;
import com.ydw.oa.auth.business.car.service.ICarBreakerService;

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
@Api(description = "车辆违章管理")
@RestController
@RequestMapping("/cp/car-breaker")
public class CarBreakerController {

	@Autowired
	private ICarBreakerService carBreakerService;

	@ApiOperation(value = "车辆违章列表")
	@PostMapping("/list")
	public Wrapper<IPage<CarBreaker>> list(@RequestBody CarBreakerQuery<CarBreaker> carBreakerQuery) {
		IPage<CarBreaker> page = carBreakerService.page(carBreakerQuery, carBreakerQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加车辆违章")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody CarBreaker carBreakerDto) {
		carBreakerService.save(carBreakerDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取车辆违章数据")
	@GetMapping("/edit_form")
	public Wrapper<CarBreaker> editForm(String objectId) {
		CarBreaker carBreakerPo = carBreakerService.getById(objectId);
		return WrapMapper.ok(carBreakerPo);
	}

	@ApiOperation(value = "修改车辆违章")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody CarBreaker carBreakerDto) {
		CarBreaker carBreakerPo = carBreakerService.getById(carBreakerDto.getObjectId());
		carBreakerPo.setBreakTime(carBreakerDto.getBreakTime());
		carBreakerPo.setCarId(carBreakerDto.getCarId());
		carBreakerPo.setCarno(carBreakerDto.getCarno());
		carBreakerPo.setType(carBreakerDto.getType());
		carBreakerPo.setUsrId(carBreakerDto.getUsrId());
		carBreakerPo.setUsrName(carBreakerDto.getUsrName());
		carBreakerService.saveOrUpdate(carBreakerPo);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除车辆违章数据")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		carBreakerService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

}
