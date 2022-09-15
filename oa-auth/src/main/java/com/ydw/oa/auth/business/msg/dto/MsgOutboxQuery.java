package com.ydw.oa.auth.business.msg.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 发件
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MsgOutbox对象", description = "")
public class MsgOutboxQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "标题")
	private String title;
	@ApiModelProperty(value = "收件人")
	private String name;
	@ApiModelProperty(value = "开始时间")
	private Date startTime;
	@ApiModelProperty(value = "结束时间")
	private Date endTime;
	@ApiModelProperty(value = "阅读状态")
	private String isRead;
	@ApiModelProperty(value = "公告类型：普通公告 or  政务公开")
	private String noticeType;
	@ApiModelProperty(value = "政务公开-开始时间")
	private Date affairsStartTime;
	@ApiModelProperty(value = "政务公开-结束时间")
	private Date affairsEndTime;
	private String sendUsrId;;
	
	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(title)) {
			queryWrapper.like("t.TITLE", title);
		}
		if (ChkUtil.isNotNull(isRead)) {
			queryWrapper.eq("t.IS_READ", isRead);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("t.CREATE_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("t.CREATE_TIME", endTime);
		}
		if (ChkUtil.isNotNull(noticeType)) {
			queryWrapper.eq("t.NOTICE_TYPE", noticeType);
		}
		if (ChkUtil.isNotNull(sendUsrId)) {
			queryWrapper.eq("t.SEND_USR_ID", sendUsrId);
		}
		// 政务公开时间段查询
		if (ChkUtil.isNotNull(affairsStartTime) && ChkUtil.isNotNull(affairsEndTime)) {
			queryWrapper.ge("t.END_TIME", affairsStartTime);
			queryWrapper.le("t.START_TIME", affairsEndTime);
		}

		
		queryWrapper.eq("t.IS_DELETED", 0);
		queryWrapper.groupBy("t.OBJECT_ID");
		queryWrapper.having(ChkUtil.isNotNull(name), "receivers like {0}","%"+name+"%");
		queryWrapper.orderByDesc("t.CREATE_TIME");
		return queryWrapper;
	}
}
