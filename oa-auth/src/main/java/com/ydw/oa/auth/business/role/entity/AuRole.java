package com.ydw.oa.auth.business.role.entity;

import java.io.Serializable;
import java.sql.Clob;
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
 *  AuRole 系统角色表
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_AU_ROLE")
@ApiModel(value="AuRole 系统角色表", description="")
public class AuRole implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "OBJECT_ID", type = IdType.UUID)
    private String objectId;

	@ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "角色描述")
    @TableField("DESCRIPTION")
    private String description;

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
    @TableField("IS_DELETED")
	@TableLogic(value = "0", delval = "1")
    private int isDeleted;

	@ApiModelProperty(value = "最后更新时间")
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime;

	@ApiModelProperty(value = "角色名称")
    @TableField("NAME")
    private String name;

	@ApiModelProperty(value = "角色ID")
    @TableField("ROLE_ID")
    private String roleId;

	@ApiModelProperty(value = "状态(0:正常;1:停用)")
    @TableField("STATUS")
    private int status;

	@ApiModelProperty(value = "扩展内容")
    @TableField("VARIABLE")
    private Clob variable;
	
	@ApiModelProperty(value = "同级显示顺序")
	@TableField("SHOW_ORDERS")
	private int showOrders;


}
