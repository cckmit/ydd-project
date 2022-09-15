package com.ydw.oa.auth.business.car.dto;

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
@ApiModel(value = "CarLogQuery 车辆记录", description = "")
public class CarBreakerQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "操作人员")
	private String usrName;
	@ApiModelProperty(value = "类型： 待处理  or  已处理")
	private String type;
	@ApiModelProperty(value = "车牌号")
	private String carno;
	@ApiModelProperty(value = "违章开始时间")
	private Date startTime;
	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrName)) {
			queryWrapper.like("USR_NAME", usrName);
		}
		if (ChkUtil.isNotNull(type)) {
			queryWrapper.eq("TYPE", type);
		}
		if (ChkUtil.isNotNull(carno)) {
			queryWrapper.like("CARNO", carno);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("BREAK_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("BREAK_TIME", endTime);
		}
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}
}
