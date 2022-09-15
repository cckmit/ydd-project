package com.ydw.oa.wkflow.business_main.reject.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.oa.wkflow.business_main.reject.entity.Reject;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxj
 * @since 2020-11-16
 */
public interface RejectMapper extends BaseMapper<Reject> {

	IPage<Map<String, Object>> query(Page<Reject> page, @Param(Constants.WRAPPER) Wrapper<Reject> makeQueryWrapper);
	IPage<Map<String, Object>> query2(Page<Reject> page, @Param(Constants.WRAPPER) Wrapper<Reject> makeQueryWrapper);

}
