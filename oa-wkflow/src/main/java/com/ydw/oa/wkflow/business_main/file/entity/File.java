package com.ydw.oa.wkflow.business_main.file.entity;

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
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_file")
@ApiModel(value = "File对象", description = "")
public class File {

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

	@ApiModelProperty(value = "源文件名")
	@TableField("FILE_NAME")
	private String fileName;
	
	@ApiModelProperty(value = "保存文件名")
	@TableField("NEW_FILE_NAME")
	private String newFileName;
	
	@ApiModelProperty(value = "保存文件夹")
	@TableField("FOLDER")
	private String folder;

	@ApiModelProperty(value = "保存路径")
	@TableField("FOLDER_URL")
	private String folderUrl;

	@ApiModelProperty(value = "文件类型")
	@TableField("CONTENT_TYPE")
	private String contentType;
	
	@ApiModelProperty(value = "文件大小")
	@TableField("SIZE")
	private long size;

	@ApiModelProperty(value = "备注")
	@TableField("NOTE")
	private String note;

	@TableField(exist = false)
	private String downUrl;
}
