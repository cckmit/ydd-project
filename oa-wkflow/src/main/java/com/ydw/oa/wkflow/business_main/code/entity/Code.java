package com.ydw.oa.wkflow.business_main.code.entity;

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
 * @author hxj
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_code")
@ApiModel(value = "Code对象", description = "")
public class Code implements Serializable {

	private static final long serialVersionUID = 1L;

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

	@ApiModelProperty(value = "编码类型")
	@TableField("REVIEW_TYPE")
	private String reviewType;

	@ApiModelProperty(value = "编码生成时间")
	@TableField("DATE_INFO")
	private String dateInfo;

	@ApiModelProperty(value = "编码数量")
	@TableField("NUM")
	private int num;

}
