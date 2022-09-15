package com.ydw.oa.auth.business.wage.dto;

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
@ApiModel(value = "PersWageLogQuery对象", description = "")
public class PersWageLogQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "人员姓名")
	private String realName;
	@ApiModelProperty(value = "工资月份： 如  7月工资")
	private String wageMonth;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(realName)) {
			queryWrapper.like("REAL_NAME", realName);
		}
		if (ChkUtil.isNotNull(wageMonth)) {
			queryWrapper.like("WAGE_MONTH", wageMonth);
		}
		queryWrapper.orderByAsc("WAGE_MONTH", "REAL_NAME");
		return queryWrapper;
	}
}
