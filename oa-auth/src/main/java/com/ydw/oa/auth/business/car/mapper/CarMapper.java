package com.ydw.oa.auth.business.car.mapper;

import com.ydw.oa.auth.business.car.entity.Car;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxj
 * @since 2020-06-30
 */
public interface CarMapper extends BaseMapper<Car> {

	List<Map<String, Object>> carList();

}
