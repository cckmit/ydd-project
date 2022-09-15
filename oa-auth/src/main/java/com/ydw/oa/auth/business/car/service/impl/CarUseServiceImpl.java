package com.ydw.oa.auth.business.car.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.auth.business.car.entity.Car;
import com.ydw.oa.auth.business.car.entity.CarUse;
import com.ydw.oa.auth.business.car.mapper.CarUseMapper;
import com.ydw.oa.auth.business.car.service.ICarService;
import com.ydw.oa.auth.business.car.service.ICarUseService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.business_wx.WxFeignService;
import com.ydw.oa.auth.util.DateTools;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-08-07
 */
@Service
public class CarUseServiceImpl extends ServiceImpl<CarUseMapper, CarUse> implements ICarUseService {

	@Autowired
	private ICarService carService;
	@Autowired
	private WxFeignService wxFeignService;
	@Autowired
	private IAuUsrService auUsrService;
	
	@Transactional
	@Override
	public void drivintCar(CarUse carUse) {
		// TODO 出车
		CarUse carUsePo = this.getById(carUse.getObjectId());
		carUsePo.setStatuz("已出车");
		carUsePo.setKeyGetTime(new Date());
		this.saveOrUpdate(carUsePo);
		
		Car car = carService.getById(carUsePo.getCarId());
		car.setStatuz("使用");
		carService.saveOrUpdate(car);
		
		AuUsr usr = auUsrService.getById(carUsePo.getUsrId());
		usr.setStatus(2);
		auUsrService.saveOrUpdate(usr);
		
		JSONObject obj = new JSONObject();
		obj.put("touser", usr.getWxUserId());
		obj.put("content", "尊敬的司机师傅，您已在"+DateTools.getTodayCn()+"领取("+carUsePo.getCarno()+")车辆钥匙。");
		wxFeignService.text(obj);
		
	}

	@Transactional
	@Override
	public void returnCar(CarUse carUse) {
		// TODO 还车
		CarUse carUsePo = this.getById(carUse.getObjectId());
		carUsePo.setStatuz("已还车");
		carUsePo.setKeyBackTime(new Date());
		this.saveOrUpdate(carUsePo);
		
		Car car = carService.getById(carUsePo.getCarId());
		car.setStatuz("在库");
		carService.saveOrUpdate(car);
		
		AuUsr usr = auUsrService.getById(carUsePo.getUsrId());
		usr.setStatus(0);
		auUsrService.saveOrUpdate(usr);
		
		JSONObject obj = new JSONObject();
		obj.put("touser", usr.getWxUserId());
		obj.put("content", "尊敬的司机师傅，您已在"+DateTools.getTodayCn()+"归还("+carUsePo.getCarno()+")车辆钥匙。");
		wxFeignService.text(obj);
	}

}
