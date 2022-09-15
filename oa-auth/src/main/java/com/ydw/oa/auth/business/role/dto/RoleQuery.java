package com.ydw.oa.auth.business.role.dto;

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
@ApiModel(value = "RoleQuery 对象", description = "")
public class RoleQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "角色名称")
	private String name;
	@ApiModelProperty(value = "状态(0:正常;1:停用)")
	private String status;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("NAME", name);
		}
		if (ChkUtil.isNotNull(status)) {
			queryWrapper.eq("STATUS", status);
		}
		queryWrapper.orderByAsc("SHOW_ORDERS");
		return queryWrapper;
	}

}
