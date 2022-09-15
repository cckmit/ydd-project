package com.ydw.oa.auth.business.doc.entity;

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
 * @since 2020-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DocFile对象", description = "")
public class DocFile implements Serializable {

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

	@ApiModelProperty(value = "名称")
	@TableField("NAME")
	private String name;

	@ApiModelProperty(value = "创建人")
	@TableField("CREATE_USR_ID")
	private String createUsrId;

	@ApiModelProperty(value = "创建部门")
	@TableField("DETP_ID")
	private String deptId;

	@ApiModelProperty(value = "附件id")
	@TableField("FILE_ID")
	private String fileId;

	@ApiModelProperty(value = "权限分配范围")
	@TableField("SCOPE")
	private int scope;

	@TableField(exist = false)
	private List<String> usrIds;

}
