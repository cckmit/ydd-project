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
@ApiModel(value = "DocFileDownloadQuery", description = "")
public class DocFileDownloadQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "下载人员名称_下载详情")
    private String realName;
	@ApiModelProperty(value = "下载状态_下载详情")
    private String downloadStatus;
	@ApiModelProperty(value = "文档名称_我的文档")
    private String fileName;
	@ApiModelProperty(value = "开始时间_我的文档")
	private String startTime;
	@ApiModelProperty(value = "结束时间_我的文档")
	private String endTime;
	@ApiModelProperty(value = "文件id_下载详情")
	private String docFileId;
	@ApiModelProperty(value = "人员id_我的文档_默认赋值")
	private String usrId;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(realName)) {
			queryWrapper.like("t2.REAL_NAME", realName);
		}
		if (ChkUtil.isNotNull(fileName)) {
			queryWrapper.like("t1.NAME", fileName);
		}
		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("t1.CREATE_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("t1.CREATE_TIME", endTime);
		}
		if (ChkUtil.isNotNull(docFileId)) {
			queryWrapper.eq("t.DOC_FILE_ID", docFileId);
		}
		if (ChkUtil.isNotNull(usrId)) {
			queryWrapper.eq("t.USR_ID", usrId);
		}
		if (ChkUtil.isNotNull(downloadStatus)) {
			queryWrapper.eq("t.STATUZ", downloadStatus);
		}
		queryWrapper.eq("t.IS_DELETED",0);
		queryWrapper.orderByDesc("t.CREATE_TIME");
		return queryWrapper;
	}

}
