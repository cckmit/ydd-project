package com.ydw.oa.auth.business.suggestions.dto;

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
@ApiModel(value = "SuggestionsQuery 意见反馈表", description = "")
public class SuggestionsQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "登陆名")
	private String usr_name;
	@ApiModelProperty(value = "反馈内容")
	private String note;
	@ApiModelProperty(value = "反馈开始时间")
	private long start_time;
	@ApiModelProperty(value = "结束时间")
	private long end_time;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();

		if (ChkUtil.isNotNull(usr_name)) {
			queryWrapper.like("U.USR_NAME", usr_name);
		}

		if (ChkUtil.isNotNull(note)) {
			queryWrapper.like("S.NOTE", note);
		}
		if (ChkUtil.isNotNull(start_time) && start_time != 0) {
			queryWrapper.ge("S.CREATED_TIME", start_time);
		}
		if (ChkUtil.isNotNull(end_time) && end_time != 0) {
			queryWrapper.le("S.CREATED_TIME", end_time);
		}

		queryWrapper.eq(true, "S.IS_DELETE", 0);
		queryWrapper.orderByDesc("S.CREATED_TIME");
		return queryWrapper;
	}

}
