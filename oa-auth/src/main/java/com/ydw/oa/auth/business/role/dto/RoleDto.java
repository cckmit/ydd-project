package com.ydw.oa.auth.business.role.dto;

import java.sql.Clob;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "RoleDto 对象", description = "")
public class RoleDto {

	@ApiModelProperty(value = "主键ID")
	private String objectId;
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	@ApiModelProperty(value = "角色描述")
	private String description;
	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	private int isDeleted;
	@ApiModelProperty(value = "最后更新时间")
	private Date lastUpdateTime;
	@ApiModelProperty(value = "角色名称")
	private String name;
	@ApiModelProperty(value = "角色ID")
	private String roleId;
	@ApiModelProperty(value = "状态(0:正常;1:停用)")
	private int status;
	@ApiModelProperty(value = "扩展内容")
	private Clob variable;

	@ApiModelProperty(value = "菜单编号,逗号分隔")
	private String menus;

	@ApiModelProperty(value = "同级显示顺序")
	private int showOrders;
}
