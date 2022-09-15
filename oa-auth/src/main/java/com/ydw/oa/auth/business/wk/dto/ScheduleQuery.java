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
public class ScheduleQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "日程状态：待完成 or  已完成")
    private String statuz;
    @ApiModelProperty(value = "日程任务")
    private String content;
    @ApiModelProperty(value = "日程创建人员")
	private String usrid;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrid)){
			queryWrapper.eq("USR_ID", usrid);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("START_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("END_TIME", endTime);
		}
		if (ChkUtil.isNotNull(statuz)) {
			queryWrapper.eq("STATUZ", statuz);
		}
		if (ChkUtil.isNotNull(content)) {
			queryWrapper.like("CONTENT", content);
		}
		queryWrapper.eq("USR_ID", SessionTool.getSessionAdminId());
		queryWrapper.orderByAsc("STATUZ");
		queryWrapper.orderByDesc("START_TIME");
		return queryWrapper;
	}

}
