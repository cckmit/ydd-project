package com.ydw.oa.auth.business.car.entity;

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
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_car_breaker")
@ApiModel(value = "CarBreaker对象", description = "")
public class CarBreaker implements Serializable {

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

	@ApiModelProperty(value = "车牌号")
	@TableField("CARNO")
	private String carno;

	@ApiModelProperty(value = "车id")
	@TableField("CAR_ID")
	private String carId;

	@ApiModelProperty(value = "类型： 待处理  or  已处理")
	@TableField("TYPE")
	private String type;

	@ApiModelProperty(value = "操作人员id")
	@TableField("USR_ID")
	private String usrId;

	@ApiModelProperty(value = "操作人员")
	@TableField("USR_NAME")
	private String usrName;

	@ApiModelProperty(value = "违章时间")
	@TableField("BREAK_TIME")
	private Date breakTime;

}
