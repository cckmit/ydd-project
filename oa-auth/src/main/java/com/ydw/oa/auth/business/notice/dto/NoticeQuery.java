package com.ydw.oa.auth.business.notice.dto;

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
@ApiModel(value = "NoticeQuery 部门表", description = "")
public class NoticeQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "标题")
	private String title;
	@ApiModelProperty(value = "接受者类型：集团  or 部门")
	private String receiveType;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(title)) {
			queryWrapper.like("TITLE", title);
		}
		if (ChkUtil.isNotNull(receiveType)) {
			queryWrapper.eq("RECEIVE_TYPE", receiveType);
		}
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}

}
