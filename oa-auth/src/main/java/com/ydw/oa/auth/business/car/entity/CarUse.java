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
 * @since 2020-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_car_use")
@ApiModel(value = "CarUse对象", description = "")
public class CarUse implements Serializable {

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

	@ApiModelProperty(value = "车id")
	@TableField("CAR_ID")
	private String carId;

	@ApiModelProperty(value = "车牌号")
	@TableField("CARNO")
	private String carno;

	@ApiModelProperty(value = "驾驶员id")
	@TableField("USR_ID")
	private String usrId;

	@ApiModelProperty(value = "驾驶员")
	@TableField("USER")
	private String user;

	@Deprecated
	@ApiModelProperty(value = "用车部门id")
	@TableField("DEPT_ID")
	private String deptId;

	@ApiModelProperty(value = "用车部门")
	@TableField("DEPERT")
	private String depert;

	@ApiModelProperty(value = "出车时间")
	@TableField("USE_TIME")
	private Date useTime;

	@ApiModelProperty(value = "出车状态： 待出车、已出车、已还车")
	@TableField("STATUZ")
	private String statuz;

	@ApiModelProperty(value = "备注")
	@TableField("NOTE")
	private String note;

	@ApiModelProperty(value = "出发地点")
	@TableField("START_ADDR")
	private String startAddr;

	@ApiModelProperty(value = "目的地")
	@TableField("END_ADDR")
	private String endAddr;

	@ApiModelProperty(value = "流程实例id")
	@TableField("PID")
	private String pid;

	@ApiModelProperty(value = "领钥匙时间")
	@TableField("KEY_GET_TIME")
	private Date keyGetTime;

	@ApiModelProperty(value = "还钥匙时间")
	@TableField("KEY_BACK_TIME")
	private Date keyBackTime;

	@ApiModelProperty(value = "补助标准 1（50公里内） or  2（50-100公里内） or  3（100公里外非当日往返） or  4（100公里外当日往返）")
	@TableField("STANDARD_TYPE")
	private int standardType = -1;

}
