package com.ydw.oa.auth.business.train.dto;

import java.util.Date;

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
@ApiModel(value = "PersTrainPlanLogQuery", description = "")
public class PersTrainPlanQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "培训计划标题")
	private String title;
	@ApiModelProperty(value = "开始时间")
	private Date startTime;
	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(title)) {
			queryWrapper.like("TITLE", title);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("START_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("START_TIME", endTime);
		}
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}
}
