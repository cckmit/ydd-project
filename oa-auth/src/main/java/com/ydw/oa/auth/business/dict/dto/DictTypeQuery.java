package com.ydw.oa.auth.business.dict.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DictQuery 对象", description = "")
public class DictTypeQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderBy(true, true, "SHOW_ORDERS");
		return queryWrapper;
	}

}
