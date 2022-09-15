package com.ydw.oa.wkflow.business_main.category.dto;

import com.tmsps.fk.common.base.dto.BaseVueQuery;
import io.swagger.annotations.ApiModel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CategoryQuery 对象", description = "")
public class CategoryQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderBy(true, true, "CODE");
		return queryWrapper;
	}

}
