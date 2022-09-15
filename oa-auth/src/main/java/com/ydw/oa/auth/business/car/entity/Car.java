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
@TableName("t_car")
@ApiModel(value="Car对象", description="")
public class Car implements Serializable {

    private static final long serialVersionUID=1L;

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

    @ApiModelProperty(value = "内部编号")
    @TableField("BUS_NO")
    private String busNo;

    @ApiModelProperty(value = "车架号")
    @TableField("FRAME_NO")
    private String frameNo;

    @ApiModelProperty(value = "发动机号")
    @TableField("ENGINE_NO")
    private String engineNo;

    @ApiModelProperty(value = "载客数量")
    @TableField("NUMBER")
    private String number;

    @ApiModelProperty(value = "状态：在库 or  使用  or  报废")
    @TableField("STATUZ")
    private String statuz;

    @ApiModelProperty(value = "购买时间")
    @TableField("BUY_TIME")
    private Date buyTime;


}
