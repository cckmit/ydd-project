package com.ydw.oa.auth.business.contract.entity;

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
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PersContract对象", description = "")
public class PersContract implements Serializable {

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

	@ApiModelProperty(value = "合同名称")
	@TableField("NAME")
	private String name;

	@ApiModelProperty(value = "合同类型： 劳动合同 or 技术合同 or 保密合同")
	@TableField("TYPE")
	private String type;

	@ApiModelProperty(value = "到期时间")
	@TableField("END_TIME")
	private Date endTime;

	@ApiModelProperty(value = "是否到期提醒  是 or 否")
	@TableField("IS_REMIND")
	private String isRemind;

	@ApiModelProperty(value = "合同对应文件id")
	@TableField("FILE_ID")
	private String fileId;

}
