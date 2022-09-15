package com.ydw.oa.wkflow.business_main.delegate.entity;

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
 * @author 冯晓东
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_delegate")
@ApiModel(value = "Delegate对象", description = "")
public class Delegate implements Serializable {

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

	@ApiModelProperty(value = "委托事项")
	@TableField("TITLE")
	private String title;

	@ApiModelProperty(value = "委托业务")
	@TableField("BUSINESS")
	private String business;

	@ApiModelProperty(value = "开始时间")
	@TableField("START")
	private long start;

	@ApiModelProperty(value = "结束时间")
	@TableField("END")
	private long end;

	@ApiModelProperty(value = "委托原因")
	@TableField("REASON")
	private String reason;

	@ApiModelProperty(value = "委托状态 : 启用 or 停用")
	@TableField("STATUZ")
	private String statuz;

	@ApiModelProperty(value = "委托人id")
	@TableField("USER")
	private String user;

	@ApiModelProperty(value = "委托人")
	@TableField("USER_NAME")
	private String userName;

	@ApiModelProperty(value = "被委托人id")
	@TableField("USERD")
	private String userd;

	@ApiModelProperty(value = "被委托人")
	@TableField("USERD_NAME")
	private String userdName;

}
