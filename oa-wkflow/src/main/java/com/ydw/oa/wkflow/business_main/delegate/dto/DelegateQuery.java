package com.ydw.oa.wkflow.business_main.delegate.dto;

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
@ApiModel(value = "DelegateQuery对象", description = "")
public class DelegateQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "委托业务")
	private String business;
	@ApiModelProperty(value = "委托事项")
	private String title;
	@ApiModelProperty(value = "委托人")
	private String user;
	@ApiModelProperty(value = "被委托人")
	private String userd;
	@ApiModelProperty(value = "开始时间")
	private long start;
	@ApiModelProperty(value = "结束时间")
	private long end;
	@ApiModelProperty(value = "委托状态 : 启用 or 停用")
	private String statuz;
	@ApiModelProperty(value = "委托原因")
	private String reason;
	
	public Wrapper<T> makeQueryWrapper() {
		
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(business)) {
			queryWrapper.like("BUSINESS", business);
		}
		if (ChkUtil.isNotNull(title)) {
			queryWrapper.like("TITLE", title);
		}
		if (ChkUtil.isNotNull(user)) {
			queryWrapper.like("USER_NAME", user);
		}
		if (ChkUtil.isNotNull(userd)) {
			queryWrapper.like("USERD_NAME", userd);
		}
		if (ChkUtil.isNotNull(start)&&start!=0) {
			queryWrapper.ge("START", start);
		}
		if (ChkUtil.isNotNull(end)&&end!=0) {
			queryWrapper.le("END", end);
		}
		if (ChkUtil.isNotNull(statuz)) {
			queryWrapper.like("STATUZ", statuz);
		}
		if (ChkUtil.isNotNull(reason)) {
			queryWrapper.like("REASON", reason);
		}

		return queryWrapper;
	}

}
