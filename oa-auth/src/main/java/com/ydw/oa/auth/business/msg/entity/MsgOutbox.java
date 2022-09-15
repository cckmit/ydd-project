package com.ydw.oa.auth.business.msg.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 发件箱
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("msg_outbox")
@ApiModel(value = "MsgOutbox对象", description = "")
public class MsgOutbox implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
	private String objectId;

	@ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableField("IS_DELETED")
	@TableLogic(value = "0", delval = "1")
	private int isDeleted;

	@ApiModelProperty(value = "发件人")
	@TableField("SEND_USR_ID")
	private String sendUsrId;

	@ApiModelProperty(value = "收件类型: 全部，指定人员")
	@TableField("RECEIVE_TYPE")
	private String receiveType;
	
	@ApiModelProperty(value = "收件人(若收件类型为全部，则收件人字段为空)")
	@TableField("RECEIVE_USR_ID")
	private String receiveUsrId;

	@ApiModelProperty(value = "内容")
	@TableField("CONTENT")
	private String content;

	@ApiModelProperty(value = "标题")
	@TableField("TITLE")
	private String title;

	@ApiModelProperty(value = "附件")
	@TableField("FILE_ID")
	private String fileId;

	@ApiModelProperty(value = "是否需要回复(0:否;1:是)")
	@TableField("NEED_REPLY")
	private String needReply;
	
	@ApiModelProperty(value = "公告类型：普通公告 or  政务公开")
	@TableField("NOTICE_TYPE")
	private String noticeType;
	
	@ApiModelProperty(value = "政务公开-开始时间")
	@TableField("START_TIME")
	private Date startTime;
	
	@ApiModelProperty(value = "政务公开-结束时间")
	@TableField("END_TIME")
	private Date endTime;


}
