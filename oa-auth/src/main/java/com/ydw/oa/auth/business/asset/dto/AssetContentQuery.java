package com.ydw.oa.auth.business.asset.dto;

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
@ApiModel(value = "AssetContentQuery对象", description = "")
public class AssetContentQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "固定资产编号")
	private String code;
	@ApiModelProperty(value = "资产名称")
	private String name;
	@ApiModelProperty(value = "固定资产分类")
	private String typeName;
	@ApiModelProperty(value = "采购时间-开始时间")
	private Date startTime;
	@ApiModelProperty(value = "采购时间-结束时间")
	private Date endTime;
//	@ApiModelProperty(value = "采购人员")
//	private String buyUsrName;

	@ApiModelProperty(value = "资产状态（在库、使用中、报废、再报废）")
	private String statuz;
	@ApiModelProperty(value = "分配部门id")
	private String currentUsrDeptId;

	@ApiModelProperty(value = "数量")
	private String number;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(code)) {
			queryWrapper.like("t.CODE", code);
		}
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("t.NAME", name);
		}
		if (ChkUtil.isNotNull(typeName)) {
			queryWrapper.like("t1.NAME", typeName);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("t.BUY_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("t.BUY_TIME", endTime);
		}
//		if (ChkUtil.isNotNull(buyUsrName)) {
//			queryWrapper.like("t3.REAL_NAME", buyUsrName);
//		}
		if (ChkUtil.isNotNull(statuz)) {
			queryWrapper.like("t.STATUZ", statuz);
		}
		if (ChkUtil.isNotNull(currentUsrDeptId)) {
			queryWrapper.eq("t.CURRENT_USR_DEPT_ID", currentUsrDeptId);
		}
		if (ChkUtil.isNotNull(number)) {
			queryWrapper.eq("t.NUMBER", number);
		}
		queryWrapper.eq("t.IS_DELETED",0);
		queryWrapper.orderByDesc("t.CREATE_TIME");
		return queryWrapper;
	}

}
