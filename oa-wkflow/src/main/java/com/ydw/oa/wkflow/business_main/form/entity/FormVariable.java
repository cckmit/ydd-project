package com.ydw.oa.wkflow.business_main.form.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

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
 * @since 2020-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_form_variable")
@ApiModel(value="FormVariable对象", description="")
public class FormVariable implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "OBJECT_ID", type = IdType.UUID)
    private String objectId;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime = new Date(System.currentTimeMillis());

    @ApiModelProperty(value = "是否被删除(0:否;1:是)")
   	@TableLogic(value = "0", delval = "1")
    @TableField("IS_DELETED")
    private int isDeleted;

    @ApiModelProperty(value = "模板id")
    @TableField("MODEL_ID")
    private String modelId;

    @ApiModelProperty(value = "模板节点id")
    @TableField("RESOURCE_ID")
    private String resourceId;

    @ApiModelProperty(value = "办理时长")
    @TableField("HANDLE_TIME")
    private String handleTime;

    @ApiModelProperty(value = "超时处理方式")
    @TableField("HANDLE_TYPE")
    private String handleType;

    @ApiModelProperty(value = "表单id")
    @TableField("FORM_ID")
    private String formId;

    @ApiModelProperty(value = "是否第一个节点： 是 or 否")
    @TableField("IS_FIRST")
    private String isFirst;
}
