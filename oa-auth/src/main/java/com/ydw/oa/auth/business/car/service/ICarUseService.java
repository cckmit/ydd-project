package com.ydw.oa.auth.business.car.service;

import com.ydw.oa.auth.business.car.entity.CarUse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-08-07
 */
public interface ICarUseService extends IService<CarUse> {

	void drivintCar(CarUse carUse);

	void returnCar(CarUse carUse);

}
