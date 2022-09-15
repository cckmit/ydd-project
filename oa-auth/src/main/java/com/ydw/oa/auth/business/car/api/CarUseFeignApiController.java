package com.ydw.oa.auth.business.car.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.car.entity.CarUse;
import com.ydw.oa.auth.business.car.service.ICarUseService;
import com.ydw.oa.auth.util.DateTools;

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
@Api(description = "车辆出车记录")
@RestController
@RequestMapping("/api/car_use")
public class CarUseFeignApiController {

	@Autowired
	private ICarUseService carUseService;

	@ApiOperation(value = "车辆列表")
	@PostMapping("/save")
	public Wrapper<String> save(@RequestBody JSONObject carInfo) {
		QueryWrapper<CarUse> query = new QueryWrapper<CarUse>();
		query.eq("PID", carInfo.getString("pid"));
		CarUse carUse = carUseService.getOne(query);
		if (ChkUtil.isNull(carUse)) {
			carUse = new CarUse();
			carUse.setCarId(carInfo.getString("carno"));
			carUse.setCarno(carInfo.getString("$carno"));
			carUse.setDepert(carInfo.getString("depert"));
			carUse.setEndAddr(carInfo.getString("end_addr"));
			carUse.setNote(carInfo.getString("note"));
			carUse.setPid(carInfo.getString("pid"));
			carUse.setStartAddr(carInfo.getString("start_addr"));
			carUse.setStatuz("待出车");
			carUse.setUser(carInfo.getString("$user"));
			carUse.setUseTime(DateTools.strToDate(carInfo.getString("use_time"), "yyyy年MM月dd日HH时"));
			carUse.setUsrId(carInfo.getString("user"));
			carUseService.save(carUse);
		}
		return WrapMapper.ok();
	}

}
