package com.ydw.oa.auth.business.doc.dto;

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
@ApiModel(value = "DocFileQuery", description = "")
public class DocFileQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "上传人员")
    private String realName;
    @ApiModelProperty(value = "上传部门")
    private String deptName;
    @ApiModelProperty(value = "开始时间")
	private String startTime;
	@ApiModelProperty(value = "结束时间")
	private String endTime;
	@ApiModelProperty(value = "上传人员id")
	private String createUserId;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("t.NAME", name);
		}
		if (ChkUtil.isNotNull(realName)) {
			queryWrapper.eq("t1.REAL_NAME", realName);
		}
		if (ChkUtil.isNotNull(deptName)) {
			queryWrapper.eq("t2.NAME", deptName);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("t.CREATE_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("t.CREATE_TIME", endTime);
		}
		if (ChkUtil.isNotNull(createUserId)) {
			queryWrapper.eq("t.CREATE_USR_ID", createUserId);
		}
		queryWrapper.eq("t.IS_DELETED",0);
		queryWrapper.orderByDesc("t.CREATE_TIME");
		queryWrapper.groupBy("t.OBJECT_ID");
		return queryWrapper;
	}

}
