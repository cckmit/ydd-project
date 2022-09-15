package com.ydw.oa.wkflow.business_activiti.dto;

import com.tmsps.fk.common.base.dto.BaseVueQuery;
import io.swagger.annotations.ApiModel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ModelQuery 对象", description = "")
public class ModelQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		return queryWrapper;
	}

}
