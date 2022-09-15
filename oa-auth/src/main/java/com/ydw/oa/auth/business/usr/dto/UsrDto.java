package com.ydw.oa.auth.business.usr.dto;

import java.sql.Clob;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * UsrDto 系统用户表
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UsrDto 系统用户对象", description = "")
public class UsrDto {

	@ApiModelProperty(value = "主键ID")
	private String objectId;
	@ApiModelProperty(value = "身份证号")
	private String certNo;
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	@ApiModelProperty(value = "所在部门ID")
	private String deptId;
	@ApiModelProperty(value = "电子邮件")
	private String email;
	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	private int isDeleted;
	@ApiModelProperty(value = "最后更新时间")
	private Date lastUpdateTime;
	@ApiModelProperty(value = "电话号码")
	private String mobile;
	@ApiModelProperty(value = "是否需要重置密码(0:否;1:是)")
	private int needResetpwd;
	@ApiModelProperty(value = "密码")
	private String passwd;
	@ApiModelProperty(value = "真实姓名")
	private String realName;
	@ApiModelProperty(value = "状态(0:正常;1:停用)")
	private int status;
	@ApiModelProperty(value = "用户类型(0:超级管理员;1:一般用户)")
	private int type;
	@ApiModelProperty(value = "用户编号")
	private String usrId;
	@ApiModelProperty(value = "登录名")
	private String usrName;
	@ApiModelProperty(value = "扩展内容")
	private Clob variable;
	@ApiModelProperty(value = "描述")
	private String note;
	@ApiModelProperty(value = "签名图片url")
	private String signUrl;
	@ApiModelProperty(value = "角色ids,逗号分隔")
	private String roles;
	@ApiModelProperty(value = "部门ids,逗号分隔")
	private String depts;
	@ApiModelProperty(value = "标签集合,逗号分隔")
	private String signs;
	@ApiModelProperty(value = "微信userid")
	private String wxUserId;
}
