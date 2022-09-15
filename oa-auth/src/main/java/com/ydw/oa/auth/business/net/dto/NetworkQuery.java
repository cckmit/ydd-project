package com.ydw.oa.auth.business.net.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "NetworkQuery 链接表", description = "")
public class NetworkQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "名称")
	private String name;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("NAME", name);
		}
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}

}
