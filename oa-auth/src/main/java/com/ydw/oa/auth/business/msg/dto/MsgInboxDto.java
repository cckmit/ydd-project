package com.ydw.oa.auth.business.msg.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 收件
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MsgInbox对象", description = "")
public class MsgInboxDto<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "发件id,发件列表中的objectId")
	private String outboxId;
	@ApiModelProperty(value = "收件人")
	private String name;
	@ApiModelProperty(value = "是否阅读(未阅读 or 已阅读)")
	private String isRead;
	
	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(outboxId)) {
			queryWrapper.eq("t.OUTBOX_ID", outboxId);
		}
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("t2.REAL_NAME", name);
		}
		queryWrapper.eq("t.IS_DELETED", 0);
		queryWrapper.orderByAsc("t2.REAL_NAME");
		return queryWrapper;
	}



}
