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
public class PersAttendanceRuleQuery<T> extends BaseVueQuery<T> {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "类型（加分、扣分）")
	private String type;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("NAME", name);
		}
		if (ChkUtil.isNotNull(type)) {
			queryWrapper.eq("TYPE", type);
		}
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}
}
