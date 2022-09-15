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
@TableName("asset_content")
@ApiModel(value = "AssetContent对象", description = "")
public class AssetContent implements Serializable {

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

	@ApiModelProperty(value = "类型id")
	@TableField("TYPE_ID")
	private String typeId;

	@ApiModelProperty(value = "编号")
	@TableField("CODE")
	private String code;

	@ApiModelProperty(value = "资产名称")
	@TableField("NAME")
	private String name;


	@ApiModelProperty(value = "信息描述")
	@TableField("NOTE")
	private String note;

	@ApiModelProperty(value = "资产状态（在库、使用中、报废、再报废）")
	@TableField("STATUZ")
	private String statuz = "在库";

	@ApiModelProperty(value = "采购人员")
	@TableField("BUY_USR")
	private String buyUsr;

	@ApiModelProperty(value = "采购人员所属部门id")
	@TableField("BUY_USR_DEPT_ID")
	private String buyUsrDeptId;

	@ApiModelProperty(value = "采购时间")
	@TableField("BUY_TIME")
	private Date buyTime;

	@ApiModelProperty(value = "资产当前所属人（分配后）")
	@TableField("CURRENT_USR_ID")
	private String currentUsrId;

	@ApiModelProperty(value = "资产当前所属部门（分配后）")
	@TableField("CURRENT_USR_DEPT_ID")
	private String currentUsrDeptId;

	@ApiModelProperty(value = "申请人员")
	@TableField("APPLY_USR_ID")
	private String applyUsrId;

	@ApiModelProperty(value = "数量")
	@TableField("NUMBER")
	private String number;
}
