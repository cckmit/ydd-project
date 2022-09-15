package com.ydw.oa.wkflow.business_main.reject.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RejectQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "任务id")
	private String task_id;
	@ApiModelProperty(value = "任务名称")
	private String taskName;
	@ApiModelProperty(value = "流程名称")
	private String procdefName;
	@ApiModelProperty(value = "驳回人姓名")
	private String realName;
	@ApiModelProperty(value = "驳回类型（局部 or 整体 or 撤销）")
	private String rejectType;
	@ApiModelProperty(value = "发起人id")
	private String startUsrId;
	@ApiModelProperty(value = "驳回人id")
	private String rejectusrid;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(task_id)) {
			queryWrapper.eq("t.TASK_ID", task_id);
		}
		if (ChkUtil.isNotNull(taskName)) {
			queryWrapper.like("t1.NAME_", taskName);
		}
		if (ChkUtil.isNotNull(procdefName)) {
			queryWrapper.like("t2.NAME_", procdefName);
		}
		if (ChkUtil.isNotNull(realName)) {
			queryWrapper.like("t3.REAL_NAME", realName);
		}
		if (ChkUtil.isNotNull(rejectType)) {
			queryWrapper.eq("t.REJECT_TYPE", rejectType);
		}
		if (ChkUtil.isNotNull(startUsrId)) {
			queryWrapper.eq("t.START_USR_ID", startUsrId);
		}
		if(ChkUtil.isNotNull(rejectusrid)){
			queryWrapper.eq("REJECT_USR_ID",rejectusrid);
		}
		queryWrapper.eq("t.IS_DELETED", 0);
		queryWrapper.orderByDesc("t.CREATE_TIME");
		return queryWrapper;
	}
	public Wrapper<T> makeQueryWrapper2() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(task_id)) {
			queryWrapper.eq("t.TASK_ID", task_id);
		}
		if (ChkUtil.isNotNull(taskName)) {
			queryWrapper.like("t1.NAME_", taskName);
		}
		if (ChkUtil.isNotNull(procdefName)) {
			queryWrapper.like("t2.NAME_", procdefName);
		}
		if (ChkUtil.isNotNull(realName)) {
			queryWrapper.like("t3.REAL_NAME", realName);
		}
		if (ChkUtil.isNotNull(rejectType)) {
			queryWrapper.eq("t.REJECT_TYPE", rejectType);
		}
		if (ChkUtil.isNotNull(startUsrId)) {
			queryWrapper.eq("t.START_USR_ID", startUsrId);
		}
		if(ChkUtil.isNotNull(rejectusrid)){
			queryWrapper.eq("REJECT_USR_ID",rejectusrid);
		}
		queryWrapper.eq("t.REJECT_TYPE","局部");
		queryWrapper.eq("t.IS_DELETED", 0);
		queryWrapper.orderByDesc("t.CREATE_TIME");
		return queryWrapper;
	}

}
