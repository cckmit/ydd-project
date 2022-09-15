package com.ydw.oa.auth.business.reception.dto;

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
@ApiModel(value = "ReceptionRecordQuery表", description = "")
public class ReceptionRecordQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "接待方式（住宿 or 就餐）")
	private String style;
	@ApiModelProperty(value = "接待部门")
	private String deptName;
	@ApiModelProperty(value = "是否会签： 待会签 or 已会签")
	private String mutiReview;
	@ApiModelProperty(value = "创建时间-开始时间")
	private String startTime;
	@ApiModelProperty(value = "创建时间-结束时间")
	private String endTime;
	@ApiModelProperty(value = "接待月份")
	private String month;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(style)) {
			queryWrapper.eq("STYLE", style);
		}
		if (ChkUtil.isNotNull(deptName)) {
			queryWrapper.eq("DEPT_NAME", deptName);
		}
		if (ChkUtil.isNotNull(mutiReview)) {
			queryWrapper.eq("MUTI_REVIEW", mutiReview);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("CREATE_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("CREATE_TIME", endTime);
		}
		if (ChkUtil.isNotNull(month)) {
			queryWrapper.eq("MONTH", month);
		}
		queryWrapper.orderByAsc("CREATE_TIME");
		return queryWrapper;
	}

}
