package com.ydw.oa.auth.business.notice.entity;

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
 * @since 2020-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_notice")
@ApiModel(value="Notice对象", description="")
public class Notice implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "OBJECT_ID", type = IdType.UUID)
    private String objectId;

	@ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
    @TableField("IS_DELETED")
	@TableLogic(value = "0", delval = "1")
    private int isDeleted;

	@ApiModelProperty(value = "标题")
    @TableField("TITLE")
    private String title;

	@ApiModelProperty(value = "接受者类型：集团  or 部门")
    @TableField("RECEIVE_TYPE")
    private String receiveType;

	@ApiModelProperty(value = "接受部门ids")
    @TableField("RECEIVE_DEPT_ID")
    private String receiveDeptId;

	@ApiModelProperty(value = "上传附件id")
    @TableField("FILE_ID")
    private String fileId;

	@ApiModelProperty(value = "通知内容")
    @TableField("CONTENT")
    private String content;

	@ApiModelProperty(value = "是否提交")
    @TableField("IS_SUBMIT")
    private String isSubmit;
}
