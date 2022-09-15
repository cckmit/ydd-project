package com.ydw.oa.auth.business.asset.dto;

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
@ApiModel(value = "AssetLogQuery对象", description = "")
public class AssetLogQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "操作类型")
	private String operate;
	@ApiModelProperty(value = "操作人员")
	private String realName;
	@ApiModelProperty(value = "固定资产id")
	private String contentId;
	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(operate)) {
			queryWrapper.like("t.OPERATE", operate);
		}
		if (ChkUtil.isNotNull(realName)) {
			queryWrapper.like("t1.REAL_NAME", realName);
		}
		if (ChkUtil.isNotNull(contentId)) {
			queryWrapper.like("t.CONTENT_ID", contentId);
		}
		queryWrapper.eq("t.IS_DELETED",0);
		queryWrapper.orderByDesc("t.CREATE_TIME");
		return queryWrapper;
	}

}
