package com.ydw.oa.auth.business.recruit.dto;

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
@ApiModel(value = "PersRecruitQuery对象", description = "")
public class PersRecruitPersonQuery<T> extends BaseVueQuery<T> {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "应聘人姓名")
	private String realName;
	@ApiModelProperty(value = "应聘岗位")
	private String recruitName;
	@ApiModelProperty(value = "是否录取  是 or 否")
	private String isEnroll;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(realName)) {
			queryWrapper.like("REAL_NAME", realName);
		}
		if (ChkUtil.isNotNull(recruitName)) {
			queryWrapper.like("RECRUIT_NAME", recruitName);
		}
		if (ChkUtil.isNotNull(isEnroll)) {
			queryWrapper.eq("IS_ENROLL", isEnroll);
		}
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}
}
