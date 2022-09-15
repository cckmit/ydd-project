package com.ydw.oa.auth.business.dict.entity;

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
 * @since 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dict_type")
@ApiModel(value = "SysDictType对象", description = "")
public class SysDictType implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "编号")
	@TableField("CODE")
	private String code;

	@ApiModelProperty(value = "字典表名称")
	@TableField("NAME")
	private String name;

	@ApiModelProperty(value = "字典描述")
	@TableField("NOTE")
	private String note;

	@ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
	private String objectId;

	@ApiModelProperty(value = "状态  预留")
	@TableField("STATUS")
	private int status;

	@ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "创建人  预留")
	@TableField("CREATER")
	private String creater;

	@ApiModelProperty(value = "是否多选  预留")
	@TableField("IS_MULTILEVEL")
	private String isMultilevel;

	@ApiModelProperty(value = "修改时间  预留")
	@TableField("UPDATED")
	private Date updated;

	@ApiModelProperty(value = "修改人员  预留")
	@TableField("UPDATER")
	private String updater;

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableLogic(value = "0", delval = "1")
	@TableField("IS_DELETED")
	private int isDeleted;

	@ApiModelProperty(value = "描述  预留")
	@TableField("DESCRIPTION")
	private String description;

	@ApiModelProperty(value = "图标")
	@TableField("ICON")
	private String icon;
	
	@ApiModelProperty(value = "同级显示顺序")
	@TableField("SHOW_ORDERS")
	private int showOrders;

}
