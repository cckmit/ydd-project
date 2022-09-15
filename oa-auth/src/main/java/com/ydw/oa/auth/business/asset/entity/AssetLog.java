package com.ydw.oa.auth.business.asset.entity;

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
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("asset_log")
@ApiModel(value = "AssetLog对象", description = "")
public class AssetLog implements Serializable {

	private static final long serialVersionUID = 1L;

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

	@ApiModelProperty(value = "操作类型（新增、分配、报废、再报废、回收）")
	@TableField("OPERATE")
	private String operate;

	@ApiModelProperty(value = "操作记录")
	@TableField("NOTE")
	private String note;

	@ApiModelProperty(value = "操作人员")
	@TableField("USR_ID")
	private String usrId;

	@ApiModelProperty(value = "固定资产id")
	@TableField("CONTENT_ID")
	private String contentId;

}
