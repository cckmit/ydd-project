package com.ydw.oa.auth.business.attendance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

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
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PersAttendanceLog对象", description = "")
public class PersAttendanceLog implements Serializable {

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

	@ApiModelProperty(value = "考核人员id")
	@TableField("USR_ID")
	private String usrId;

	@ApiModelProperty(value = "考核月份")
	@TableField("MONTH")
	private String month;

	@ApiModelProperty(value = "考核日期")
	@TableField("DAY")
	private String day;

	@ApiModelProperty(value = "考核类型")
	@TableField("TYPE")
	private String type;

	@ApiModelProperty(value = "考核分数")
	@TableField("SCORE")
	private BigDecimal score;

	@ApiModelProperty(value = "操作")
	@TableField("OPERATION_TYPE")
	private String operationType;

	@ApiModelProperty(value = "原因")
	@TableField("NOTE")
	private String note;

	@ApiModelProperty(value = "审批状态")
	@TableField("STATUZ")
	private String statuz;
	@ApiModelProperty(value = "审批状态")
	@TableField("APPROVE_USR_ID")
	private String approveUsrId;


}
