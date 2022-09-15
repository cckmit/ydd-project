package com.ydw.oa.auth.business.dept.dto;

import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DeptQuery 部门表", description = "")
public class DeptQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "部门名称")
	private String name;
	@ApiModelProperty(value = "上级部门ID")
	private String parentId;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("NAME", name);
		}
		if (ChkUtil.isNotNull(parentId)) {
			queryWrapper.eq("PARENT_ID", parentId);
		}
		return queryWrapper;
	}

}
