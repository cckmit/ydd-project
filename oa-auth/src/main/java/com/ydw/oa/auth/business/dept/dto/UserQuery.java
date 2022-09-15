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
@ApiModel(value = "UserQuery 部门表", description = "")
public class UserQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "登录名")
	private String usrName;
	@ApiModelProperty(value = "真实姓名")
	private String realName;
	@ApiModelProperty(value = "状态(0:正常;1:停用)")
	private String status;
	@ApiModelProperty(value = "所在部门ID")
	private String dept_id;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrName)) {
			queryWrapper.like("g.USR_NAME", usrName);
		}
		if (ChkUtil.isNotNull(realName)) {
			queryWrapper.like("g.REAL_NAME", realName);
		}
		if(ChkUtil.isNotNull(dept_id)) {
			queryWrapper.eq("a.DEPT_ID", dept_id);
		}
		
		return queryWrapper;
	}

}
