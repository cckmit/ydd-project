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
 * 收件
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MsgInbox对象", description = "")
public class MsgInboxQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "标题")
	private String title;
	@ApiModelProperty(value = "发件人")
	private String name;
	@ApiModelProperty(value = "回复状态")
	private String status;
	@ApiModelProperty(value = "开始时间")
	private String startTime;
	@ApiModelProperty(value = "结束时间")
	private String endTime;
	@ApiModelProperty(value = "阅读状态")
	private String isRead;


	
	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(title)) {
			queryWrapper.like("TITLE", title);
		}
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("g.REAL_NAME", name);
		}
		if (ChkUtil.isNotNull(status)) {
			String[] split = status.split(",");
			if(split.length==1){
				queryWrapper.eq("t.STATUZ", split[0]);
			}else{
				queryWrapper.in("t.STATUZ", split);
			}
		}
		if(ChkUtil.isNotNull(isRead)){
			queryWrapper.eq("t.IS_READ",isRead);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("t.CREATE_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("t.CREATE_TIME", endTime);
		}
		queryWrapper.eq("t.IS_DELETED", 0);
		queryWrapper.eq("t.USR_ID", SessionTool.getSessionAdminId());
		queryWrapper.orderByDesc("t.IS_READ","FIELD(t.STATUZ,'未回复','已驳回')","t.CREATE_TIME");
		return queryWrapper;
	}

}
