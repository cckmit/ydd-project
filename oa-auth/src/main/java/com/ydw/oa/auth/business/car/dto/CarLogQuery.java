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
public class CarLogQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "操作人员")
	private String usrName;
	@ApiModelProperty(value = "类型： 申请  or  使用  or  维护 or  保养  or  归还")
	private String type;
	@ApiModelProperty(value = "车牌号")
	private String carno;
	@ApiModelProperty(value = "申请的开始时间")
	private Date startTime;
	@ApiModelProperty(value = "申请的结束时间")
	private Date endTime;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrName)) {
			queryWrapper.like("USR_NAME", usrName);
		}
		if (ChkUtil.isNotNull(carno)) {
			queryWrapper.like("CARNO", carno);
		}
		if (ChkUtil.isNotNull(type)) {
			queryWrapper.eq("TYPE", type);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("START_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("END_TIME", endTime);
		}
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}
}
