package com.ydw.oa.auth.business.dept.entity;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
import java.util.List;

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
 * AuDept 部门表
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_au_dept")
@ApiModel(value = "AuDept 部门表", description = "")
public class AuDept implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
	private String objectId;

	@ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "部门ID")
	@TableField("DEPT_ID")
	private String deptId;

	@ApiModelProperty(value = "功能描述")
	@TableField("DESCRIPTION")
	private String description;

	@ApiModelProperty(value = "单位简称")
	@TableField("DWJC")
	private String dwjc;

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableField("IS_DELETED")
	@TableLogic(value = "0", delval = "1")
	private int isDeleted;

	@ApiModelProperty(value = "部门名称")
	@TableField("NAME")
	private String name;

	@ApiModelProperty(value = "分管领导")
	@TableField("USR_ID")
	private String usrId;
	
	@ApiModelProperty(value = "部长/部门负责人")
	@TableField("DEPT_MANA_USR_ID")
	private String deptManaUsrId;

	@ApiModelProperty(value = "上级部门ID")
	@TableField("PARENT_ID")
	private String parentId;

	@ApiModelProperty(value = "扩展内容")
	@TableField("VARIABLE")
	private Clob variable;

	@ApiModelProperty(value = "行政简称")
	@TableField("XZJC")
	private String xzjc;
	
	@ApiModelProperty(value = "同级显示顺序")
	@TableField("SHOW_ORDERS")
	private int showOrders;

	@TableField(exist = false)
	private List<AuDept> children;

}
