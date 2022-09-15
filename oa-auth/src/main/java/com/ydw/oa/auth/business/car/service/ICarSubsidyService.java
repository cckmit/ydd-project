package com.ydw.oa.auth.business.car.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.car.entity.CarSubsidy;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hxj
 * @since 2020-08-10
 */
public interface ICarSubsidyService extends IService<CarSubsidy> {

	JSONObject getSubsidyList(String time);

	JSONObject reloadSubsidyList(String time);

	//导出考核考勤数据Excel
	void export(String time,String dataPath);

}
