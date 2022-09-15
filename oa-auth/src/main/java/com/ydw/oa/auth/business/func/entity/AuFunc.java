package com.ydw.oa.auth.business.func.entity;

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
 * AuFunc 系统功能表
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_AU_FUNC")
@ApiModel(value = "AuFunc 系统功能表", description = "")
public class AuFunc implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
	private String objectId;

	@ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "功能描述")
	@TableField("DESCRIPTION")
	private String description;

	@ApiModelProperty(value = "功能样式")
	@TableField("FUNC_ICON")
	private String funcIcon;

	@ApiModelProperty(value = "功能ID")
	@TableField("FUNC_ID")
	private String funcId;

	@ApiModelProperty(value = "功能名称")
	@TableField("FUNC_NAME")
	private String funcName;

	@ApiModelProperty(value = "功能类型(0:页面;1:按钮)")
	@TableField("FUNC_TYPE")
	private int funcType;

	@ApiModelProperty(value = "COMMENT 功能URL,使用\"|\"分割多个请求")
	@TableField("FUNC_URLS")
	private String funcUrls;

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableField("IS_DELETED")
	@TableLogic(value = "0", delval = "1")
	private int isDeleted;

	@ApiModelProperty(value = "最后更新时间")
	@TableField("LAST_UPDATE_TIME")
	private Date lastUpdateTime;

	@ApiModelProperty(value = "父功能ID")
	@TableField("PARENT_ID")
	private String parentId;

	@ApiModelProperty(value = "同级显示顺序")
	@TableField("SHOW_ORDERS")
	private int showOrders;

	@ApiModelProperty(value = "状态(0:显示;1:隐藏)")
	@TableField("STATUS")
	private int status;

	@ApiModelProperty(value = "扩展内容")
	@TableField("VARIABLE")
	private Clob variable;

	@ApiModelProperty(value = "是否缓存: 1：不缓存 or 0：缓存")
	@TableField("KEEP_ALIVE")
	private boolean keepAlive;

	@TableField(exist = false)
	private List<AuFunc> children;
	
	@TableField(exist = false)
	private String role;
}
