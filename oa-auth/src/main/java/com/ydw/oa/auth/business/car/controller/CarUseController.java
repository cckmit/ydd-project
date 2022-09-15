package com.ydw.oa.auth.business.car.controller;

import com.alibaba.fastjson.JSONObject;
import com.ydw.oa.auth.business.usr.mapper.AuUsrMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.business_wx.WxFeignService;
import com.ydw.oa.auth.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.car.dto.CarUseQuery;
import com.ydw.oa.auth.business.car.entity.CarUse;
import com.ydw.oa.auth.business.car.service.ICarUseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-08-07
 */
@Api(description = "行车记录管理")
@RestController
@RequestMapping("/cp/car-use")
public class CarUseController {

	@Autowired
	private ICarUseService carUseService;
	@Autowired
	private AuUsrMapper auUsrMapper;
	@Autowired
	WxFeignService wxFeignService;

	@ApiOperation(value = "行车命令单列表")
	@PostMapping("/list")
	public Wrapper<IPage<CarUse>> list(@RequestBody CarUseQuery<CarUse> query) {
		IPage<CarUse> page = carUseService.page(query, query.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "新增用车记录（派车）")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody CarUse carUse) {
		carUse.setStatuz("待出车");
		carUse.setCreateTime(new Date());
		carUseService.save(carUse);
		//通知司机领钥匙
		new Thread(){
			@Override
			public void run() {
				List<String> wxUsrIds = auUsrMapper.getWxUsrIds(carUse.getUsrId());
				String touser = String.join("|", wxUsrIds);
				JSONObject obj = new JSONObject();
				obj.put("touser", touser);
				obj.put("content", "尊敬的司机师傅，您有新的出行任务，请尽快联系办公室领取("+carUse.getCarno()+")的钥匙。");
				wxFeignService.text(obj);
			}
		}.start();
		return WrapMapper.ok("添加成功");
	}

	@ApiOperation(value = "出车")
	@PostMapping("/driving_car")
	public Wrapper<String> driving_car(@RequestBody CarUse carUse) {
		carUseService.drivintCar(carUse);
		return WrapMapper.ok("出车成功");
	}

	@ApiOperation(value = "还车")
	@PostMapping("/return_car")
	public Wrapper<String> return_car(@RequestBody CarUse carUse) {
		carUseService.returnCar(carUse);
		return WrapMapper.ok("还车成功");
	}

	@ApiOperation(value = "获取对象")
	@GetMapping("/get")
	public Wrapper<CarUse> get(String objectId) {
		CarUse carUse = carUseService.getById(objectId);
		return WrapMapper.ok(carUse);
	}

	@ApiOperation(value = "删除记录")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		carUseService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
	
	@ApiOperation(value = "设定距离")
	@GetMapping("/distince")
	public Wrapper<CarUse> distince(String objectId, int standardType) {
		CarUse carUse = carUseService.getById(objectId);
		carUse.setStandardType(standardType);
		carUseService.saveOrUpdate(carUse);
		return WrapMapper.ok(carUse);
	}
}
