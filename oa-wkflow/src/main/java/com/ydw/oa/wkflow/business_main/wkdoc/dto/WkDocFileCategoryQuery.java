package com.ydw.oa.wkflow.business_main.wkdoc.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分类流程文档
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WkDocFileCategoryQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "文档编号")
	private String code;
	@ApiModelProperty(value = "文档名称")
	private String name;
	private String category;
	private String deptId;
	
	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(code)) {
			queryWrapper.like("t.CODE", code);
		}
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("t.NAME", name);
		}
		if (ChkUtil.isNotNull(deptId)) {
			queryWrapper.eq("t.DETP_ID", deptId);
		}
		if (ChkUtil.isNotNull(category)) {
			queryWrapper.like("t1.TYPE", category);
		}
		queryWrapper.eq("t.IS_DELETED", 0);
//		queryWrapper.orderByAsc("t.NAME");
//		queryWrapper.orderByAsc("t.CREATE_TIME");
		queryWrapper.orderByDesc("t.CODE");
		queryWrapper.orderByDesc("RIGHT(t.CODE,5)");
		return queryWrapper;
	}

}
