package com.ydw.oa.auth.business.log.dto;

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
@ApiModel(value = "OperateLogQuery 日志", description = "")
public class OperateLogQuery<T> extends BaseVueQuery<T>  {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "操作人员")
	private String usrName;
	@ApiModelProperty(value = "操作内容")
	private String operate;
	@ApiModelProperty(value = "操作类型")
	private String type;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrName)) {
			queryWrapper.like("USR_NAME", usrName);
		}
		if (ChkUtil.isNotNull(operate)) {
			queryWrapper.like("OPERATE", operate);
		}
		if (ChkUtil.isNotNull(type)) {
			queryWrapper.like("TYPE", type);
		}
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}
	
}
