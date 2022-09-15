package com.ydw.oa.auth.business.wage.entity;

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
@ApiModel(value = "PersWageLog对象", description = "")
public class PersWageLog implements Serializable {

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

	@ApiModelProperty(value = "人员id")
	@TableField("USR_ID")
	private String usrId;

	@ApiModelProperty(value = "人员姓名")
	@TableField("REAL_NAME")
	private String realName;

	@ApiModelProperty(value = "基本工资")
	@TableField("BASE_WAGE")
	private BigDecimal baseWage;

	@ApiModelProperty(value = "实际工资")
	@TableField("REAL_WAGE")
	private BigDecimal realWage;

	@ApiModelProperty(value = "发放日期")
	@TableField("SEND_TIME")
	private Date sendTime;

	@ApiModelProperty(value = "工资月份： 如 2020-07")
	@TableField("WAGE_MONTH")
	private Date wageMonth;

}
