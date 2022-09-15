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
 * 收件箱
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("msg_inbox")
@ApiModel(value = "MsgInbox对象", description = "")
public class MsgInbox implements Serializable {

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

	@ApiModelProperty(value = "收件人")
	@TableField("USR_ID")
	private String usrId;
	
	@ApiModelProperty(value = "是否阅读(未阅读 or 已阅读)")
	@TableField("IS_READ")
	private String isRead;

	@ApiModelProperty(value = "发件id")
	@TableField("OUTBOX_ID")
	private String outboxId;

	@ApiModelProperty(value = "阅读时间")
	@TableField("READ_TIME")
	private Date readTime;

	@ApiModelProperty(value = "状态：未回复 已回复 已驳回 已完成", notes = "收件人回复->已回复，发件人审批通过->已完成，驳回->已驳回")
	@TableField("STATUZ")
	private String statuz;

}
