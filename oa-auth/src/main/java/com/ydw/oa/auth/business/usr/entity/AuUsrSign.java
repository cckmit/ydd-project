package com.ydw.oa.auth.business.usr.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @since 2020-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_au_usr_sign")
@ApiModel(value="AuUsrSign对象", description="")
public class AuUsrSign implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "OBJECT_ID", type = IdType.UUID)
    private String objectId;

    @ApiModelProperty(value = "标签集合——董事长：chairman 总经理：manager  分管领导：leader 部门负责人：admin 政治（综合）工作部主任：director 办公室：office 审计：audit 工会：union 纪检：supervision 财务：finance")
    @TableField("SIGN")
    private String sign;

    @ApiModelProperty(value = "人员id")
    @TableField("USR_ID")
    private String usrId;


}
