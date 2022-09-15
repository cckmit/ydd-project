package com.ydw.oa.auth.business.car.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ydw.oa.auth.business.car.mapper.CarMapper;

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
@Api(description = "车辆管理")
@RestController
@RequestMapping("/api/car")
public class CarApiController {

	@Autowired
	private CarMapper carMapper;

	@ApiOperation(value = "车辆列表")
	@GetMapping("/list")
	public List<Map<String, Object>> list() {
		List<Map<String, Object>> list = carMapper.carList();
		return list;
	}

}
