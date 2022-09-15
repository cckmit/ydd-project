package com.ydw.oa.auth.business.car.dto;

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
public class CarUseQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "车牌号")
    private String carno;
	@ApiModelProperty(value = "驾驶员")
    private String user;
	@ApiModelProperty(value = "用车部门")
    private String depert;
	@ApiModelProperty(value = "出车状态： 待出车、已出车、已还车")
    private String statuz;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(carno)) {
			queryWrapper.like("CARNO", carno);
		}
		if (ChkUtil.isNotNull(user)) {
			queryWrapper.like("USER", user);
		}
		if (ChkUtil.isNotNull(depert)) {
			queryWrapper.like("DEPERT", depert);
		}
		if (ChkUtil.isNotNull(statuz)) {
			queryWrapper.eq("STATUZ", statuz);
		}
		queryWrapper.eq("IS_DELETED",0);
		queryWrapper.orderByDesc("STATUZ","CREATE_TIME");
		return queryWrapper;
	}
}
