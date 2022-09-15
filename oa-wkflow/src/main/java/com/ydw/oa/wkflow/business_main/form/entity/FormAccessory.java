package com.ydw.oa.wkflow.business_main.form.entity;

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
 * 表单附件（表单模板）
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_form_accessory")
@ApiModel(value="FormAccessory对象", description="")
public class FormAccessory implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "OBJECT_ID", type = IdType.UUID)
    private String objectId;

    @ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableLogic(value = "0", delval = "1")
    @TableField("IS_DELETED")
    private int isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime = new Date(System.currentTimeMillis());

    @ApiModelProperty(value = "表单ID")
    @TableField("FORM_ID")
    private String formId;

    @ApiModelProperty(value = "模板名称")
    @TableField("FILE_NAME")
    private String fileName;

    @ApiModelProperty(value = "模板文件ID")
    @TableField("FILE_ID")
    private String fileId;

    @ApiModelProperty(value = "排序")
    @TableField("SORTZ")
    private int sortz;

    @ApiModelProperty(value = "是否必须上传： 是 or 否")
    @TableField("IS_REQUIRED")
    private String isRequired;


}
