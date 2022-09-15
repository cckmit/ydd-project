package com.ydw.oa.auth.business.usr.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * AuUsrRole 用户 - 角色表
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_AU_USR_ROLE")
@ApiModel(value="AuUsrRole 用户 - 角色表", description="")
public class AuUsrRole implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "OBJECT_ID", type = IdType.UUID)
    private String objectId;

	@ApiModelProperty(value = "角色ID")
    @TableField("ROLE_ID")
    private String roleId;

	@ApiModelProperty(value = "用户ID")
    @TableField("USR_ID")
    private String usrId;


}
