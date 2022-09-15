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
@ApiModel(value = "WkDocFileUsr对象", description = "")
public class WkDocFileUsr implements Serializable {

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

	@ApiModelProperty(value = "流程文档id")
	@TableField("WK_DOC_FILE_ID")
	private String wkDocFileId;

	@ApiModelProperty(value = "人员id")
	@TableField("USR_ID")
	private String usrId;

}
