package com.ydw.oa.auth.business.car.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @since 2020-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_car_subsidy_item")
@ApiModel(value = "CarSubsidyItem对象", description = "")
public class CarSubsidyItem implements Serializable {

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

	@ApiModelProperty(value = "补助id")
	@TableField("SUBSIDY_ID")
	private String subsidyId;

	@ApiModelProperty(value = "类型： 1（50公里内） or  2（50-100公里内） or  3（100公里外非当日往返） or  4（100公里外当日往返）")
	@TableField("STANDARD_TYPE")
	private int standardType;

	@ApiModelProperty(value = "补助金额")
	@TableField("STANDARD")
	private BigDecimal standard;

	@ApiModelProperty(value = "合计天数")
	@TableField("TOTAL_DAY")
	private int totalDay;

	@ApiModelProperty(value = "合计补助金额")
	@TableField("TOTAL_MONEY")
	private BigDecimal totalMoney;

}
