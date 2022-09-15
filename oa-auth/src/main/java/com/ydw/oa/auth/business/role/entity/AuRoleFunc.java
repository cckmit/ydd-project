package com.ydw.oa.auth.business.role.entity;

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
 * AuRoleFunc对象
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_AU_ROLE_FUNC")
@ApiModel(value="AuRoleFunc对象", description="")
public class AuRoleFunc implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "OBJECT_ID", type = IdType.UUID)
    private String objectId;

	@ApiModelProperty(value = "功能ID")
    @TableField("FUNC_ID")
    private String funcId;

	@ApiModelProperty(value = "角色ID")
    @TableField("ROLE_ID")
    private String roleId;


}
