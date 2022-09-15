package com.ydw.oa.auth.business.msg.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MsgDraftbox对象", description = "")
public class MsgDraftboxQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "标题")
	private String title;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(title)) {
			queryWrapper.like("TITLE", title);
		}
		queryWrapper.eq("a.USR_ID", SessionTool.getSessionAdminId());
		queryWrapper.eq("a.IS_DELETED", 0);
		queryWrapper.orderByDesc("a.CREATE_TIME");
		return queryWrapper;
	}
}
