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
public class CarQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "车牌号")
	private String carno;
	@ApiModelProperty(value = "状态：在库 or  使用  or  报废")
	private String statuz;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(carno)) {
			queryWrapper.like("CARNO", carno);
		}
		if (ChkUtil.isNotNull(statuz)) {
			queryWrapper.eq("STATUZ", statuz);
		}
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}
}
