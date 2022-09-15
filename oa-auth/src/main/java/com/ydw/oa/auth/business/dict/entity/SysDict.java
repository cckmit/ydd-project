package com.ydw.oa.auth.business.dict.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
 * @author 冯晓东
 * @since 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SysDict对象", description = "")
public class SysDict implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "字典类型编号")
	@TableField("TYPE_CODE")
	private String typeCode;

	@ApiModelProperty(value = "字典编号")
	@TableField("CODE")
	private String code;

	@ApiModelProperty(value = "字典值")
	@TableField("VALUE")
	private String value;

	@ApiModelProperty(value = "字典说明")
	@TableField("NOTE")
	private String note;

	@ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
	private String objectId;

	@ApiModelProperty(value = "状态  预留")
	@TableField("STATUS")
	private int status;

	@ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "简写 预留")
	@TableField("BRIEF")
	private String brief;

	@ApiModelProperty(value = "编码类型 预留")
	@TableField("CODE_TYPE")
	private String codeType;

	@ApiModelProperty(value = "创建人  预留")
	@TableField("CREATER")
	private String creater;

	@ApiModelProperty(value = "英文备注  预留")
	@TableField("EN_NOTE")
	private String enNote;

	@ApiModelProperty(value = "父级id")
	@TableField("PARENT_ID")
	private String parentId;

	@ApiModelProperty(value = "修改时间  预留")
	@TableField("UPDATED")
	private Date updated = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "修改人员  预留")
	@TableField("UPDATER")
	private String updater;

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableLogic(value = "0", delval = "1")
	@TableField("IS_DELETED")
	private int isDeleted;

	@ApiModelProperty(value = "描述 预留")
	@TableField("DESCRIPTION")
	private String description;

	@ApiModelProperty(value = "下级")
	@TableField(exist = false)
	private List<SysDict> children;

}
