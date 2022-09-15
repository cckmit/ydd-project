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
@ApiModel(value = "PersAttendanceQuery", description = "")
public class PersAttendanceQuery<T> extends BaseVueQuery<T> {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "考勤月份")
	private String month;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(month)) {
			queryWrapper.eq("MONTH", month);
		}
		queryWrapper.orderByDesc("MONTH");
		return queryWrapper;
	}
}
