package com.ydw.oa.wkflow.business_main.reject.entity;

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
 * 
 * </p>
 *
 * @author hxj
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_reject")
@ApiModel(value = "Reject对象", description = "")
public class Reject implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
	private String objectId;

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableLogic(value = "0", delval = "1")
	@TableField("IS_DELETED")
	private int isDeleted;

	@ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "流程id")
	@TableField("PID")
	private String pid;

	@ApiModelProperty(value = "发起人id")
	@TableField("START_USR_ID")
	private String startUsrId;

	@ApiModelProperty(value = "发起时间")
	@TableField("START_TIME")
	private Date startTime;

	@ApiModelProperty(value = "被驳回任务id（发起人的任务id）")
	@TableField("TASK_ID")
	private String taskId;

	@ApiModelProperty(value = "驳回操作人id（领导）")
	@TableField("REJECT_USR_ID")
	private String rejectUsrId;

	@ApiModelProperty(value = "驳回时间（创建时间）")
	@TableField("REJECT_TIME")
	private Date rejectTime;

	@ApiModelProperty(value = "驳回类型（局部 or 整体 or 撤销）")
	@TableField("REJECT_TYPE")
	private String rejectType;

	@ApiModelProperty(value = "驳回原因")
	@TableField("REASON")
	private String reason;

}
