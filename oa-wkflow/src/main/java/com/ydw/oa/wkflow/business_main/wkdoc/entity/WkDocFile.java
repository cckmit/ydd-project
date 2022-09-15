package com.ydw.oa.wkflow.business_main.wkdoc.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

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
 * @since 2020-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "WkDocFile对象", description = "")
public class WkDocFile implements Serializable {

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

	@ApiModelProperty(value = "文档编号")
	@TableField("CODE")
	private String code;

	@ApiModelProperty(value = "文档名称")
	@TableField("NAME")
	private String name;

	@ApiModelProperty(value = "对应流程id")
	@TableField("PID")
	private String pid;

	@ApiModelProperty(value = "对应表格id")
	@TableField("FORM_ID")
	private String formId;

	@ApiModelProperty(value = "对应流程分类")
	@TableField("CATEGORY_ID")
	private String categoryId;

	@ApiModelProperty(value = "发起人员部门id")
	@TableField("DETP_ID")
	private String detpId;

	@ApiModelProperty(value = "发起人员id")
	@TableField("USR_ID")
	private String usrId;

	@ApiModelProperty(value = "发起时间")
	@TableField("START_TIME")
	private Date startTime;

	@ApiModelProperty(value = "是否生成pdf文件(0-否,1-是)")
	@TableField("IS_CREATE_PDF")
	private int isCreatePdf = 0;

	@ApiModelProperty(value = "生成的pdf文件id(fk_file的objectId)")
	@TableField("FILE_ID")
	private String fileId;

}
