package com.ydw.oa.auth.business.attendance.dto;

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
@ApiModel(value = "PersAttendanceLogQuery", description = "")
public class PersAttendanceLogQuery<T> extends BaseVueQuery<T> {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "月份")
	private String month;

	@ApiModelProperty(value = "用户id")
	private String userId;

	@ApiModelProperty(value = "状态")
	private String statuz;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(month)) {
			queryWrapper.eq("MONTH", month);
		}
		if (ChkUtil.isNotNull(userId)) {
			queryWrapper.eq("USR_ID", userId);
		}
		if (ChkUtil.isNotNull(statuz)) {
			queryWrapper.eq("STATUZ", statuz);
		}

		queryWrapper.orderByAsc("DAY");
		return queryWrapper;
	}
}
