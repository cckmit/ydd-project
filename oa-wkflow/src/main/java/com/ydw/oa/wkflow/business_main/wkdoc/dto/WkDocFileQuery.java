package com.ydw.oa.wkflow.business_main.wkdoc.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.util.SessionTool;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 我的流程文档
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WkDocFileQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "文档编号")
	private String code;
	@ApiModelProperty(value = "文档名称")
	private String name;
	@ApiModelProperty(value = "流程分类")
	private String category;
	
	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(code)) {
			queryWrapper.like("t.CODE", code);
		}
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("t.NAME", name);
		}
		if (ChkUtil.isNotNull(category)) {
			queryWrapper.like("t1.TYPE", category);
		}
		queryWrapper.eq("t4.USR_ID", SessionTool.getSessionAdminId());
		queryWrapper.eq("t.IS_DELETED", 0);
//		queryWrapper.orderByDesc("t.CREATE_TIME");
		queryWrapper.orderByDesc("t.CODE");
		queryWrapper.orderByDesc("RIGHT(t.CODE,5)");
//		ORDER BY t.CODE DESC,RIGHT(t.`CODE`,5)DESC
		return queryWrapper;
	}

}
