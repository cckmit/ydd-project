package com.ydw.oa.auth.business.wk.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;

import com.ydw.oa.auth.util.SessionTool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ScheduleQuery 对象", description = "")
public class WkQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "类型 计划：周计划 or 月计划 or 季度计划  or 年计划 ||  总结： 周总结 or 日总结")
	private String type;
	@ApiModelProperty(value = "内容")
	private String content;
	@ApiModelProperty(value = "人员 计划 or 总结 ")
	private String usrid;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrid)){
			queryWrapper.eq("USR_ID",usrid);
		}
		if (ChkUtil.isNotNull(type)) {
			queryWrapper.eq("TYPE", type);
		}
		if (ChkUtil.isNotNull(content)) {
			queryWrapper.like("CONTENT", content);
		}
		queryWrapper.eq("USR_ID", SessionTool.getSessionAdminId());
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}

}
